package com.sed.gestor.proyectos.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "avance_actividad", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"actividad_id", "vigencia", "trimestre"})
})
public class AvanceActividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actividad_id", nullable = false)
    private Actividad actividad;

    @Column(nullable = false)
    private Integer vigencia;

    @Column (name = "mes")
    private Integer mes;

    @Column(nullable = false)
    private Integer trimestre;

    @Column(name = "avance_fisico", precision = 8, scale = 2)
    private BigDecimal avanceFisico;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    public AvanceActividad() {
    }

    public AvanceActividad(Long id, Actividad actividad, Integer vigencia, Integer mes, BigDecimal avanceFisico,
            String observaciones) {
        this.id = id;
        this.actividad = actividad;
        this.vigencia = vigencia;
        this.mes = mes;
        this.avanceFisico = avanceFisico;
        this.observaciones = observaciones;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    public Integer getVigencia() {
        return vigencia;
    }

    public void setVigencia(Integer vigencia) {
        this.vigencia = vigencia;
    }

    public Integer getTrimestre() {
        return trimestre;
    }

    public void setTrimestre(Integer trimestre) {
        this.trimestre = trimestre;
    }

    public BigDecimal getAvanceFisico() {
        return avanceFisico;
    }

    public void setAvanceFisico(BigDecimal avanceFisico) {
        this.avanceFisico = avanceFisico;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvanceActividad that = (AvanceActividad) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
        if (mes != null){
            this.trimestre = ((mes - 1)/3)+1;
        }
    }
}