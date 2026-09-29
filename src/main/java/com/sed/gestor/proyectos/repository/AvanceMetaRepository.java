package com.sed.gestor.proyectos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sed.gestor.proyectos.model.AvanceMeta;

public interface AvanceMetaRepository extends JpaRepository<AvanceMeta, Long>{
    List<AvanceMeta> findByMetaProductoIdOrderByVigenciaDesctrimestreDesc(Long metaProductoId);
    Optional<AvanceMeta> findByMetaProductoIdAndVigenciaAndTrimestre(Long metaProductoId, Integer vigencia, Integer trimestre);
}
