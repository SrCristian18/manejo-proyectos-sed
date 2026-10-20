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
@Table (name = "metas_resultado")
public class MetaResultado {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column (name = "codigo", nullable = false, length = 20)
    private String codigo;

    @Column (name = "descripcion",nullable = false, length = 500)
    private String descripcion;

    @Column(name = "linea_base")
    private Double lineaBase;

    @Column(name = "meta_cuatrienio")
    private Double metaCuatrienio;
    
    @Column(name = "unidad_medida", length = 50)
    private String unidadMedida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "impulsor_avance_id", nullable = false)
    @JsonIgnoreProperties ("metasResultado")
    private ImpulsorAvance impulsorAvance;

    @OneToMany(mappedBy = "metaResultado", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties ("metaResultado")
    private List<Programa> programas = new ArrayList<>();
    
    public MetaResultado(){

    }

    public MetaResultado(String codigo, String descripcion, Double lineaBase, Double metaCuatrienio,
            String unidadMedida, ImpulsorAvance impulsorAvance) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.lineaBase = lineaBase;
        this.metaCuatrienio = metaCuatrienio;
        this.unidadMedida = unidadMedida;
        this.impulsorAvance = impulsorAvance;
    }

    public void addPrograma(Programa programa) {
        programas.add(programa);
        programa.setMetaResultado(this);
    }

    public void removePrograma(Programa programa) {
        programas.remove(programa);
        programa.setMetaResultado(null);
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getLineaBase() {
        return lineaBase;
    }

    public void setLineaBase(Double lineaBase) {
        this.lineaBase = lineaBase;
    }

    public Double getMetaCuatrienio() {
        return metaCuatrienio;
    }

    public void setMetaCuatrienio(Double metaCuatrienio) {
        this.metaCuatrienio = metaCuatrienio;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public ImpulsorAvance getImpulsorAvance() {
        return impulsorAvance;
    }

    public void setImpulsorAvance(ImpulsorAvance impulsorAvance) {
        this.impulsorAvance = impulsorAvance;
    }

    public List<Programa> getProgramas() {
        return programas;
    }

    public void setProgramas(List<Programa> programas) {
        this.programas = programas;
    }
    
}