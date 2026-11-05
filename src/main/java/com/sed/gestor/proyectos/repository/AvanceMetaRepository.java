package com.sed.gestor.proyectos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sed.gestor.proyectos.model.AvanceMeta;

@Repository 
public interface AvanceMetaRepository extends JpaRepository<AvanceMeta, Long>{
    List<AvanceMeta> findByMetaProductoIdOrderByVigenciaDescTrimestreDesc(Long metaProductoId);
    Optional<AvanceMeta> findByMetaProductoIdAndVigenciaAndMes(Long metaProductoId, Integer vigencia, Integer mes);
}