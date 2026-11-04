package com.sed.gestor.proyectos.model.politicapublica;

import java.util.ArrayList;
import java.util.List;

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
@Table (name = "factor_estrategico")
public class FactorEstrategico {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false, length = 255)
    private String nombre;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "componente_id", nullable = false)
    private Componente componente;

    @OneToMany (mappedBy = "factorEstrategico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ObjetivoEspecifico> objetivosEspecificos = new ArrayList<>();

    public FactorEstrategico(){
    }

    public FactorEstrategico(Long id, String nombre, Componente componente) {
        this.id = id;
        this.nombre = nombre;
        this.componente = componente;
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

    public Componente getComponente() {
        return componente;
    }

    public void setComponente(Componente componente) {
        this.componente = componente;
    }

    public List<ObjetivoEspecifico> getObjetivosEspecificos() {
        return objetivosEspecificos;
    }

    public void setObjetivosEspecificos(List<ObjetivoEspecifico> objetivosEspecificos) {
        this.objetivosEspecificos = objetivosEspecificos;
    }

    public void addObjetivoEspecifico(ObjetivoEspecifico objetivo) {
        objetivosEspecificos.add(objetivo);
        objetivo.setFactorEstrategico(this);
    }

    public void removeObjetivoEspecifico(ObjetivoEspecifico objetivo) {
        objetivosEspecificos.remove(objetivo);
        objetivo.setFactorEstrategico(null);
    }
}
