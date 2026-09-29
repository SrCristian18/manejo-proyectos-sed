package com.sed.gestor.proyectos.dto;

import com.sed.gestor.proyectos.model.enums.TipoMovimiento;
import java.time.LocalDate;
import java.util.List;

public class MovimientoRequest {
    private Long proyectoId;
    private TipoMovimiento tipoMovimiento;
    private String numeroDecreto;
    private LocalDate fechaDecreto;
    private String observaciones;
    private List<DetalleMovimientoRequest> detalles;

    public MovimientoRequest() {}

    // Getters y Setters
    public Long getProyectoId() { return proyectoId; }
    public void setProyectoId(Long proyectoId) { this.proyectoId = proyectoId; }

    public TipoMovimiento getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }

    public String getNumeroDecreto() { return numeroDecreto; }
    public void setNumeroDecreto(String numeroDecreto) { this.numeroDecreto = numeroDecreto; }

    public LocalDate getFechaDecreto() { return fechaDecreto; }
    public void setFechaDecreto(LocalDate fechaDecreto) { this.fechaDecreto = fechaDecreto; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public List<DetalleMovimientoRequest> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleMovimientoRequest> detalles) { this.detalles = detalles; }
}