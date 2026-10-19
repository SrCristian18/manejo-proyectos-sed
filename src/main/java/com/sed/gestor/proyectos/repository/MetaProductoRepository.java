package com.sed.gestor.proyectos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sed.gestor.proyectos.model.MetaProducto;

@Repository 
public interface MetaProductoRepository extends JpaRepository<MetaProducto, Long>{
    List<MetaProducto> findByProyectoId(Long proyectoId);
    List<MetaProducto> findByProyectoIdAndTipoMeta(Long proyectoId, String tipoMeta);
}