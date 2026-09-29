package com.sed.gestor.proyectos.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sed.gestor.proyectos.model.MetaProducto;

@Repository 
public interface MetaProductoRepository extends JpaRepository<MetaProducto, Long>{
    List<MetaProducto> findByActividadProyectoId(Long proyectoId);

    @Query ("SELECT COALESCE(SUM(a.valorLogrado), 0) FROM AvanceMeta a " +
           "WHERE a.metaProducto.id = :metaId AND a.vigencia = :vigencia")
    BigDecimal calcularAcumuladoFisicoMeta(@Param("metaId") Long metaId, @Param("vigencia") Integer vigencia);
}