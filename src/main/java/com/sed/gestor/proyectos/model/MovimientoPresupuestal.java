package com.sed.gestor.proyectos.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sed.gestor.proyectos.model.enums.TipoMovimiento;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "movimiento_presupuestales")
public class MovimientoPresupuestal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proyecto_id", nullable = false)
    @JsonIgnoreProperties ("actividades")
    private Proyecto proyecto;

    @Enumerated(EnumType.STRING)
    @Column (name = "tipo_movimiento",nullable = false)
    private TipoMovimiento tipoMovimiento;

    @Column (name = "numero_decreto")
    private String numeroDecreto;

    @Column (name = "fecha_decreto")
    private LocalDate fechaDecreto;

    @Column (name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column (name = "observaciones", length = 500)
    private String observaciones;

    @OneToMany (mappedBy = "movimiento", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties ("movimiento")
    private List<DetalleMovimiento> detalles = new ArrayList<>();

    public MovimientoPresupuestal() {
    }

    public void addDetalle(DetalleMovimiento detalle){
        detalles.add(detalle);
        detalle.setMovimiento(this);
    }

    public void removeDetalle(DetalleMovimiento detalle){
        detalles.remove(detalle);
        detalle.setMovimiento(null);
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Proyecto getProyecto() {
        return proyecto;
    }
    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }
    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }
    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }
    public String getNumeroDecreto() {
        return numeroDecreto;
    }
    public void setNumeroDecreto(String numeroDecreto) {
        this.numeroDecreto = numeroDecreto;
    }
    public LocalDate getFechaDecreto() {
        return fechaDecreto;
    }
    public void setFechaDecreto(LocalDate fechaDecreto) {
        this.fechaDecreto = fechaDecreto;
    }
    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    public List<DetalleMovimiento> getDetalles(){
        return detalles;
    }
    public void setDetalles(List<DetalleMovimiento> detalles) {
        this.detalles = detalles;
    }
}
