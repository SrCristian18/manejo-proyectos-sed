package com.sed.gestor.proyectos.repository;

import com.sed.gestor.proyectos.model.AvanceActividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AvanceActividadRepository extends JpaRepository<AvanceActividad, Long> {

    List<AvanceActividad> findByActividadProyectoIdAndVigenciaAndTrimestre(Long proyectoId, Integer vigencia, Integer trimestre);

    Optional<AvanceActividad> findByActividadIdAndVigenciaAndTrimestre(Long actividadId, Integer vigencia, Integer trimestre);
}