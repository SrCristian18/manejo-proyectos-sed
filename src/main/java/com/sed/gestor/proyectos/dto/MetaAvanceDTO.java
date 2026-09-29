package com.sed.gestor.proyectos.dto;

import java.math.BigDecimal;

public class MetaAvanceDTO {
    private String descripcionMeta;
    private String unidadMedida;
    private BigDecimal metaProgramadaVigencia;
    private BigDecimal acumuladoFisicoLogrado;
    private BigDecimal porcentajeAvanceSimple;

    public MetaAvanceDTO(){

    }

    public MetaAvanceDTO(String descripcionMeta, String unidadMedida, BigDecimal metaProgramadaVigencia,
            BigDecimal acumuladoFisicoLogrado, BigDecimal porcentajeAvanceSimple) {
        this.descripcionMeta = descripcionMeta;
        this.unidadMedida = unidadMedida;
        this.metaProgramadaVigencia = metaProgramadaVigencia;
        this.acumuladoFisicoLogrado = acumuladoFisicoLogrado;
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

    public BigDecimal getMetaProgramadaVigencia() {
        return metaProgramadaVigencia;
    }

    public void setMetaProgramadaVigencia(BigDecimal metaProgramadaVigencia) {
        this.metaProgramadaVigencia = metaProgramadaVigencia;
    }

    public BigDecimal getAcumuladoFisicoLogrado() {
        return acumuladoFisicoLogrado;
    }

    public void setAcumuladoFisicoLogrado(BigDecimal acumuladoFisicoLogrado) {
        this.acumuladoFisicoLogrado = acumuladoFisicoLogrado;
    }

    public BigDecimal getPorcentajeAvanceSimple() {
        return porcentajeAvanceSimple;
    }

    public void setPorcentajeAvanceSimple(BigDecimal porcentajeAvanceSimple) {
        this.porcentajeAvanceSimple = porcentajeAvanceSimple;
    }
}
