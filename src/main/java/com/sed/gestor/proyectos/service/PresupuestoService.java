package com.sed.gestor.proyectos.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sed.gestor.proyectos.dto.AjusteCostoDTO;
import com.sed.gestor.proyectos.dto.MetricasFuenteDTO;
import com.sed.gestor.proyectos.dto.PresupuestoResumenDTO;
import com.sed.gestor.proyectos.dto.SolicitudActualizacionDTO;
import com.sed.gestor.proyectos.exception.PresupuestoInsuficienteException;
import com.sed.gestor.proyectos.model.Actividad;
import com.sed.gestor.proyectos.model.DetalleMovimiento;
import com.sed.gestor.proyectos.model.MovimientoPresupuestal;
import com.sed.gestor.proyectos.model.Proyecto;
import com.sed.gestor.proyectos.model.enums.FuenteFinanciacion;
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
        DetalleMovimientoRepository detalleRepository, ActividadRepository actividadRepository, ProyectoRepository proyectoRepository) {
            this.movimientoRepository = movimientoRepository;
            this.detalleRepository = detalleRepository;
            this.actividadRepository = actividadRepository;
            this.proyectoRepository = proyectoRepository;
        }
    
    @Transactional(readOnly = true)
    public BigDecimal obtenerSaldoDisponible(Long actividadId, FuenteFinanciacion fuente){
        return detalleRepository.calcularSaldoAcumuladoByActividadYFuente(actividadId, fuente);
    }

    @Transactional
    public MovimientoPresupuestal registrarMovimiento(MovimientoPresupuestal movimiento){
        movimiento.setFechaRegistro(LocalDateTime.now());

        for (DetalleMovimiento detalle : movimiento.getDetalles()){
            detalle.setMovimiento(movimiento);

            if (detalle.getMonto().compareTo(BigDecimal.ZERO)<0){
                BigDecimal saldoActual = obtenerSaldoDisponible(detalle.getActividad().getId(),detalle.getFuente());
                BigDecimal montoDebitar = detalle.getMonto().abs();

                if(saldoActual.compareTo(montoDebitar)<0){
                    throw new PresupuestoInsuficienteException(
                        String.format("Saldo insuficiente en la actividad '%s' para la fuente %s. Disponible: %s, Solicitando debitar: %s",
                        detalle.getActividad().getNombre(),
                        detalle.getFuente(),
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

            // Bucle dinámico sobre CUALQUIER cantidad de fuentes de financiación
            for (FuenteFinanciacion fuente : FuenteFinanciacion.values()) {
                    BigDecimal init = detalleRepository.sumarMontoPorActividadFuenteYTipo(act.getId(), fuente, TipoMovimiento.APROPIACION_INICIAL);
                    BigDecimal adic = detalleRepository.sumarMontoPorActividadFuenteYTipo(act.getId(), fuente, TipoMovimiento.ADICION_CREDITO);
                    BigDecimal red = detalleRepository.sumarMontoPorActividadFuenteYTipo(act.getId(), fuente, TipoMovimiento.REDUCCION_CONTRACREDITO).abs();
                    BigDecimal ejec = detalleRepository.sumarMontoPorActividadFuenteYTipo(act.getId(), fuente, TipoMovimiento.EJECUCION_CDP_RP).abs();

                    BigDecimal def = init.add(adic).subtract(red);
                    BigDecimal disp = def.subtract(ejec);

                    MetricasFuenteDTO metrica = dto.getMetrica(fuente);
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

    @Transactional
    public void procesarActualizacionProyecto(SolicitudActualizacionDTO solicitud){
        Proyecto proyecto = proyectoRepository.findById(solicitud.getProyectoId())
            .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));
        
        MovimientoPresupuestal movimiento = new MovimientoPresupuestal();
        movimiento.setProyecto(proyecto);
        movimiento.setTipoMovimiento(TipoMovimiento.ADICION_CREDITO);
        movimiento.setNumeroDecreto(solicitud.getNumeroDecreto());

        for (AjusteCostoDTO ajuste : solicitud.getAjustes()){
            Actividad actividad = actividadRepository.findById(ajuste.getActividadId())
                .orElseThrow(() -> new IllegalArgumentException("Actividad no encontrada"));

            BigDecimal variacion = ajuste.getCambioEnCosto();

            if (variacion.compareTo(BigDecimal.ZERO) != 0) {
                DetalleMovimiento detalle = new DetalleMovimiento();
                detalle.setActividad(actividad);
                detalle.setFuente(ajuste.getFuente() != null ? ajuste.getFuente() : FuenteFinanciacion.LIBRE_DESTINACION);
                detalle.setMonto(variacion);
                movimiento.addDetalle(detalle);

                // Reajustamos la apropiación de la actividad
                BigDecimal nuevaApropiacion = actividad.getPresupuestoInicial().add(variacion);
                actividad.setPresupuestoInicial(nuevaApropiacion);
                actividadRepository.save(actividad);
            }
        }
        if (!movimiento.getDetalles().isEmpty()) {
            registrarMovimiento(movimiento);
        }
    }
}