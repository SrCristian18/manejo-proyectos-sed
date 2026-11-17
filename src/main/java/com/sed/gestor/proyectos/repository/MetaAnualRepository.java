package com.sed.gestor.proyectos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sed.gestor.proyectos.model.MetaAnual;

@Repository 
public interface MetaAnualRepository extends JpaRepository<MetaAnual, Long>{
    List<MetaAnual> findByMetaProductoId(Long metaProductoId);
    Optional<MetaAnual> findByMetaProductoIdAndVigencia(Long metaProductoId, Integer vigencia);    
}
