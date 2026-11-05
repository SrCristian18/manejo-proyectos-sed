package com.sed.gestor.proyectos.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReporteTrimestralProyectoFormDTO {

    private Long proyectoId;
    private Long metaProductoId;
    private Integer vigencia;
    private Integer trimestre;
    private BigDecimal avanceLogradoMeta;
    private String observacionesMeta;

    private List<ActividadAvanceDTO> actividades = new ArrayList<>();

    public ReporteTrimestralProyectoFormDTO() {
    }

    public Long getProyectoId() { return proyectoId; }
    public void setProyectoId(Long proyectoId) { this.proyectoId = proyectoId; }

    public Long getMetaProductoId() { return metaProductoId; }
    public void setMetaProductoId(Long metaProductoId) { this.metaProductoId = metaProductoId; }

    public Integer getVigencia() { return vigencia; }
    public void setVigencia(Integer vigencia) { this.vigencia = vigencia; }

    public Integer getTrimestre() { return trimestre; }
    public void setTrimestre(Integer trimestre) { this.trimestre = trimestre; }

    public BigDecimal getAvanceLogradoMeta() { return avanceLogradoMeta; }
    public void setAvanceLogradoMeta(BigDecimal avanceLogradoMeta) { this.avanceLogradoMeta = avanceLogradoMeta; }

    public String getObservacionesMeta() { return observacionesMeta; }
    public void setObservacionesMeta(String observacionesMeta) { this.observacionesMeta = observacionesMeta; }

    public List<ActividadAvanceDTO> getActividades() { return actividades; }
    public void setActividades(List<ActividadAvanceDTO> actividades) { this.actividades = actividades; }
}