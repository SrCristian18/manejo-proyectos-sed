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
import com.sed.gestor.proyectos.dto.RubroEjecucionDTO;
import com.sed.gestor.proyectos.exception.PresupuestoInsuficienteException;
import com.sed.gestor.proyectos.model.Actividad;
import com.sed.gestor.proyectos.model.DetalleMovimiento;
import com.sed.gestor.proyectos.model.MovimientoPresupuestal;
import com.sed.gestor.proyectos.model.Proyecto;
import com.sed.gestor.proyectos.model.RubroGasto;
import com.sed.gestor.proyectos.model.enums.FuenteFinanciacion;
import com.sed.gestor.proyectos.model.enums.OperacionActualizacion;
import com.sed.gestor.proyectos.model.enums.TipoMovimiento;
import com.sed.gestor.proyectos.repository.ActividadRepository;
import com.sed.gestor.proyectos.repository.DetalleMovimientoRepository;
import com.sed.gestor.proyectos.repository.MovimientoPresupuestalRepository;
import com.sed.gestor.proyectos.repository.ProyectoRepository;
import com.sed.gestor.proyectos.repository.RubroGastoRepository;

@Service
public class PresupuestoService {
    private final MovimientoPresupuestalRepository movimientoRepository;
    private final DetalleMovimientoRepository detalleRepository;
    private final ActividadRepository actividadRepository;
    private final ProyectoRepository proyectoRepository;
    private final RubroGastoRepository rubroGastoRepository;

    public PresupuestoService(MovimientoPresupuestalRepository movimientoRepository,
                              DetalleMovimientoRepository detalleRepository,
                              ActividadRepository actividadRepository,
                              ProyectoRepository proyectoRepository,
                              RubroGastoRepository rubroGastoRepository) {
        this.movimientoRepository = movimientoRepository;
        this.detalleRepository = detalleRepository;
        this.actividadRepository = actividadRepository;
        this.proyectoRepository = proyectoRepository;
        this.rubroGastoRepository = rubroGastoRepository;
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
        BigDecimal redist = detalleRepository.sumarMontoPorActividadFuenteYTipo(actividadId, fuente, TipoMovimiento.REDISTRIBUCION);
        BigDecimal ejec = detalleRepository.sumarMontoPorActividadFuenteYTipo(actividadId, fuente, TipoMovimiento.EJECUCION_CDP_RP).abs();

        return init.add(adic).subtract(red).add(redist).subtract(ejec);
    }

