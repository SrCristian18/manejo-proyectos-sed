package com.sed.gestor.proyectos.controller;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sed.gestor.proyectos.dto.MovimientoRequest;
import com.sed.gestor.proyectos.dto.SaldoResponse;
import com.sed.gestor.proyectos.model.Actividad;
import com.sed.gestor.proyectos.model.DetalleMovimiento;
import com.sed.gestor.proyectos.model.MovimientoPresupuestal;
import com.sed.gestor.proyectos.model.Proyecto;
import com.sed.gestor.proyectos.model.enums.FuenteFinanciacion;
import com.sed.gestor.proyectos.repository.ActividadRepository;
import com.sed.gestor.proyectos.repository.ProyectoRepository;
import com.sed.gestor.proyectos.service.PresupuestoService;

@RestController 
@RequestMapping ("/api/presupuesto")
public class PresupuestoController {
    private final PresupuestoService presupuestoService;
    private final ProyectoRepository proyectoRepository;
    private final ActividadRepository actividadRepository;
    
    public PresupuestoController(PresupuestoService presupuestoService, ProyectoRepository proyectoRepository,
            ActividadRepository actividadRepository) {
        this.presupuestoService = presupuestoService;
        this.proyectoRepository = proyectoRepository;
        this.actividadRepository = actividadRepository;
    }
    
    @GetMapping ("/saldo")
    public ResponseEntity<SaldoResponse> consultarSaldo(
        @RequestParam Long actividadId,
        @RequestParam FuenteFinanciacion fuente){
        
        BigDecimal saldo = presupuestoService.obtenerSaldoDisponible(actividadId, fuente);
        return ResponseEntity.ok(new SaldoResponse(actividadId, fuente, saldo));
        }

    @PostMapping ("/movimiento")
    public ResponseEntity<MovimientoPresupuestal> registrarMovimiento(@RequestBody MovimientoRequest request){
        Proyecto proyecto = proyectoRepository.findById(request.getProyectoId())
            .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado con ID: "+ request.getProyectoId()));

        MovimientoPresupuestal movimiento = new MovimientoPresupuestal();
        movimiento.setProyecto(proyecto);
        movimiento.setTipoMovimiento(request.getTipoMovimiento());
        movimiento.setNumeroDecreto(request.getNumeroDecreto());
        movimiento.setFechaDecreto(request.getFechaDecreto());
        movimiento.setObservaciones(request.getObservaciones());

        request.getDetalles().forEach(d -> {
            Actividad actividad = actividadRepository.findById(d.getActividadId())
            .orElseThrow(() -> new IllegalArgumentException("Actividad no encontrada con ID: "+ d.getActividadId()));

            DetalleMovimiento detalle = new DetalleMovimiento();
            detalle.setActividad(actividad);
            detalle.setFuente(d.getFuente());
            detalle.setMonto(d.getMonto());
            detalle.setNumeroCdp(d.getNumeroCdp());
            detalle.setNumeroRp(d.getNumeroRp());

            movimiento.addDetalle(detalle);
        });
        MovimientoPresupuestal guardado = presupuestoService.registrarMovimiento(movimiento);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }
}
