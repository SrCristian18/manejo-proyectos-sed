package com.sed.gestor.proyectos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sed.gestor.proyectos.model.MetaResultado;

@Repository 
public interface MetaResultadoRepository extends JpaRepository<MetaResultado, Long>{
    List<MetaResultado> findByImpulsorAvanceId(Long impulsorId);
}