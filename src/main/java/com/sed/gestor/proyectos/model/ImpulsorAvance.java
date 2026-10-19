package com.sed.gestor.proyectos.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity 
@Table (name = "impulsores_avance")
public class ImpulsorAvance {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "codigo", nullable = false, length = 20)
    private String codigo;

    @Column (name = "nombre", nullable = false, length = 255)
    private String nombre;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "linea_estrategica_id", nullable = false)
    @JsonIgnoreProperties ("impulsores")
    private LineaEstrategica lineaEstrategica;

    @OneToMany (mappedBy = "impulsorAvance", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties ("impulsorAvance")
    private List<MetaResultado> metasResultado = new ArrayList<>();
    
    public ImpulsorAvance(){

    }

    public ImpulsorAvance(String codigo, String nombre, LineaEstrategica lineaEstrategica) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.lineaEstrategica = lineaEstrategica;
    }

    public void addMetaResultado(MetaResultado meta) {
        metasResultado.add(meta);
        meta.setImpulsorAvance(this);
    }

    public void removeMetaResultado(MetaResultado meta) {
        metasResultado.remove(meta);
        meta.setImpulsorAvance(null);
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

    public LineaEstrategica getLineaEstrategica() {
        return lineaEstrategica;
    }

    public void setLineaEstrategica(LineaEstrategica lineaEstrategica) {
        this.lineaEstrategica = lineaEstrategica;
    }

    public List<MetaResultado> getMetasResultado() {
        return metasResultado;
    }

    public void setMetasResultado(List<MetaResultado> metasResultado) {
        this.metasResultado = metasResultado;
    }

}
