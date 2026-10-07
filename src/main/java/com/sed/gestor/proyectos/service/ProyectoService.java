package com.sed.gestor.proyectos.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sed.gestor.proyectos.dto.ProyectoDTO;
import com.sed.gestor.proyectos.model.Actividad;
import com.sed.gestor.proyectos.model.DetalleMovimiento;
import com.sed.gestor.proyectos.model.MovimientoPresupuestal;
import com.sed.gestor.proyectos.model.Proyecto;
import com.sed.gestor.proyectos.model.enums.FuenteFinanciacion;
import com.sed.gestor.proyectos.model.enums.TipoMovimiento;
import com.sed.gestor.proyectos.repository.ActividadRepository;
import com.sed.gestor.proyectos.repository.ProyectoRepository;

@Service 
public class ProyectoService {
    private final ProyectoRepository proyectoRepository;
    private final ActividadRepository actividadRepository;
    private final PresupuestoService presupuestoService;

    public ProyectoService(ProyectoRepository proyectoRepository, ActividadRepository actividadRepository, PresupuestoService presupuestoService){
        this.proyectoRepository = proyectoRepository;
        this.actividadRepository = actividadRepository;
        this.presupuestoService = presupuestoService;
    }

    @Transactional (readOnly = true)
    public List<Proyecto> obtenerTodos(){
        return proyectoRepository.findAll();
    }

    @Transactional 
    public Proyecto obtenerPorId(Long id){
        return proyectoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado con ID: " + id));
    }

    @Transactional 
    public Proyecto guardarOActualizar(ProyectoDTO dto)
    {
        Proyecto proyecto = (dto.getId() != null) ? obtenerPorId(dto.getId()) : new Proyecto();

        proyecto.setCodigoBpin(dto.getCodigoBpin());
        proyecto.setNombre(dto.getNombre());
        proyecto.setPrograma(dto.getPrograma());
        proyecto.setPoliticaPublica(dto.getPoliticaPublica());
        proyecto.setEjeEstrategico(dto.getEjeEstrategico());
        proyecto.setPresupuestoInicialGlobal(dto.getPresupuestoInicialGlobal());

        return proyectoRepository.save(proyecto);
    }

    @Transactional
    public Actividad agregarActividad(Long proyectoId, String nombreActividad, String descripcion, BigDecimal montoInicial, FuenteFinanciacion fuente) {
        Proyecto proyecto = obtenerPorId(proyectoId);

        Actividad actividad = new Actividad();
        actividad.setNombre(nombreActividad);
        actividad.setDescripcion(descripcion);
        proyecto.addActividad(actividad);

        Actividad guardada = actividadRepository.save(actividad);

        // Si se especificó un presupuesto inicial para la actividad, creamos automáticamente el Movimiento de APROPIACIÓN INICIAL
        if(montoInicial != null && montoInicial.compareTo(BigDecimal.ZERO)>0){
            MovimientoPresupuestal movimientoInicial = new MovimientoPresupuestal();
            movimientoInicial.setProyecto(proyecto);;
            movimientoInicial.setTipoMovimiento(TipoMovimiento.APROPIACION_INICIAL);
            movimientoInicial.setNumeroDecreto("Apropiación Inicial Planificada");

            DetalleMovimiento detalle = new DetalleMovimiento();
            detalle.setActividad(guardada);
            detalle.setFuente(fuente != null ? fuente : FuenteFinanciacion.PROPIOS);
            detalle.setMonto(montoInicial);

            movimientoInicial.addDetalle(detalle);
            presupuestoService.registrarMovimiento(movimientoInicial);

        }
        return guardada;

    }

    @Transactional
    public void eliminarProyecto(Long id) {
        proyectoRepository.deleteById(id);
    }

    @Transactional
    public Actividad editarActividad(Long actividadId, String nuevoNombre, String nuevaDescripcion) {
    Actividad actividad = actividadRepository.findById(actividadId)
            .orElseThrow(() -> new IllegalArgumentException("Actividad no encontrada con ID: " + actividadId));
    
    actividad.setNombre(nuevoNombre);
    actividad.setDescripcion(nuevaDescripcion);
    return actividadRepository.save(actividad);
    }

    @Transactional
    public void eliminarActividad(Long actividadId) {
        actividadRepository.deleteById(actividadId);
    }

}
