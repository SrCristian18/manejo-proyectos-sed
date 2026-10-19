package com.sed.gestor.proyectos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sed.gestor.proyectos.model.RubroGasto;

public interface RubroGastoRepository extends JpaRepository<RubroGasto, Long>{
    List<RubroGasto> findByActividadId(Long actividadId);
}
