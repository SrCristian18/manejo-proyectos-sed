package com.sed.gestor.proyectos.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sed.gestor.proyectos.model.enums.FuenteFinanciacion;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity 
@Table (name = "actividades")
public class Actividad {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "nombre", nullable = false, length = 500)
    private String nombre;

    @Column (name = "presupuesto_inicial", precision = 15, scale = 2)
    private BigDecimal presupuestoInicial = BigDecimal.ZERO;

    @Enumerated (EnumType.STRING)
    @Column (name = "fuente", length = 50)
    private FuenteFinanciacion fuente;

    @Column (name = "descripcion", length = 1000)
    private String descripcion;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "proyecto_id", nullable = false)
    @JsonIgnoreProperties ("actividades")
    private Proyecto proyecto;

    @OneToMany (mappedBy = "actividad", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties ("actividad")
    private List<RubroGasto> rubros = new ArrayList<>();

    public Actividad(){
    }

    //apoyo
    public void addRubro(RubroGasto rubro){
        rubros.add(rubro);
        rubro.setActividad(this);
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

    public BigDecimal getPresupuestoInicial() {
        return presupuestoInicial;
    }

    public void setPresupuestoInicial(BigDecimal presupuestoInicial) {
        this.presupuestoInicial = presupuestoInicial;
    }

    public FuenteFinanciacion getFuente() {
        return fuente;
    }

    public void setFuente(FuenteFinanciacion fuente) {
        this.fuente = fuente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public List<RubroGasto> getRubros() {
        return rubros;
    }

    public void setRubros(List<RubroGasto> rubros) {
        this.rubros = rubros;
    }

}
