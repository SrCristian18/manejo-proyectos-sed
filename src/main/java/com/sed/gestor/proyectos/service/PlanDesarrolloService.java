package com.sed.gestor.proyectos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sed.gestor.proyectos.model.LineaEstrategica;
import com.sed.gestor.proyectos.model.PlanDesarrollo;
import com.sed.gestor.proyectos.repository.LineaEstrategicaRepository;
import com.sed.gestor.proyectos.repository.PlanDesarrolloRepository;

@Service 
public class PlanDesarrolloService {
    private final PlanDesarrolloRepository planDesarrolloRepository;
    private final LineaEstrategicaRepository lineaEstrategicaRepository;
  
    @Autowired 
    public PlanDesarrolloService(PlanDesarrolloRepository planDesarrolloRepository,
            LineaEstrategicaRepository lineaEstrategicaRepository) {
        this.planDesarrolloRepository = planDesarrolloRepository;
        this.lineaEstrategicaRepository = lineaEstrategicaRepository;
    }

    @Transactional (readOnly = true)
    public List<PlanDesarrollo> obtenerTodosLosPlanes(){
        return planDesarrolloRepository.findAll();
    }

    @Transactional (readOnly = true)
    public List<LineaEstrategica> obtenerLineaEstrategicas(){
        return lineaEstrategicaRepository.findAll();
    }

    public PlanDesarrollo obtenerPlanPorId(Long id){
        return planDesarrolloRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Plan de desarrollo no encontrado con ID: "+ id));
    }
}
