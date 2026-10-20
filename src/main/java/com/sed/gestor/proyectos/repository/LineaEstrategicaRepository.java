package com.sed.gestor.proyectos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sed.gestor.proyectos.model.LineaEstrategica;

@Repository 
public interface LineaEstrategicaRepository extends JpaRepository<LineaEstrategica, Long>{
    List<LineaEstrategica> findByPlanDesarrolloId(Long planDesarrolloId);
}
