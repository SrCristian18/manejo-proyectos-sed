package com.sed.gestor.proyectos.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sed.gestor.proyectos.dto.ProyectoDTO;
import com.sed.gestor.proyectos.model.Actividad;
import com.sed.gestor.proyectos.model.Proyecto;
import com.sed.gestor.proyectos.model.enums.FuenteFinanciacion;
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

    /*
    @Transactional (readOnly = true)
    public List<Proyecto> obtenerTodos(){
        return proyectoRepository.findAll();
    }
    */
    
    @Transactional (readOnly = true)
    public List<Proyecto> obtenerPorVigencia(Integer vigencia){
        return proyectoRepository.findByVigencia(vigencia);
    }

    @Transactional (readOnly = true)
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
        proyecto.setMetaResultado(dto.getMetaResultado());
        proyecto.setMetaProducto(dto.getMetaProducto());
        proyecto.setVigencia(dto.getVigencia());

        return proyectoRepository.save(proyecto);
    }

    @Transactional
    public Actividad agregarActividad(Long proyectoId, String nombre, BigDecimal presupuestoInicial, FuenteFinanciacion fuente, String descripcion) {
        Proyecto proyecto = obtenerPorId(proyectoId);

        Actividad actividad = new Actividad();
        actividad.setNombre(nombre);
        actividad.setPresupuestoInicial(presupuestoInicial);
        actividad.setFuente(fuente);
        actividad.setDescripcion(descripcion);
        proyecto.addActividad(actividad);

        return actividadRepository.save(actividad);
    }

    @Transactional
    public Actividad editarActividad(Long actividadId, String nombre, BigDecimal presupuestoInicial, FuenteFinanciacion fuente, String descripcion){
        Actividad actividad = actividadRepository.findById(actividadId)
            .orElseThrow(() -> new IllegalArgumentException("Actividad no encontrada con ID: " + actividadId));

        actividad.setNombre(nombre);
        actividad.setPresupuestoInicial(presupuestoInicial);
        actividad.setFuente(fuente);
        actividad.setDescripcion(descripcion);

        return actividadRepository.save(actividad);
    }

    @Transactional
    public void eliminarActividad(Long actividadId) {
        actividadRepository.deleteById(actividadId);
    }

    @Transactional
    public void eliminarProyecto(Long id) {
        proyectoRepository.deleteById(id);
    }
}
