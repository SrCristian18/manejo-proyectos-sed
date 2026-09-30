package com.sed.gestor.proyectos.dto;

import java.math.BigDecimal;

public class MetricasFuenteDTO {
    private BigDecimal apropiacionInicial = BigDecimal.ZERO;
    private BigDecimal adiciones = BigDecimal.ZERO;
    private BigDecimal reducciones = BigDecimal.ZERO;
    private BigDecimal apropiacionDefinitiva = BigDecimal.ZERO;
    private BigDecimal ejecutado = BigDecimal.ZERO;
    private BigDecimal saldoDisponible = BigDecimal.ZERO;

    public MetricasFuenteDTO() {}

    // Getters y Setters
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