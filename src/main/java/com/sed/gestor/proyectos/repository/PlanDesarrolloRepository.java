package com.sed.gestor.proyectos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sed.gestor.proyectos.model.PlanDesarrollo;

@Repository 
public interface PlanDesarrolloRepository extends JpaRepository<PlanDesarrollo, Long>{
    
}
