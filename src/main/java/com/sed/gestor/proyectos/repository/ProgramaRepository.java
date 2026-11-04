package com.sed.gestor.proyectos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sed.gestor.proyectos.model.Programa;

public interface ProgramaRepository extends JpaRepository<Programa, Long>{
    List<Programa> findByMetaResultadoId(Long metaResultadoId);
}