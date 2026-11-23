package com.sed.gestor.proyectos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sed.gestor.proyectos.model.ActividadMetaAnual;

@Repository 
public interface ActividadMetaAnualRepository extends JpaRepository<ActividadMetaAnual, Long>{
    Optional<ActividadMetaAnual> findByActividadIdAndVigencia(Long actividadId, Integer vigencia);
}
