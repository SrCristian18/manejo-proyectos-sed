package com.sed.gestor.proyectos.dto;

import java.math.BigDecimal;

public class ActividadAvanceDTO {

    private Long actividadId;
    private String codigo;
    private String descripcion;
    private BigDecimal avanceFisico;
    private String observaciones;

    public ActividadAvanceDTO() {
    }

    public ActividadAvanceDTO(Long actividadId, String codigo, String descripcion, BigDecimal avanceFisico, String observaciones) {
        this.actividadId = actividadId;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.avanceFisico = avanceFisico;
        this.observaciones = observaciones;
    }

    public Long getActividadId() { return actividadId; }
    public void setActividadId(Long actividadId) { this.actividadId = actividadId; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getAvanceFisico() { return avanceFisico; }
    public void setAvanceFisico(BigDecimal avanceFisico) { this.avanceFisico = avanceFisico; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}