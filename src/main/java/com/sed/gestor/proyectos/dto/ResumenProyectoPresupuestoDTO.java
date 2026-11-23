package com.sed.gestor.proyectos.dto;

import java.math.BigDecimal;

public class ResumenProyectoPresupuestoDTO {
    private BigDecimal valorTotalInicial = BigDecimal.ZERO;
    private BigDecimal totalModificado = BigDecimal.ZERO;
    private BigDecimal valorTotalDefinitivo = BigDecimal.ZERO;
    private BigDecimal totalGastado = BigDecimal.ZERO;
    private BigDecimal totalDisponible = BigDecimal.ZERO;
    private BigDecimal porcentajeEjecucion = BigDecimal.ZERO;
    
    public ResumenProyectoPresupuestoDTO() {
    }

    public BigDecimal getValorTotalInicial() {
        return valorTotalInicial;
    }

    public void setValorTotalInicial(BigDecimal valorTotalInicial) {
        this.valorTotalInicial = valorTotalInicial;
    }

    public BigDecimal getTotalModificado() {
        return totalModificado;
    }

    public void setTotalModificado(BigDecimal totalModificado) {
        this.totalModificado = totalModificado;
    }

    public BigDecimal getValorTotalDefinitivo() {
        return valorTotalDefinitivo;
    }

    public void setValorTotalDefinitivo(BigDecimal valorTotalDefinitivo) {
        this.valorTotalDefinitivo = valorTotalDefinitivo;
    }

    public BigDecimal getTotalGastado() {
        return totalGastado;
    }

    public void setTotalGastado(BigDecimal totalGastado) {
        this.totalGastado = totalGastado;
    }

    public BigDecimal getTotalDisponible() {
        return totalDisponible;
    }

    public void setTotalDisponible(BigDecimal totalDisponible) {
        this.totalDisponible = totalDisponible;
    }

    public BigDecimal getPorcentajeEjecucion() {
        return porcentajeEjecucion;
    }

    public void setPorcentajeEjecucion(BigDecimal porcentajeEjecucion) {
        this.porcentajeEjecucion = porcentajeEjecucion;
    }
}
