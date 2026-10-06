package com.sed.gestor.proyectos.model;

import java.math.BigDecimal;
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
@Table (name = "proyectos")
public class Proyecto {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "codigo_bpin", nullable = false, unique = true, length = 50)
    private String codigoBpin;

    @Column (name = "nombre", nullable = false, length = 255)
    private String nombre;

    @Column (name = "programa", length = 100)
    private String programa;

    @Column (name = "politica_publica", length = 255)
    private String politicaPublica;

    @Column (name = "eje_estrategico", length = 255)
    private String ejeEstrategico;

    @Column (name = "presupuesto_inicial_global", precision = 15, scale = 2)
    private BigDecimal presupuestoInicialGlobal = BigDecimal.ZERO;

    //nuevo
    @OneToMany (mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties ("proyecto")
    private List<Actividad> actividades = new ArrayList<>();
    
    public Proyecto()
    {
        
    }
    
    public Proyecto(String codigoBpin, String nombre, String programa, String politicaPublica) {
        this.codigoBpin = codigoBpin;
        this.nombre = nombre;
        this.programa = programa;
        this.politicaPublica = politicaPublica;
    }

    public void addActividad(Actividad actividad){
        actividades.add(actividad);
        actividad.setProyecto(this);
    }

    public void removeActividad(Actividad actividad){
        actividades.remove(actividad);
        actividad.setProyecto(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoBpin() {
        return codigoBpin;
    }

    public void setCodigoBpin(String codigoBpin) {
        this.codigoBpin = codigoBpin;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public String getPoliticaPublica() {
        return politicaPublica;
    }

    public void setPoliticaPublica(String politicaPublica) {
        this.politicaPublica = politicaPublica;
    }

    public String getEjeEstrategico() {
        return ejeEstrategico;
    }

    public void setEjeEstrategico(String ejeEstrategico) {
        this.ejeEstrategico = ejeEstrategico;
    }

    public List<Actividad> getActividades() {
        return actividades;
    }

    public void setActividades(List<Actividad> actividades) {
        this.actividades = actividades;
    }

    public BigDecimal getPresupuestoInicialGlobal() {
        return presupuestoInicialGlobal;
    }

    public void setPresupuestoInicialGlobal(BigDecimal presupuestoInicialGlobal) {
        this.presupuestoInicialGlobal = presupuestoInicialGlobal;
    }
}