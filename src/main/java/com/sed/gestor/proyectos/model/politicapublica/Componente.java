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
@Table (name = "componente")
public class Componente {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false, length = 255)
    private String nombre;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "objetivo_general_id", nullable = false)
    private ObjetivoGeneral objetivoGeneral;

    @OneToMany (mappedBy = "componente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FactorEstrategico> factoresEstrategicos = new ArrayList<>();

    public Componente(){

    }

    public Componente(Long id, String nombre, ObjetivoGeneral objetivoGeneral) {
        this.id = id;
        this.nombre = nombre;
        this.objetivoGeneral = objetivoGeneral;
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

    public ObjetivoGeneral getObjetivoGeneral() {
        return objetivoGeneral;
    }

    public void setObjetivoGeneral(ObjetivoGeneral objetivoGeneral) {
        this.objetivoGeneral = objetivoGeneral;
    }

    public List<FactorEstrategico> getFactoresEstrategicos() {
        return factoresEstrategicos;
    }

    public void setFactoresEstrategicos(List<FactorEstrategico> factoresEstrategicos) {
        this.factoresEstrategicos = factoresEstrategicos;
    }

    public void addFactorEstrategico(FactorEstrategico factor) {
        factoresEstrategicos.add(factor);
        factor.setComponente(this);
    }

    public void removeFactorEstrategico(FactorEstrategico factor) {
        factoresEstrategicos.remove(factor);
        factor.setComponente(null);
    }
}
