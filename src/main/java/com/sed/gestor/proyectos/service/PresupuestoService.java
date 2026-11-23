package com.sed.gestor.proyectos.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sed.gestor.proyectos.dto.MetricasFuenteDTO;
import com.sed.gestor.proyectos.dto.PresupuestoResumenDTO;
import com.sed.gestor.proyectos.dto.ResumenProyectoPresupuestoDTO;
import com.sed.gestor.proyectos.exception.PresupuestoInsuficienteException;
import com.sed.gestor.proyectos.model.Actividad;
import com.sed.gestor.proyectos.model.DetalleMovimiento;
import com.sed.gestor.proyectos.model.MovimientoPresupuestal;
import com.sed.gestor.proyectos.model.Proyecto;
import com.sed.gestor.proyectos.model.enums.FuenteFinanciacion;
import com.sed.gestor.proyectos.model.enums.OperacionActualizacion;
import com.sed.gestor.proyectos.model.enums.TipoMovimiento;
import com.sed.gestor.proyectos.repository.ActividadRepository;
import com.sed.gestor.proyectos.repository.DetalleMovimientoRepository;
import com.sed.gestor.proyectos.repository.MovimientoPresupuestalRepository;
import com.sed.gestor.proyectos.repository.ProyectoRepository;

@Service
public class PresupuestoService {
    private final MovimientoPresupuestalRepository movimientoRepository;
    private final DetalleMovimientoRepository detalleRepository;
    private final ActividadRepository actividadRepository;
    private final ProyectoRepository proyectoRepository;

    public PresupuestoService(MovimientoPresupuestalRepository movimientoRepository,
                              DetalleMovimientoRepository detalleRepository,
                              ActividadRepository actividadRepository,
                              ProyectoRepository proyectoRepository) {
        this.movimientoRepository = movimientoRepository;
        this.detalleRepository = detalleRepository;
        this.actividadRepository = actividadRepository;
        this.proyectoRepository = proyectoRepository;
    }

    @Transactional(readOnly = true)
    public BigDecimal obtenerApropiacionInicialActividadYFuente(Actividad act, FuenteFinanciacion fuente) {
        BigDecimal initMov = detalleRepository.sumarMontoPorActividadFuenteYTipo(
                act.getId(), fuente, TipoMovimiento.APROPIACION_INICIAL);

        if (initMov != null && initMov.compareTo(BigDecimal.ZERO) > 0) {
            return initMov;
        }
        if (act.getFuente() == fuente && act.getPresupuestoInicial() != null) {
            return act.getPresupuestoInicial();
        }
        return BigDecimal.ZERO;
    }

    @Transactional(readOnly = true)
    public BigDecimal obtenerSaldoDisponible(Long actividadId, FuenteFinanciacion fuente) {
        Actividad act = actividadRepository.findById(actividadId)
                .orElseThrow(() -> new IllegalArgumentException("Actividad no encontrada con ID: " + actividadId));

        BigDecimal init = obtenerApropiacionInicialActividadYFuente(act, fuente);
        BigDecimal adic = detalleRepository.sumarMontoPorActividadFuenteYTipo(actividadId, fuente, TipoMovimiento.ADICION_CREDITO);
        BigDecimal red = detalleRepository.sumarMontoPorActividadFuenteYTipo(actividadId, fuente, TipoMovimiento.REDUCCION_CONTRACREDITO).abs();
        BigDecimal ejec = detalleRepository.sumarMontoPorActividadFuenteYTipo(actividadId, fuente, TipoMovimiento.EJECUCION_CDP_RP).abs();

        return init.add(adic).subtract(red).subtract(ejec);
    }

