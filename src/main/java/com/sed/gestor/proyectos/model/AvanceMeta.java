package com.sed.gestor.proyectos.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity 
@Table (name = "avances_meta")
public class AvanceMeta {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column (name = "trimestre", nullable = false)
    private Integer trimestre;

    @Column (name = "vigencia", nullable = false)
    private Integer vigencia;

    @Column (name = "valor_logrado", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorLogrado;

    @Column (name = "observaciones", length = 500)
    private String observaciones;

    @Column (name = "fecha_reporte", nullable = false)
    private LocalDateTime fechaReporte;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "meta_producto_id", nullable = false)
    @JsonIgnoreProperties ("avances")
    private MetaProducto metaProducto;

    public AvanceMeta(){
    }

    public AvanceMeta(Integer trimestre, Integer vigencia, BigDecimal valorLogrado, String observaciones,
            LocalDateTime fechaReporte, MetaProducto metaProducto) {
        this.trimestre = trimestre;
        this.vigencia = vigencia;
        this.valorLogrado = valorLogrado;
        this.observaciones = observaciones;
        this.fechaReporte = fechaReporte;
        this.metaProducto = metaProducto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTrimestre() {
        return trimestre;
    }

    public void setTrimestre(Integer trimestre) {
        this.trimestre = trimestre;
    }

    public Integer getVigencia() {
        return vigencia;
    }

    public void setVigencia(Integer vigencia) {
        this.vigencia = vigencia;
    }

    public BigDecimal getValorLogrado() {
        return valorLogrado;
    }

    public void setValorLogrado(BigDecimal valorLogrado) {
        this.valorLogrado = valorLogrado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFechaReporte() {
        return fechaReporte;
    }

    public void setFechaReporte(LocalDateTime fechaReporte) {
        this.fechaReporte = fechaReporte;
    }

    public MetaProducto getMetaProducto() {
        return metaProducto;
    }

    public void setMetaProducto(MetaProducto metaProducto) {
        this.metaProducto = metaProducto;
    }
}
