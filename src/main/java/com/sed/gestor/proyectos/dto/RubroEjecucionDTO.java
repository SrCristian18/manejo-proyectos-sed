package com.sed.gestor.proyectos.dto;

import java.math.BigDecimal;

import com.sed.gestor.proyectos.model.enums.FuenteFinanciacion;

public class RubroEjecucionDTO {
    private Long id;
    private String concepto;
    private FuenteFinanciacion fuente;
    private String etiquetaFuente;
    private BigDecimal montoAsignado = BigDecimal.ZERO;
    private BigDecimal montoEjecutado = BigDecimal.ZERO;
    private BigDecimal saldoDisponible = BigDecimal.ZERO;
    private BigDecimal porcentajeEjecucion = BigDecimal.ZERO;

    public RubroEjecucionDTO(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public FuenteFinanciacion getFuente() {
        return fuente;
    }

    public void setFuente(FuenteFinanciacion fuente) {
        this.fuente = fuente;
        if(fuente != null){
            this.etiquetaFuente = fuente.getEtiqueta();
        }
    }

    public String getEtiquetaFuente() {
        return etiquetaFuente;
    }

    public void setEtiquetaFuente(String etiquetaFuente) {
        this.etiquetaFuente = etiquetaFuente;
    }

    public BigDecimal getMontoAsignado() {
        return montoAsignado;
    }

    public void setMontoAsignado(BigDecimal montoAsignado) {
        this.montoAsignado = montoAsignado;
    }

    public BigDecimal getMontoEjecutado() {
        return montoEjecutado;
    }

    public void setMontoEjecutado(BigDecimal montoEjecutado) {
        this.montoEjecutado = montoEjecutado;
    }

    public BigDecimal getSaldoDisponible() {
        return saldoDisponible;
    }

    public void setSaldoDisponible(BigDecimal saldoDisponible) {
        this.saldoDisponible = saldoDisponible;
    }

    public BigDecimal getPorcentajeEjecucion() {
        return porcentajeEjecucion;
    }

    public void setPorcentajeEjecucion(BigDecimal porcentajeEjecucion) {
        this.porcentajeEjecucion = porcentajeEjecucion;
    }
}
