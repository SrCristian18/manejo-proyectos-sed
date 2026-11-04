package com.sed.gestor.proyectos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sed.gestor.proyectos.model.MovimientoPresupuestal;

@Repository 
public interface MovimientoPresupuestalRepository extends JpaRepository<MovimientoPresupuestal, Long>{
    List<MovimientoPresupuestal> findByProyectoIdOrderByFechaRegistroDesc(Long proyectoId);
    List<MovimientoPresupuestal> findByProyectoId(Long proyectoId);
    List<MovimientoPresupuestal> findByDetallesActividadId(Long actividadId);
}
