package com.sed.gestor.proyectos.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity 
@Table (name = "metas_anuales", uniqueConstraints = {
    @UniqueConstraint (columnNames = {"meta_producto_id", "vigencia"})
})
public class MetaAnual {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "meta_producto_id", nullable = false)
    private MetaProducto metaProducto;

    @Column (nullable = false)
    private Integer vigencia; //2024-2027

    @Column (name = "valor_programado", precision = 12, scale = 2, nullable = false)
    private BigDecimal valorProgramado = BigDecimal.ZERO;

    public MetaAnual(){}

    public MetaAnual(MetaProducto metaProducto, Integer vigencia, BigDecimal valorProgramado) {
        this.metaProducto = metaProducto;
        this.vigencia = vigencia;
        this.valorProgramado = valorProgramado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MetaProducto getMetaProducto() {
        return metaProducto;
    }

    public void setMetaProducto(MetaProducto metaProducto) {
        this.metaProducto = metaProducto;
    }

    public Integer getVigencia() {
        return vigencia;
    }

    public void setVigencia(Integer vigencia) {
        this.vigencia = vigencia;
    }

    public BigDecimal getValorProgramado() {
        return valorProgramado;
    }

    public void setValorProgramado(BigDecimal valorProgramado) {
        this.valorProgramado = valorProgramado;
    }
}
