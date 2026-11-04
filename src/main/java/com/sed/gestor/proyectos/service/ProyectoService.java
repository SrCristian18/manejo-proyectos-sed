package com.sed.gestor.proyectos.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sed.gestor.proyectos.dto.ProyectoDTO;
import com.sed.gestor.proyectos.model.Actividad;
import com.sed.gestor.proyectos.model.MetaProducto;
import com.sed.gestor.proyectos.model.Proyecto;
import com.sed.gestor.proyectos.model.enums.FuenteFinanciacion;
import com.sed.gestor.proyectos.repository.ProyectoRepository;

import jakarta.persistence.EntityNotFoundException;

import com.sed.gestor.proyectos.repository.ActividadRepository;
import com.sed.gestor.proyectos.repository.MetaProductoRepository;

@Service 
public class ProyectoService {
    private final ProyectoRepository proyectoRepository;
    private final MetaProductoRepository metaProductoRepository;
    private final ActividadRepository actividadRepository;

    @Autowired 
    public ProyectoService(ProyectoRepository proyectoRepository, MetaProductoRepository metaProductoRepository, ActividadRepository actividadRepository){
        this.proyectoRepository = proyectoRepository;
        this.metaProductoRepository = metaProductoRepository;
        this.actividadRepository = actividadRepository;
    }

    //Lecturas generales

    @Transactional (readOnly = true)
    public List<Proyecto> obtenerTodos(){
        return proyectoRepository.findAll();
    }

    @Transactional (readOnly = true)
    public List<Proyecto> obtenerPorVigencia(Integer vigencia){
        if (vigencia == null){
            return proyectoRepository.findAll();
        }
        return proyectoRepository.findAll().stream()
            .filter(p -> p.getVigencia().equals(vigencia))
            .toList();
    }

    @Transactional (readOnly = true)
    public Proyecto obtenerPorId(Long id){
        return proyectoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado con ID: "+ id));
    }

    //Proyectos
    @Transactional
    public Proyecto guardarOActualizar(ProyectoDTO dto) {
        Proyecto proyecto;
        if (dto.getId() != null) {
            proyecto = obtenerPorId(dto.getId());
        } else {
            proyecto = new Proyecto();
        }

        proyecto.setCodigoBpin(dto.getCodigoBpin());
        proyecto.setNombre(dto.getNombre());
        proyecto.setVigencia(dto.getVigencia());

        if (dto.getMetaProductoId() != null) {
            MetaProducto metaProducto = metaProductoRepository.findById(dto.getMetaProductoId())
                    .orElseThrow(() -> new EntityNotFoundException("Meta Producto no encontrada con ID: " + dto.getMetaProductoId()));
            proyecto.setMetaProducto(metaProducto);
        } else {
            proyecto.setMetaProducto(null);
        }

        return proyectoRepository.save(proyecto);
    }

        @Transactional 
    public Proyecto guardarDesdeDTO(ProyectoDTO dto){
        Proyecto proyecto;
        if(dto.getId() != null){
            proyecto = obtenerPorId(dto.getId());
        }else{
            proyecto = new Proyecto();
        }
        proyecto.setCodigoBpin(dto.getCodigoBpin());
        proyecto.setNombre(dto.getNombre());
        proyecto.setVigencia(dto.getVigencia());

        if(dto.getMetaProductoId() != null){
            MetaProducto metaProducto = metaProductoRepository.findById(dto.getMetaProductoId())
                .orElseThrow(() -> new IllegalArgumentException("Meta producto no encontrada con ID: "+ dto.getMetaProductoId()));
            proyecto.setMetaProducto(metaProducto);
        }else{
            proyecto.setMetaProducto(null);
        }
        return proyectoRepository.save(proyecto);
    }
   
    @Transactional
    public Proyecto guardar(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }

    @Transactional
    public void eliminar(Long id) {
        if(!proyectoRepository.existsById(id)){
            throw new EntityNotFoundException("Proyecto no encontrado con ID: "+ id);
        }
        proyectoRepository.deleteById(id);
    }

    //Actividades
    @Transactional
    public Actividad agregarActividad(Long proyectoId, String nombre, BigDecimal presupuestoInicial, FuenteFinanciacion fuente, String descripcion) {
        Proyecto proyecto = obtenerPorId(proyectoId);
        Actividad actividad = new Actividad(nombre, presupuestoInicial, fuente, descripcion, proyecto);
        proyecto.addActividad(actividad);
        return actividadRepository.save(actividad);
    }

    @Transactional
    public Actividad editarActividad(Long actividadId, String nombre, BigDecimal presupuestoInicial, FuenteFinanciacion fuente, String descripcion) {
        Actividad actividad = actividadRepository.findById(actividadId)
                .orElseThrow(() -> new EntityNotFoundException("Actividad no encontrada con ID: " + actividadId));
        actividad.setNombre(nombre);
        actividad.setPresupuestoInicial(presupuestoInicial);
        actividad.setFuente(fuente);
        actividad.setDescripcion(descripcion);
        return actividadRepository.save(actividad);
    }

    @Transactional
    public void eliminarActividad(Long actividadId) {
        if (!actividadRepository.existsById(actividadId)) {
            throw new EntityNotFoundException("Actividad no encontrada con ID: " + actividadId);
        }
        actividadRepository.deleteById(actividadId);
    }
}
