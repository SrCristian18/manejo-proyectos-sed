package com.sed.gestor.proyectos.dto;

import com.sed.gestor.proyectos.model.enums.FuenteFinanciacion;
import java.math.BigDecimal;

public class DetalleMovimientoRequest {
    private Long actividadId;
    private FuenteFinanciacion fuente;
    private BigDecimal monto;
    private String numeroCdp;
    private String numeroRp;

    public DetalleMovimientoRequest() {}

    // Getters y Setters
    public Long getActividadId() { return actividadId; }
    public void setActividadId(Long actividadId) { this.actividadId = actividadId; }

    public FuenteFinanciacion getFuente() { return fuente; }
    public void setFuente(FuenteFinanciacion fuente) { this.fuente = fuente; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public String getNumeroCdp() { return numeroCdp; }
    public void setNumeroCdp(String numeroCdp) { this.numeroCdp = numeroCdp; }

    public String getNumeroRp() { return numeroRp; }
    public void setNumeroRp(String numeroRp) { this.numeroRp = numeroRp; }
}