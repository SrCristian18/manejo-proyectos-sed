package com.sed.gestor.proyectos.dto;

import com.sed.gestor.proyectos.model.enums.FuenteFinanciacion;
import java.math.BigDecimal;

public class PresupuestoResumenDTO {

    private Long actividadId;
    private String nombreActividad;
    private FuenteFinanciacion fuente;
    private BigDecimal apropiacionInicial;
    private BigDecimal adiciones;
    private BigDecimal reducciones;
    private BigDecimal apropiacionDefinitiva;
    private BigDecimal ejecutadoCompromisos; // Suma de CDP / RP
    private BigDecimal saldoDisponible;
    private BigDecimal porcentajeEjecucion; // (Ejecutado / Definitiva) * 100

    public PresupuestoResumenDTO() {}

    // Getters y Setters
    public Long getActividadId() { return actividadId; }
    public void setActividadId(Long actividadId) { this.actividadId = actividadId; }

    public String getNombreActividad() { return nombreActividad; }
    public void setNombreActividad(String nombreActividad) { this.nombreActividad = nombreActividad; }

    public FuenteFinanciacion getFuente() { return fuente; }
    public void setFuente(FuenteFinanciacion fuente) { this.fuente = fuente; }

    public BigDecimal getApropiacionInicial() { return apropiacionInicial; }
    public void setApropiacionInicial(BigDecimal apropiacionInicial) { this.apropiacionInicial = apropiacionInicial; }

    public BigDecimal getAdiciones() { return adiciones; }
    public void setAdiciones(BigDecimal adiciones) { this.adiciones = adiciones; }

    public BigDecimal getReducciones() { return reducciones; }
    public void setReducciones(BigDecimal reducciones) { this.reducciones = reducciones; }

    public BigDecimal getApropiacionDefinitiva() { return apropiacionDefinitiva; }
    public void setApropiacionDefinitiva(BigDecimal apropiacionDefinitiva) { this.apropiacionDefinitiva = apropiacionDefinitiva; }

    public BigDecimal getEjecutadoCompromisos() { return ejecutadoCompromisos; }
    public void setEjecutadoCompromisos(BigDecimal ejecutadoCompromisos) { this.ejecutadoCompromisos = ejecutadoCompromisos; }

    public BigDecimal getSaldoDisponible() { return saldoDisponible; }
    public void setSaldoDisponible(BigDecimal saldoDisponible) { this.saldoDisponible = saldoDisponible; }

    public BigDecimal getPorcentajeEjecucion() { return porcentajeEjecucion; }
    public void setPorcentajeEjecucion(BigDecimal porcentajeEjecucion) { this.porcentajeEjecucion = porcentajeEjecucion; }
}