    @Transactional
    public MovimientoPresupuestal registrarMovimiento(MovimientoPresupuestal movimiento) {
        movimiento.setFechaRegistro(LocalDateTime.now());
        TipoMovimiento tipo = movimiento.getTipoMovimiento();

        for (DetalleMovimiento detalle : movimiento.getDetalles()) {
            detalle.setMovimiento(movimiento);

            BigDecimal montoAbs = detalle.getMonto().abs();
            if (tipo == TipoMovimiento.EJECUCION_CDP_RP || tipo == TipoMovimiento.REDUCCION_CONTRACREDITO) {
                detalle.setMonto(montoAbs.negate());
            } else if (tipo == TipoMovimiento.ADICION_CREDITO || tipo == TipoMovimiento.APROPIACION_INICIAL) {
                detalle.setMonto(montoAbs);
            }
            // Si es REDISTRIBUCION, conserva el signo asignado (-monto en origen, +monto en destino)

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

            if (detalle.getMonto().compareTo(BigDecimal.ZERO) < 0 && detalle.getRubro() != null) {
                RubroGasto rubro = detalle.getRubro();
                BigDecimal ejecutadoRubro = detalleRepository
                        .sumarMontoPorRubroYTipo(rubro.getId(), TipoMovimiento.EJECUCION_CDP_RP).abs();
                BigDecimal disponibleRubro = rubro.getMontoAsignado().subtract(ejecutadoRubro);
                BigDecimal montoDebitar = detalle.getMonto().abs();

                if (disponibleRubro.compareTo(montoDebitar) < 0) {
                    throw new PresupuestoInsuficienteException(
                            String.format("Saldo insuficiente en el rubro '%s'. Disponible en rubro: $%,.2f, Solicitando: $%,.2f",
                                    rubro.getConcepto(), disponibleRubro, montoDebitar)
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
                BigDecimal redist = detalleRepository.sumarMontoPorActividadFuenteYTipo(act.getId(), fuente, TipoMovimiento.REDISTRIBUCION);
                BigDecimal ejec = detalleRepository.sumarMontoPorActividadFuenteYTipo(act.getId(), fuente, TipoMovimiento.EJECUCION_CDP_RP).abs();

                BigDecimal def = init.add(adic).subtract(red).add(redist);
                BigDecimal disp = def.subtract(ejec);

                MetricasFuenteDTO metrica = dto.getMetrica(fuente);
                metrica.setFuente(fuente);
                metrica.setApropiacionInicial(init);
                metrica.setAdiciones(adic.add(redist)); // Refleja la variación neta en modificadoNeto
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

    //metricas
    @Transactional(readOnly = true)
    public List<MetricasFuenteDTO> obtenerMetricasPorFuenteProyecto(List<PresupuestoResumenDTO> resumenActividades) {
        List<MetricasFuenteDTO> resultado = new ArrayList<>();

        for (FuenteFinanciacion fuente : FuenteFinanciacion.values()) {
            BigDecimal init = BigDecimal.ZERO;
            BigDecimal adic = BigDecimal.ZERO;
            BigDecimal red = BigDecimal.ZERO;
            BigDecimal def = BigDecimal.ZERO;
            BigDecimal ejec = BigDecimal.ZERO;
            BigDecimal disp = BigDecimal.ZERO;

            if (resumenActividades != null) {
                for (PresupuestoResumenDTO actResumen : resumenActividades) {
                    MetricasFuenteDTO mf = actResumen.getMetrica(fuente);
                    init = init.add(mf.getApropiacionInicial());
                    adic = adic.add(mf.getAdiciones());
                    red = red.add(mf.getReducciones());
                    def = def.add(mf.getApropiacionDefinitiva());
                    ejec = ejec.add(mf.getEjecutado());
                    disp = disp.add(mf.getSaldoDisponible());
                }
            }

            MetricasFuenteDTO consolidadoFuente = new MetricasFuenteDTO();
            consolidadoFuente.setFuente(fuente);
            consolidadoFuente.setApropiacionInicial(init);
            consolidadoFuente.setAdiciones(adic);
            consolidadoFuente.setReducciones(red);
            consolidadoFuente.setApropiacionDefinitiva(def);
            consolidadoFuente.setEjecutado(ejec);
            consolidadoFuente.setSaldoDisponible(disp);

            resultado.add(consolidadoFuente);
        }
        return resultado;
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
                                                                 String tipoAjuste,
                                                                 BigDecimal montoAjuste,
                                                                 Long actividadDestinoId,
                                                                 FuenteFinanciacion fuenteDestino,
                                                                 String observaciones) {
        if (!operacion.isHabilitada()) {
            throw new IllegalArgumentException("La operación seleccionada aún no está habilitada.");
        }

        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));
        Actividad actividadOrigen = actividadRepository.findById(actividadId)
                .orElseThrow(() -> new IllegalArgumentException("Actividad origen no encontrada"));

        String obsConPrefijo = "[" + operacion.getEtiqueta() + "] " + (observaciones != null ? observaciones : "");

        switch (operacion) {
            case OP_07_AJUSTE_COSTOS_INICIALES: {
                if (montoAjuste == null || montoAjuste.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalArgumentException("El monto del ajuste debe ser mayor a cero.");
                }
                TipoMovimiento tipoMov = "REDUCCION".equalsIgnoreCase(tipoAjuste)
                        ? TipoMovimiento.REDUCCION_CONTRACREDITO
                        : TipoMovimiento.ADICION_CREDITO;

                MovimientoPresupuestal movimiento = new MovimientoPresupuestal();
                movimiento.setProyecto(proyecto);
                movimiento.setTipoMovimiento(tipoMov);
                movimiento.setNumeroDecreto(numeroDecreto);
                movimiento.setObservaciones(obsConPrefijo);

                DetalleMovimiento detalle = new DetalleMovimiento();
                detalle.setActividad(actividadOrigen);
                detalle.setFuente(fuente);
                detalle.setMonto(montoAjuste);
                movimiento.addDetalle(detalle);

                return registrarMovimiento(movimiento);
            }

            case OP_03_REDISTRIBUCION_COSTOS: {
                if (montoAjuste == null || montoAjuste.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalArgumentException("El monto a redistribuir debe ser mayor a cero.");
                }
                if (actividadDestinoId == null || actividadDestinoId.equals(actividadId)) {
                    throw new IllegalArgumentException("Debe seleccionar una actividad destino diferente a la actividad origen.");
                }
                Actividad actividadDestino = actividadRepository.findById(actividadDestinoId)
                        .orElseThrow(() -> new IllegalArgumentException("Actividad destino no encontrada"));

                MovimientoPresupuestal movimiento = new MovimientoPresupuestal();
                movimiento.setProyecto(proyecto);
                movimiento.setTipoMovimiento(TipoMovimiento.REDISTRIBUCION);
                movimiento.setNumeroDecreto(numeroDecreto);
                movimiento.setObservaciones(obsConPrefijo + " (Traslado hacia: " + actividadDestino.getNombre() + ")");

                // Débito en actividad origen
                DetalleMovimiento debito = new DetalleMovimiento();
                debito.setActividad(actividadOrigen);
                debito.setFuente(fuente);
                debito.setMonto(montoAjuste.abs().negate());
                movimiento.addDetalle(debito);

                // Crédito en actividad destino
                DetalleMovimiento credito = new DetalleMovimiento();
                credito.setActividad(actividadDestino);
                credito.setFuente(fuente);
                credito.setMonto(montoAjuste.abs());
                movimiento.addDetalle(credito);

                return registrarMovimiento(movimiento);
            }

            case OP_08_FUENTES_FINANCIACION: {
                if (montoAjuste == null || montoAjuste.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalArgumentException("El monto a reclasificar entre fuentes debe ser mayor a cero.");
                }
                if (fuenteDestino == null || fuenteDestino == fuente) {
                    throw new IllegalArgumentException("Debe seleccionar una fuente destino diferente a la fuente origen.");
                }

                MovimientoPresupuestal movimiento = new MovimientoPresupuestal();
                movimiento.setProyecto(proyecto);
                movimiento.setTipoMovimiento(TipoMovimiento.REDISTRIBUCION);
                movimiento.setNumeroDecreto(numeroDecreto);
                movimiento.setObservaciones(obsConPrefijo + " (" + fuente.getEtiqueta() + " -> " + fuenteDestino.getEtiqueta() + ")");

                // Débito en fuente origen
                DetalleMovimiento debito = new DetalleMovimiento();
                debito.setActividad(actividadOrigen);
                debito.setFuente(fuente);
                debito.setMonto(montoAjuste.abs().negate());
                movimiento.addDetalle(debito);

                // Crédito en fuente destino
                DetalleMovimiento credito = new DetalleMovimiento();
                credito.setActividad(actividadOrigen);
                credito.setFuente(fuenteDestino);
                credito.setMonto(montoAjuste.abs());
                movimiento.addDetalle(credito);

                return registrarMovimiento(movimiento);
            }

            case OP_13_CARGUE_DOCUMENTOS: {
                MovimientoPresupuestal movimiento = new MovimientoPresupuestal();
                movimiento.setProyecto(proyecto);
                movimiento.setTipoMovimiento(TipoMovimiento.REDISTRIBUCION);
                movimiento.setNumeroDecreto(numeroDecreto);
                movimiento.setObservaciones(obsConPrefijo);

                DetalleMovimiento registroDoc = new DetalleMovimiento();
                registroDoc.setActividad(actividadOrigen);
                registroDoc.setFuente(fuente != null ? fuente : FuenteFinanciacion.LIBRE_DESTINACION);
                registroDoc.setMonto(BigDecimal.ZERO);
                movimiento.addDetalle(registroDoc);

                return registrarMovimiento(movimiento);
            }

            default:
                throw new IllegalArgumentException("Operación no soportada.");
        }
    }

    //presupuesto
    @Transactional(readOnly = true)
    public List<RubroEjecucionDTO> obtenerDetalleRubrosActividad(Long actividadId) {
        List<RubroGasto> rubros = rubroGastoRepository.findByActividadId(actividadId);
        List<RubroEjecucionDTO> resultado = new ArrayList<>();

        for (RubroGasto r : rubros) {
            BigDecimal ejecutado = detalleRepository
                    .sumarMontoPorRubroYTipo(r.getId(), TipoMovimiento.EJECUCION_CDP_RP).abs();
            BigDecimal asignado = r.getMontoAsignado() != null ? r.getMontoAsignado() : BigDecimal.ZERO;
            BigDecimal disponible = asignado.subtract(ejecutado);

            BigDecimal pct = BigDecimal.ZERO;
            if (asignado.compareTo(BigDecimal.ZERO) > 0) {
                pct = ejecutado.multiply(new BigDecimal("100"))
                        .divide(asignado, 2, RoundingMode.HALF_UP);
            }

            RubroEjecucionDTO dto = new RubroEjecucionDTO();
            dto.setId(r.getId());
            dto.setConcepto(r.getConcepto());
            dto.setFuente(r.getFuente());
            dto.setMontoAsignado(asignado);
            dto.setMontoEjecutado(ejecutado);
            dto.setSaldoDisponible(disponible);
            dto.setPorcentajeEjecucion(pct);
            resultado.add(dto);
        }
        return resultado;
    }

    @Transactional
    public RubroGasto guardarOActualizarRubro(Long rubroId, Long actividadId, String concepto,
                                              FuenteFinanciacion fuente, BigDecimal montoAsignado) {
        if (montoAsignado == null || montoAsignado.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto asignado al rubro no puede ser negativo.");
        }

        Actividad actividad = actividadRepository.findById(actividadId)
                .orElseThrow(() -> new IllegalArgumentException("Actividad no encontrada"));

        // Calcular presupuesto definitivo disponible en esa fuente para la actividad
        BigDecimal init = obtenerApropiacionInicialActividadYFuente(actividad, fuente);
        BigDecimal adic = detalleRepository.sumarMontoPorActividadFuenteYTipo(actividadId, fuente, TipoMovimiento.ADICION_CREDITO);
        BigDecimal red = detalleRepository.sumarMontoPorActividadFuenteYTipo(actividadId, fuente, TipoMovimiento.REDUCCION_CONTRACREDITO).abs();
        BigDecimal definitivaFuente = init.add(adic).subtract(red);

        // Sumar lo ya asignado en otros rubros de la misma fuente
        List<RubroGasto> rubrosActividad = rubroGastoRepository.findByActividadId(actividadId);
        BigDecimal asignadoOtrosRubros = rubrosActividad.stream()
                .filter(r -> r.getFuente() == fuente && (rubroId == null || !r.getId().equals(rubroId)))
                .map(RubroGasto::getMontoAsignado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (asignadoOtrosRubros.add(montoAsignado).compareTo(definitivaFuente) > 0) {
            BigDecimal cupoRestante = definitivaFuente.subtract(asignadoOtrosRubros);
            throw new IllegalArgumentException(
                    String.format("El monto supera el presupuesto definitivo de la fuente %s. Cupo sin distribuir en rubros: $%,.2f",
                            fuente.getEtiqueta(), cupoRestante.max(BigDecimal.ZERO))
            );
        }

        RubroGasto rubro;
        if (rubroId != null) {
            rubro = rubroGastoRepository.findById(rubroId)
                    .orElseThrow(() -> new IllegalArgumentException("Rubro no encontrado"));

            BigDecimal yaEjecutado = detalleRepository
                    .sumarMontoPorRubroYTipo(rubroId, TipoMovimiento.EJECUCION_CDP_RP).abs();
            if (montoAsignado.compareTo(yaEjecutado) < 0) {
                throw new IllegalArgumentException(
                        String.format("No puede reducir el rubro por debajo de lo ya ejecutado ($%,.2f).", yaEjecutado)
                );
            }
        } else {
            rubro = new RubroGasto();
            rubro.setActividad(actividad);
        }

        rubro.setConcepto(concepto);
        rubro.setFuente(fuente);
        rubro.setMontoAsignado(montoAsignado);

        return rubroGastoRepository.save(rubro);
    }

    @Transactional(readOnly = true)
    public List<DetalleMovimiento> obtenerHistorialEjecucionesActividad(Long actividadId) {
        return detalleRepository.findEjecucionesPorActividad(actividadId, TipoMovimiento.EJECUCION_CDP_RP);
    }
}