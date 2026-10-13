package com.sed.gestor.proyectos.dto;

import java.math.BigDecimal;

public class MetaAvanceDTO {
    private String descripcionMeta;
    private String unidadMedida;
    private BigDecimal programadoVigencia;
    private BigDecimal logradoVigencia;
    private BigDecimal porcentajeAvanceSimple;

    public MetaAvanceDTO(){

    }

    public MetaAvanceDTO(String descripcionMeta, String unidadMedida, BigDecimal programadoVigencia,
            BigDecimal logradoVigencia, BigDecimal porcentajeAvanceSimple) {
        this.descripcionMeta = descripcionMeta;
        this.unidadMedida = unidadMedida;
        this.programadoVigencia = programadoVigencia;
        this.logradoVigencia = logradoVigencia;
        this.porcentajeAvanceSimple = porcentajeAvanceSimple;
    }

    public String getDescripcionMeta() {
        return descripcionMeta;
    }

    public void setDescripcionMeta(String descripcionMeta) {
        this.descripcionMeta = descripcionMeta;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public BigDecimal getProgramadoVigencia() {
        return programadoVigencia;
    }

    public void setProgramadoVigencia(BigDecimal programadoVigencia) {
        this.programadoVigencia = programadoVigencia;
    }

    public BigDecimal getLogradoVigencia() {
        return logradoVigencia;
    }

    public void setLogradoVigencia(BigDecimal logradoVigencia) {
        this.logradoVigencia = logradoVigencia;
    }

    public BigDecimal getPorcentajeAvanceSimple() {
        return porcentajeAvanceSimple;
    }

    public void setPorcentajeAvanceSimple(BigDecimal porcentajeAvanceSimple) {
        this.porcentajeAvanceSimple = porcentajeAvanceSimple;
    }

}
