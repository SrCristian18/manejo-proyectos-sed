package com.sed.gestor.proyectos.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sed.gestor.proyectos.exception.PresupuestoInsuficienteException;
import com.sed.gestor.proyectos.model.DetalleMovimiento;
import com.sed.gestor.proyectos.model.MovimientoPresupuestal;
import com.sed.gestor.proyectos.model.enums.FuenteFinanciacion;
import com.sed.gestor.proyectos.repository.DetalleMovimientoRepository;
import com.sed.gestor.proyectos.repository.MovimientoPresupuestalRepository;

@Service
public class PresupuestoService {
    private final MovimientoPresupuestalRepository movimientoRepository;
    private final DetalleMovimientoRepository detalleRepository;

    public PresupuestoService(MovimientoPresupuestalRepository movimientoRepository,
        DetalleMovimientoRepository detalleRepository) {
            this.movimientoRepository = movimientoRepository;
            this.detalleRepository = detalleRepository;
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

}