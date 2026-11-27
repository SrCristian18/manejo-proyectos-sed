package com.sed.gestor.proyectos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sed.gestor.proyectos.model.Proyecto;

@Repository 
public interface ProyectoRepository extends JpaRepository<Proyecto, Long>{
    Optional<Proyecto> findByCodigoBpin(String codigoBpin);
    List<Proyecto> findByMetaProductoId(Long MetaProductoId);
    List<Proyecto> findByVigencia(Integer vigencia);

    @Query("SELECT p FROM Proyecto p WHERE :vigencia >= p.vigencia " +
           "AND :vigencia <= COALESCE(p.vigenciaFin, (p.vigencia - MOD(p.vigencia, 4) + 3))")
    List<Proyecto> findActivosEnVigencia(@Param("vigencia") Integer vigencia);
}