package com.sed.gestor.proyectos.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity 
@Table (name = "lineas_estrategicas")
public class LineaEstrategica {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "codigo", nullable = false, length = 20) 
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;

    @Column(name = "ods_relacionado", length = 255)
    private String odsRelacionado; //objetivo desarrollo sostenible
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_desarrollo_id", nullable = false)
    @JsonIgnoreProperties ("lineasEstrategicas")
    private PlanDesarrollo planDesarrollo;

    @OneToMany (mappedBy = "lineaEstrategica", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties ("lineasEstrategicas")
    private List<ImpulsorAvance> impulsores = new ArrayList<>();

    public LineaEstrategica(){
    }

    public LineaEstrategica(String codigo, String nombre, String odsRelacionado, PlanDesarrollo planDesarrollo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.odsRelacionado = odsRelacionado;
        this.planDesarrollo = planDesarrollo;
    }

    public void addImpulsor(ImpulsorAvance impulsor) {
        impulsores.add(impulsor);
        impulsor.setLineaEstrategica(this);
    }

    public void removeImpulsor(ImpulsorAvance impulsor) {
        impulsores.remove(impulsor);
        impulsor.setLineaEstrategica(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getOdsRelacionado() {
        return odsRelacionado;
    }

    public void setOdsRelacionado(String odsRelacionado) {
        this.odsRelacionado = odsRelacionado;
    }

    public PlanDesarrollo getPlanDesarrollo() {
        return planDesarrollo;
    }

    public void setPlanDesarrollo(PlanDesarrollo planDesarrollo) {
        this.planDesarrollo = planDesarrollo;
    }

    public List<ImpulsorAvance> getImpulsores() {
        return impulsores;
    }

    public void setImpulsores(List<ImpulsorAvance> impulsores) {
        this.impulsores = impulsores;
    }
}
