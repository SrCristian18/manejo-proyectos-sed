package com.sed.gestor.proyectos.model.politicapublica;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity 
@Table (name = "politica_publica")
public class PoliticaPublica {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false, length = 255)
    private String nombre;

    @Column (columnDefinition = "TEXT")
    private String descripcion;

    @Column (name = "vigencia_inicio", nullable = false)
    private Integer vigenciaInicio;
    
    @Column (name = "vigencia_fin", nullable = false)
    private Integer vigenciaFin;

    @OneToMany (mappedBy = "politicaPublica", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ObjetivoGeneral> objetivosGenerales = new ArrayList<>();

    public PoliticaPublica() {
    }

    public PoliticaPublica(Long id, String nombre, String descripcion, Integer vigenciaInicio, Integer vigenciaFin) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.vigenciaInicio = vigenciaInicio;
        this.vigenciaFin = vigenciaFin;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getVigenciaInicio() {
        return vigenciaInicio;
    }

    public void setVigenciaInicio(Integer vigenciaInicio) {
        this.vigenciaInicio = vigenciaInicio;
    }

    public Integer getVigenciaFin() {
        return vigenciaFin;
    }

    public void setVigenciaFin(Integer vigenciaFin) {
        this.vigenciaFin = vigenciaFin;
    }

    public List<ObjetivoGeneral> getObjetivosGenerales() {
        return objetivosGenerales;
    }

    public void setObjetivosGenerales(List<ObjetivoGeneral> objetivosGenerales) {
        this.objetivosGenerales = objetivosGenerales;
    }

    public void addObjetivoGeneral(ObjetivoGeneral objetivo) {
        objetivosGenerales.add(objetivo);
        objetivo.setPoliticaPublica(this);
    }

    public void removeObjetivoGeneral(ObjetivoGeneral objetivo) {
        objetivosGenerales.remove(objetivo);
        objetivo.setPoliticaPublica(null);
    }

}
