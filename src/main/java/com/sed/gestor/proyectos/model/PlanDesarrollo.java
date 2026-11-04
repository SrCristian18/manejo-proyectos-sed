package com.sed.gestor.proyectos.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity 
@Table (name = "planes_desarrollo")
public class PlanDesarrollo {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;    

    @Column (name = "nombre", nullable = false, length = 200)
    private String nombre;

    @Column(name = "ano_inicio", nullable = false)
    private Integer anoInicio;

    @Column(name = "ano_fin", nullable = false)
    private Integer anoFin;

    @Column(name = "descripcion", length = 1000)
    private String descripcion;

    @OneToMany (mappedBy = "planDesarrollo", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties ("planDesarrollo")
    private List<LineaEstrategica> lineasEstrategicas = new ArrayList<>();

    public PlanDesarrollo(){

    }

    public PlanDesarrollo(String nombre, Integer anoInicio, Integer anoFin, String descripcion) {
        this.nombre = nombre;
        this.anoInicio = anoInicio;
        this.anoFin = anoFin;
        this.descripcion = descripcion;
    }

    public void addLineaEstrategica(LineaEstrategica linea) {
        lineasEstrategicas.add(linea);
        linea.setPlanDesarrollo(this);
    }

    public void removeLineaEstrategica(LineaEstrategica linea) {
        lineasEstrategicas.remove(linea);
        linea.setPlanDesarrollo(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getAnoInicio() {
        return anoInicio;
    }

    public void setAnoInicio(Integer anoInicio) {
        this.anoInicio = anoInicio;
    }

    public Integer getAnoFin() {
        return anoFin;
    }

    public void setAnoFin(Integer anoFin) {
        this.anoFin = anoFin;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<LineaEstrategica> getLineasEstrategicas() {
        return lineasEstrategicas;
    }

    public void setLineasEstrategicas(List<LineaEstrategica> lineasEstrategicas) {
        this.lineasEstrategicas = lineasEstrategicas;
    }
}
