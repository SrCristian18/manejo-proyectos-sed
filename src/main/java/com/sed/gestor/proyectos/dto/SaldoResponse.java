package com.sed.gestor.proyectos.dto;

import com.sed.gestor.proyectos.model.enums.FuenteFinanciacion;
import java.math.BigDecimal;

public class SaldoResponse {
    private Long actividadId;
    private FuenteFinanciacion fuente;
    private BigDecimal saldoDisponible;

    public SaldoResponse(Long actividadId, FuenteFinanciacion fuente, BigDecimal saldoDisponible) {
        this.actividadId = actividadId;
        this.fuente = fuente;
        this.saldoDisponible = saldoDisponible;
    }

    // Getters
    public Long getActividadId() { return actividadId; }
    public FuenteFinanciacion getFuente() { return fuente; }
    public BigDecimal getSaldoDisponible() { return saldoDisponible; }
}