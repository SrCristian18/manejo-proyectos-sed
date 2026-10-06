package com.sed.gestor.proyectos.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sed.gestor.proyectos.dto.ProyectoDTO;
import com.sed.gestor.proyectos.model.Actividad;
import com.sed.gestor.proyectos.model.Proyecto;
import com.sed.gestor.proyectos.repository.ActividadRepository;
import com.sed.gestor.proyectos.repository.ProyectoRepository;

@Service 
public class ProyectoService {
    private final ProyectoRepository proyectoRepository;
    private final ActividadRepository actividadRepository;

    public ProyectoService(ProyectoRepository proyectoRepository, ActividadRepository actividadRepository){
        this.proyectoRepository = proyectoRepository;
        this.actividadRepository = actividadRepository;
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
        Proyecto proyecto;
        if(dto.getId() != null){
            proyecto = obtenerPorId(dto.getId());
        }else{
            proyecto = new Proyecto();
        }

        proyecto.setCodigoBpin(dto.getCodigoBpin());
        proyecto.setNombre(dto.getNombre());
        proyecto.setPrograma(dto.getPrograma());
        proyecto.setPoliticaPublica(dto.getPoliticaPublica());
        proyecto.setEjeEstrategico(dto.getEjeEstrategico());

        return proyectoRepository.save(proyecto);
    }

    @Transactional
    public Actividad agregarActividad(Long proyectoId, String nombreActividad, String descripcion) {
        Proyecto proyecto = obtenerPorId(proyectoId);
        Actividad actividad = new Actividad();
        actividad.setNombre(nombreActividad);
        actividad.setDescripcion(descripcion);
        proyecto.addActividad(actividad);
        
        return actividadRepository.save(actividad);
    }

    @Transactional
    public void eliminarProyecto(Long id) {
        proyectoRepository.deleteById(id);
    }

}
