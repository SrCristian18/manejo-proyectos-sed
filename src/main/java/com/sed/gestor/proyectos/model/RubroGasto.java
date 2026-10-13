package com.sed.gestor.proyectos.model;

import java.math.BigDecimal;

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
@Table (name = "rubros_gasto")
public class RubroGasto {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "concepto", nullable = false, length = 255)
    private String concepto;

    @Column (name = "monto_asignado", precision = 15, scale = 2, nullable = false)
    private BigDecimal montoAsignado = BigDecimal.ZERO;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "actividad_id", nullable = false)
    @JsonIgnoreProperties ("rubros")
    private Actividad actividad;

    public RubroGasto(){
    }

    public RubroGasto(String concepto, BigDecimal montoAsignado, Actividad actividad) {
        this.concepto = concepto;
        this.montoAsignado = montoAsignado;
        this.actividad = actividad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public BigDecimal getMontoAsignado() {
        return montoAsignado;
    }

    public void setMontoAsignado(BigDecimal montoAsignado) {
        this.montoAsignado = montoAsignado;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

}
