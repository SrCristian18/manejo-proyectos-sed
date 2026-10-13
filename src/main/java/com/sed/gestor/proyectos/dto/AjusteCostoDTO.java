package com.sed.gestor.proyectos.dto;

import java.math.BigDecimal;

import com.sed.gestor.proyectos.model.enums.FuenteFinanciacion;

public class AjusteCostoDTO {
    private Long actividadId;
    private FuenteFinanciacion fuente;
    private BigDecimal constoInicial = BigDecimal.ZERO;
    private BigDecimal constoAjustado = BigDecimal.ZERO;

    public AjusteCostoDTO(){

    }

    public BigDecimal getCambioEnCosto(){
        if(constoAjustado == null) return BigDecimal.ZERO;
        if(constoInicial == null) return constoAjustado;
        return constoAjustado.subtract(constoInicial);
    }

    public Long getActividadId() {
        return actividadId;
    }

    public void setActividadId(Long actividadId) {
        this.actividadId = actividadId;
    }

    public FuenteFinanciacion getFuente() {
        return fuente;
    }

    public void setFuente(FuenteFinanciacion fuente) {
        this.fuente = fuente;
    }

    public BigDecimal getConstoInicial() {
        return constoInicial;
    }

    public void setConstoInicial(BigDecimal constoInicial) {
        this.constoInicial = constoInicial;
    }

    public BigDecimal getConstoAjustado() {
        return constoAjustado;
    }

    public void setConstoAjustado(BigDecimal constoAjustado) {
        this.constoAjustado = constoAjustado;
    }
}