    @Transactional
    public MovimientoPresupuestal registrarMovimiento(MovimientoPresupuestal movimiento) {
        movimiento.setFechaRegistro(LocalDateTime.now());
        TipoMovimiento tipo = movimiento.getTipoMovimiento();

        for (DetalleMovimiento detalle : movimiento.getDetalles()) {
            detalle.setMovimiento(movimiento);

            // Normalizar signo según naturaleza del movimiento
            BigDecimal montoAbs = detalle.getMonto().abs();
            if (tipo == TipoMovimiento.EJECUCION_CDP_RP || tipo == TipoMovimiento.REDUCCION_CONTRACREDITO) {
                detalle.setMonto(montoAbs.negate());
            } else {
                detalle.setMonto(montoAbs);
            }

            if (detalle.getMonto().compareTo(BigDecimal.ZERO) < 0) {
                BigDecimal saldoActual = obtenerSaldoDisponible(detalle.getActividad().getId(), detalle.getFuente());
                BigDecimal montoDebitar = detalle.getMonto().abs();

                if (saldoActual.compareTo(montoDebitar) < 0) {
                    throw new PresupuestoInsuficienteException(
                            String.format("Saldo insuficiente en la actividad '%s' para la fuente %s. Disponible: $%,.2f, Solicitando: $%,.2f",
                                    detalle.getActividad().getNombre(),
                                    detalle.getFuente().getEtiqueta(),
                                    saldoActual,
                                    montoDebitar)
                    );
                }
            }
        }
        return movimientoRepository.save(movimiento);
    }

    @Transactional(readOnly = true)
    public List<PresupuestoResumenDTO> obtenerResumenPresupuestal(Long proyectoId) {
        List<Actividad> actividades = actividadRepository.findByProyectoId(proyectoId);
        List<PresupuestoResumenDTO> listaResumen = new ArrayList<>();

        for (Actividad act : actividades) {
            PresupuestoResumenDTO dto = new PresupuestoResumenDTO();
            dto.setActividadId(act.getId());
            dto.setNombreActividad(act.getNombre());

            BigDecimal acumuladoDefinitivaTotal = BigDecimal.ZERO;
            BigDecimal acumuladoEjecutadoTotal = BigDecimal.ZERO;
            BigDecimal acumuladoDisponibleTotal = BigDecimal.ZERO;

            for (FuenteFinanciacion fuente : FuenteFinanciacion.values()) {
                BigDecimal init = obtenerApropiacionInicialActividadYFuente(act, fuente);
                BigDecimal adic = detalleRepository.sumarMontoPorActividadFuenteYTipo(act.getId(), fuente, TipoMovimiento.ADICION_CREDITO);
                BigDecimal red = detalleRepository.sumarMontoPorActividadFuenteYTipo(act.getId(), fuente, TipoMovimiento.REDUCCION_CONTRACREDITO).abs();
                BigDecimal ejec = detalleRepository.sumarMontoPorActividadFuenteYTipo(act.getId(), fuente, TipoMovimiento.EJECUCION_CDP_RP).abs();

                BigDecimal def = init.add(adic).subtract(red);
                BigDecimal disp = def.subtract(ejec);

                MetricasFuenteDTO metrica = dto.getMetrica(fuente);
                metrica.setFuente(fuente);
                metrica.setApropiacionInicial(init);
                metrica.setAdiciones(adic);
                metrica.setReducciones(red);
                metrica.setApropiacionDefinitiva(def);
                metrica.setEjecutado(ejec);
                metrica.setSaldoDisponible(disp);

                acumuladoDefinitivaTotal = acumuladoDefinitivaTotal.add(def);
                acumuladoEjecutadoTotal = acumuladoEjecutadoTotal.add(ejec);
                acumuladoDisponibleTotal = acumuladoDisponibleTotal.add(disp);
            }

            BigDecimal pctEjecucion = BigDecimal.ZERO;
            if (acumuladoDefinitivaTotal.compareTo(BigDecimal.ZERO) > 0) {
                pctEjecucion = acumuladoEjecutadoTotal.multiply(new BigDecimal("100"))
                        .divide(acumuladoDefinitivaTotal, 2, RoundingMode.HALF_UP);
            }

            dto.setDisponibleTotal(acumuladoDisponibleTotal);
            dto.setPorcentajeEjecucion(pctEjecucion);

            listaResumen.add(dto);
        }
        return listaResumen;
    }

