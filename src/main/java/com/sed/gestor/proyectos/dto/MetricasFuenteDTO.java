package com.sed.gestor.proyectos.dto;

import com.sed.gestor.proyectos.model.enums.FuenteFinanciacion;
import java.math.BigDecimal;

public class MetricasFuenteDTO {

    // --- Metadata para Dashboard y Gráficos ---
    private FuenteFinanciacion fuente;
    private String etiqueta;
    private boolean esRecursoPropio;
    private String colorHex;

    // --- Métricas Presupuestales detalladas ---
    private BigDecimal apropiacionInicial = BigDecimal.ZERO;
    private BigDecimal adiciones = BigDecimal.ZERO;
    private BigDecimal reducciones = BigDecimal.ZERO;
    private BigDecimal apropiacionDefinitiva = BigDecimal.ZERO;
    private BigDecimal ejecutado = BigDecimal.ZERO;
    private BigDecimal saldoDisponible = BigDecimal.ZERO;

    // 1. Constructor vacío (requerido por PresupuestoResumenDTO)
    public MetricasFuenteDTO() {}

    // 2. Constructor dinámico (utilizado por el WebController para el Dashboard)
    public MetricasFuenteDTO(FuenteFinanciacion fuente, BigDecimal saldoDisponible) {
        setFuente(fuente);
        this.saldoDisponible = saldoDisponible != null ? saldoDisponible : BigDecimal.ZERO;
    }

    // Getters y Setters de Metadata Visual
    public FuenteFinanciacion getFuente() { return fuente; }
    public void setFuente(FuenteFinanciacion fuente) { 
        this.fuente = fuente;
        if (fuente != null) {
            this.etiqueta = fuente.getEtiqueta();
            this.esRecursoPropio = fuente.isEsRecursoPropio();
            this.colorHex = fuente.getColorGrafico();
        }
    }

    public String getEtiqueta() { return etiqueta; }
    public void setEtiqueta(String etiqueta) { this.etiqueta = etiqueta; }

    public boolean isEsRecursoPropio() { return esRecursoPropio; }
    public void setEsRecursoPropio(boolean esRecursoPropio) { this.esRecursoPropio = esRecursoPropio; }

    public String getColorHex() { return colorHex; }
    public void setColorHex(String colorHex) { this.colorHex = colorHex; }

    // Getters y Setters de Métricas Presupuestales
    public BigDecimal getApropiacionInicial() { return apropiacionInicial; }
    public void setApropiacionInicial(BigDecimal apropiacionInicial) { this.apropiacionInicial = apropiacionInicial; }

    public BigDecimal getAdiciones() { return adiciones; }
    public void setAdiciones(BigDecimal adiciones) { this.adiciones = adiciones; }

    public BigDecimal getReducciones() { return reducciones; }
    public void setReducciones(BigDecimal reducciones) { this.reducciones = reducciones; }

    public BigDecimal getApropiacionDefinitiva() { return apropiacionDefinitiva; }
    public void setApropiacionDefinitiva(BigDecimal apropiacionDefinitiva) { this.apropiacionDefinitiva = apropiacionDefinitiva; }

    public BigDecimal getEjecutado() { return ejecutado; }
    public void setEjecutado(BigDecimal ejecutado) { this.ejecutado = ejecutado; }

    public BigDecimal getSaldoDisponible() { return saldoDisponible; }
    public void setSaldoDisponible(BigDecimal saldoDisponible) { this.saldoDisponible = saldoDisponible; }
}