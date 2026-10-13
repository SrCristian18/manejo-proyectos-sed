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
@Table (name = "proyectos")
public class Proyecto {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "codigo_bpin", nullable = false, unique = true, length = 50)
    private String codigoBpin;

    @Column (name = "nombre", nullable = false, length = 255)
    private String nombre;

    @Column (name = "meta_resultado", length = 500) //publica
    private String metaResultado;

    @Column (name = "meta_producto", length = 500) //distrital
    private String metaProducto;

    @Column (name = "vigencia", nullable = false)
    private Integer vigencia;

    @OneToMany (mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties ("proyecto")
    private List<Actividad> actividades = new ArrayList<>();
    
    public Proyecto()
    {
        
    }

    public Proyecto(String codigoBpin, String nombre, String metaResultado, String metaProducto, Integer vigencia) {
        this.codigoBpin = codigoBpin;
        this.nombre = nombre;
        this.metaResultado = metaResultado;
        this.metaProducto = metaProducto;
        this.vigencia = vigencia;
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

    public String getMetaResultado() {
        return metaResultado;
    }

    public void setMetaResultado(String metaResultado) {
        this.metaResultado = metaResultado;
    }

    public String getMetaProducto() {
        return metaProducto;
    }

    public void setMetaProducto(String metaProducto) {
        this.metaProducto = metaProducto;
    }

    public Integer getVigencia() {
        return vigencia;
    }

    public void setVigencia(Integer vigencia) {
        this.vigencia = vigencia;
    }

    public List<Actividad> getActividades() {
        return actividades;
    }

    public void setActividades(List<Actividad> actividades) {
        this.actividades = actividades;
    }
}