    @Transactional(readOnly = true)
    public ResumenProyectoPresupuestoDTO obtenerResumenGlobalProyecto(List<PresupuestoResumenDTO> resumenActividades) {
        ResumenProyectoPresupuestoDTO resumenGlobal = new ResumenProyectoPresupuestoDTO();
        if (resumenActividades == null || resumenActividades.isEmpty()) {
            return resumenGlobal;
        }

        BigDecimal totalInicial = BigDecimal.ZERO;
        BigDecimal totalAdiciones = BigDecimal.ZERO;
        BigDecimal totalReducciones = BigDecimal.ZERO;
        BigDecimal totalDefinitivo = BigDecimal.ZERO;
        BigDecimal totalGastado = BigDecimal.ZERO;
        BigDecimal totalDisponible = BigDecimal.ZERO;

        for (PresupuestoResumenDTO actResumen : resumenActividades) {
            for (MetricasFuenteDTO mf : actResumen.getValoresPorFuente().values()) {
                totalInicial = totalInicial.add(mf.getApropiacionInicial());
                totalAdiciones = totalAdiciones.add(mf.getAdiciones());
                totalReducciones = totalReducciones.add(mf.getReducciones());
                totalDefinitivo = totalDefinitivo.add(mf.getApropiacionDefinitiva());
                totalGastado = totalGastado.add(mf.getEjecutado());
                totalDisponible = totalDisponible.add(mf.getSaldoDisponible());
            }
        }

        BigDecimal pctGlobal = BigDecimal.ZERO;
        if (totalDefinitivo.compareTo(BigDecimal.ZERO) > 0) {
            pctGlobal = totalGastado.multiply(new BigDecimal("100"))
                    .divide(totalDefinitivo, 2, RoundingMode.HALF_UP);
        }

        resumenGlobal.setValorTotalInicial(totalInicial);
        resumenGlobal.setTotalModificado(totalAdiciones.subtract(totalReducciones));
        resumenGlobal.setValorTotalDefinitivo(totalDefinitivo);
        resumenGlobal.setTotalGastado(totalGastado);
        resumenGlobal.setTotalDisponible(totalDisponible);
        resumenGlobal.setPorcentajeEjecucion(pctGlobal);

        return resumenGlobal;
    }

    @Transactional
    public MovimientoPresupuestal procesarActualizacionActividad(Long proyectoId,
                                                                 Long actividadId,
                                                                 OperacionActualizacion operacion,
                                                                 String numeroDecreto,
                                                                 FuenteFinanciacion fuente,
                                                                 String tipoAjuste, // "ADICION" o "REDUCCION"
                                                                 BigDecimal montoAjuste,
                                                                 String observaciones) {
        if (operacion != OperacionActualizacion.OP_07_AJUSTE_COSTOS_INICIALES) {
            throw new IllegalArgumentException("La operación seleccionada aún no está habilitada para ejecución.");
        }
        if (montoAjuste == null || montoAjuste.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto del ajuste debe ser mayor a cero.");
        }

        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));
        Actividad actividad = actividadRepository.findById(actividadId)
                .orElseThrow(() -> new IllegalArgumentException("Actividad no encontrada"));

        TipoMovimiento tipoMov = "REDUCCION".equalsIgnoreCase(tipoAjuste)
                ? TipoMovimiento.REDUCCION_CONTRACREDITO
                : TipoMovimiento.ADICION_CREDITO;

        MovimientoPresupuestal movimiento = new MovimientoPresupuestal();
        movimiento.setProyecto(proyecto);
        movimiento.setTipoMovimiento(tipoMov);
        movimiento.setNumeroDecreto(numeroDecreto);
        movimiento.setObservaciones("[" + operacion.getEtiqueta() + "] " + (observaciones != null ? observaciones : ""));

        DetalleMovimiento detalle = new DetalleMovimiento();
        detalle.setActividad(actividad);
        detalle.setFuente(fuente);
        detalle.setMonto(montoAjuste);
        movimiento.addDetalle(detalle);

        return registrarMovimiento(movimiento);
    }
}