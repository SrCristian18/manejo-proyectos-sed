package com.sed.gestor.proyectos.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sed.gestor.proyectos.dto.PresupuestoResumenDTO;
import com.sed.gestor.proyectos.exception.PresupuestoInsuficienteException;
import com.sed.gestor.proyectos.model.Actividad;
import com.sed.gestor.proyectos.model.DetalleMovimiento;
import com.sed.gestor.proyectos.model.MovimientoPresupuestal;
import com.sed.gestor.proyectos.model.enums.FuenteFinanciacion;
import com.sed.gestor.proyectos.model.enums.TipoMovimiento;
import com.sed.gestor.proyectos.repository.ActividadRepository;
import com.sed.gestor.proyectos.repository.DetalleMovimientoRepository;
import com.sed.gestor.proyectos.repository.MovimientoPresupuestalRepository;

@Service
public class PresupuestoService {
    private final MovimientoPresupuestalRepository movimientoRepository;
    private final DetalleMovimientoRepository detalleRepository;
    private final ActividadRepository actividadRepository;

    public PresupuestoService(MovimientoPresupuestalRepository movimientoRepository,
        DetalleMovimientoRepository detalleRepository, ActividadRepository actividadRepository) {
            this.movimientoRepository = movimientoRepository;
            this.detalleRepository = detalleRepository;
            this.actividadRepository = actividadRepository;
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
    
    public List<PresupuestoResumenDTO> obtenerResumenPresupuestal(Long proyectoId){
        List<Actividad> actividades = actividadRepository.findByProyectoId(proyectoId);
        List<PresupuestoResumenDTO> listaResumen = new ArrayList<>();

        for(Actividad act : actividades){
            for(FuenteFinanciacion fuente: FuenteFinanciacion.values()){
                BigDecimal inicial = detalleRepository.sumarMontoPorActividadFuenteYTipo(act.getId(), fuente, TipoMovimiento.APROPIACION_INICIAL);
                BigDecimal adiciones = detalleRepository.sumarMontoPorActividadFuenteYTipo(act.getId(), fuente, TipoMovimiento.ADICION_CREDITO);
                BigDecimal reducciones = detalleRepository.sumarMontoPorActividadFuenteYTipo(act.getId(), fuente, TipoMovimiento.REDUCCION_CONTRACREDITO).abs();
                BigDecimal ejecucion = detalleRepository.sumarMontoPorActividadFuenteYTipo(act.getId(), fuente, TipoMovimiento.EJECUCION_CDP_RP).abs();

                // Cálculos dinámicos
                BigDecimal definitiva = inicial.add(adiciones).subtract(reducciones);
                BigDecimal disponible = definitiva.subtract(ejecucion);
                
                BigDecimal pctEjecucion = BigDecimal.ZERO;
                if (definitiva.compareTo(BigDecimal.ZERO) > 0) {
                pctEjecucion = ejecucion.multiply(new BigDecimal("100")).divide(definitiva, 2, RoundingMode.HALF_UP);
                }
                
                if (definitiva.compareTo(BigDecimal.ZERO) > 0 || disponible.compareTo(BigDecimal.ZERO) != 0) {
                    PresupuestoResumenDTO dto = new PresupuestoResumenDTO();
                    dto.setActividadId(act.getId());
                    dto.setNombreActividad(act.getNombre());
                    dto.setFuente(fuente);
                    dto.setApropiacionInicial(inicial);
                    dto.setAdiciones(adiciones);
                    dto.setReducciones(reducciones);
                    dto.setApropiacionDefinitiva(definitiva);
                    dto.setEjecutadoCompromisos(ejecucion);
                    dto.setSaldoDisponible(disponible);
                    dto.setPorcentajeEjecucion(pctEjecucion);

                    listaResumen.add(dto);
                }
            }
        }
            return listaResumen;
    }

}