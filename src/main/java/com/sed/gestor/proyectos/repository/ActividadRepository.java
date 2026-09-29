package com.sed.gestor.proyectos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sed.gestor.proyectos.model.Actividad;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long>{
    List<Actividad> findByProyectoId(Long proyectoId);    
}
