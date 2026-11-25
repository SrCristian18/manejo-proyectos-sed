package com.sed.gestor.proyectos.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sed.gestor.proyectos.model.enums.FuenteFinanciacion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Enumerated (EnumType.STRING)
    @Column (name = "fuente", nullable = false, length = 50)
    private FuenteFinanciacion fuente = FuenteFinanciacion.LIBRE_DESTINACION;

    @Column (name = "monto_asignado", precision = 15, scale = 2, nullable = false)
    private BigDecimal montoAsignado = BigDecimal.ZERO;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "actividad_id", nullable = false)
    @JsonIgnoreProperties ({"proyecto", "actividades", "rubros", "avances", "metasAnuales"})
    private Actividad actividad;

    public RubroGasto(){
    }

    public RubroGasto(String concepto, FuenteFinanciacion fuente, BigDecimal montoAsignado, Actividad actividad) {
        this.concepto = concepto;
        this.fuente = fuente != null ? fuente : FuenteFinanciacion.LIBRE_DESTINACION;
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

    public FuenteFinanciacion getFuente() {
        return fuente;
    }

    public void setFuente(FuenteFinanciacion fuente) {
        this.fuente = fuente;
    }

}
