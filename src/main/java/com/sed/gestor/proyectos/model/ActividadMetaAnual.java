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
@Table (name = "actividad_metas_anuales", uniqueConstraints = {
    @UniqueConstraint (columnNames = {"actividad_id", "vigencia"})
})
public class ActividadMetaAnual {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "actividad_id", nullable = false)
    private Actividad actividad;

    @Column (nullable = false)
    private Integer vigencia;

    @Column (name = "valor_programado", precision = 12, scale = 2, nullable = false)
    private BigDecimal valorProgramado = BigDecimal.ZERO;

    @Column(name = "no_programada", nullable = false)
    private boolean noProgramada = false;

    public ActividadMetaAnual(){}

    public ActividadMetaAnual(Actividad actividad, Integer vigencia, BigDecimal valorProgramado) {
        this.actividad = actividad;
        this.vigencia = vigencia;
        this.valorProgramado = valorProgramado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
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

    //metodo de apoyo
    public boolean isNoProgramada(){
        return noProgramada;
    }

    public void setNoProgramada(boolean noProgramada){
        this.noProgramada = noProgramada;
        if(noProgramada){
            this.valorProgramado = BigDecimal.ZERO;
        }
    }
}
