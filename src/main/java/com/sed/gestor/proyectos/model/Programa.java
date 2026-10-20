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
@Table (name = "programas")
public class Programa {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "codigo", nullable = false, length = 20)
    private String codigo;

    @Column (name = "nombre", nullable = false, length = 255)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meta_resultado_id", nullable = false)
    @JsonIgnoreProperties ("programas")
    private MetaResultado metaResultado;

    @OneToMany(mappedBy = "programa", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties ("programa")
    private List<MetaProducto> metasProducto = new ArrayList<>();

    public Programa(){

    }

    public Programa(String codigo, String nombre, MetaResultado metaResultado) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.metaResultado = metaResultado;
    }

    public void addMetaProducto(MetaProducto metaProducto) {
        metasProducto.add(metaProducto);
        metaProducto.setPrograma(this);
    }

    public void removeMetaProducto(MetaProducto metaProducto) {
        metasProducto.remove(metaProducto);
        metaProducto.setPrograma(null);
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

    public MetaResultado getMetaResultado() {
        return metaResultado;
    }

    public void setMetaResultado(MetaResultado metaResultado) {
        this.metaResultado = metaResultado;
    }

    public List<MetaProducto> getMetasProducto() {
        return metasProducto;
    }

    public void setMetasProducto(List<MetaProducto> metasProducto) {
        this.metasProducto = metasProducto;
    }
}
