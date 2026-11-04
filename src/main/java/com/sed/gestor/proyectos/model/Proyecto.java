package com.sed.gestor.proyectos.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sed.gestor.proyectos.model.politicapublica.Producto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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

    @Column (name = "vigencia", nullable = false)
    private Integer vigencia;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "meta_producto_id", nullable = true)
    @JsonIgnoreProperties ("proyectos")
    private MetaProducto metaProducto;

    @OneToMany (mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties ("proyecto")
    private List<Actividad> actividades = new ArrayList<>();

    @ManyToMany (mappedBy = "proyectos", fetch = FetchType.LAZY)
    private Set<Producto> productos = new HashSet<>();
    
    public Proyecto(){
    }

    public Proyecto(String codigoBpin, String nombre, Integer vigencia, MetaProducto metaProducto) {
        this.codigoBpin = codigoBpin;
        this.nombre = nombre;
        this.vigencia = vigencia;
        this.metaProducto = metaProducto;
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

    public Integer getVigencia() {
        return vigencia;
    }

    public void setVigencia(Integer vigencia) {
        this.vigencia = vigencia;
    }

    public MetaProducto getMetaProducto() {
        return metaProducto;
    }

    public void setMetaProducto(MetaProducto metaProducto) {
        this.metaProducto = metaProducto;
    }

    public List<Actividad> getActividades() {
        return actividades;
    }

    public void setActividades(List<Actividad> actividades) {
        this.actividades = actividades;
    }

    public Set<Producto> getProductos() {
        return productos;
    }

    public void setProductos(Set<Producto> productos) {
        this.productos = productos;
    }
    
}