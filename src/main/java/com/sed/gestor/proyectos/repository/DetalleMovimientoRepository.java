package com.sed.gestor.proyectos.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sed.gestor.proyectos.model.DetalleMovimiento;
import com.sed.gestor.proyectos.model.enums.FuenteFinanciacion;

@Repository 
public interface DetalleMovimientoRepository extends JpaRepository<DetalleMovimiento,Long>{
    @Query("SELECT COALESCE(SUM(d.monto), 0) FROM DetalleMovimiento d " +
           "WHERE d.actividad.id = :actividadId AND d.fuente = :fuente")
    BigDecimal calcularSaldoAcumuladoByActividadYFuente(
        @Param("actividadId") Long actividadId,
        @Param("fuente") FuenteFinanciacion fuente
    );

    @Query("SELECT COALESCE(SUM(d.monto), 0) FROM DetalleMovimiento d " +
           "WHERE d.actividad.id = :actividadId")
    BigDecimal calcularSaldoAcumuladoTotalByActividad(@Param("actividadId") Long actividadId);
    List<DetalleMovimiento> findByActividadId(Long actividadId);
}
