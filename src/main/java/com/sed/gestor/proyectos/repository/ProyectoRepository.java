package com.sed.gestor.proyectos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sed.gestor.proyectos.model.Proyecto;

@Repository 
public interface ProyectoRepository extends JpaRepository<Proyecto, Long>{
    List<Proyecto> findByActividadId(Long actividadId);
}