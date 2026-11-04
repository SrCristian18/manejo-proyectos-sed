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
@Table (name = "objetivo_general")
public class ObjetivoGeneral {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "politica_id", nullable = false)
    private PoliticaPublica politicaPublica;

    @OneToMany (mappedBy = "objetivoGeneral", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Componente> componentes = new ArrayList<>();

    public ObjetivoGeneral() {
    }

    public ObjetivoGeneral(Long id, String descripcion, PoliticaPublica politicaPublica) {
        this.id = id;
        this.descripcion = descripcion;
        this.politicaPublica = politicaPublica;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public PoliticaPublica getPoliticaPublica() {
        return politicaPublica;
    }

    public void setPoliticaPublica(PoliticaPublica politicaPublica) {
        this.politicaPublica = politicaPublica;
    }

    public List<Componente> getComponentes() {
        return componentes;
    }

    public void setComponentes(List<Componente> componentes) {
        this.componentes = componentes;
    }

    public void addComponente(Componente componente) {
        componentes.add(componente);
        componente.setObjetivoGeneral(this);
    }

    public void removeComponente(Componente componente) {
        componentes.remove(componente);
        componente.setObjetivoGeneral(null);
    }
}
