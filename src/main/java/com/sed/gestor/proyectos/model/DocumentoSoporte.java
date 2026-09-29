package com.sed.gestor.proyectos.model;

import java.time.LocalDateTime;

public class DocumentoSoporte {
    private Long id;
    private MovimientoPresupuestal movimiento;
    private String nombreArchivo;
    private String rutaArchivo;
    private LocalDateTime fechaCarga;

    public DocumentoSoporte(){
    }

    public DocumentoSoporte(Long id, MovimientoPresupuestal movimiento, String nombreArchivo, String rutaArchivo,
            LocalDateTime fechaCarga) {
        this.id = id;
        this.movimiento = movimiento;
        this.nombreArchivo = nombreArchivo;
        this.rutaArchivo = rutaArchivo;
        this.fechaCarga = fechaCarga;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MovimientoPresupuestal getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(MovimientoPresupuestal movimiento) {
        this.movimiento = movimiento;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public LocalDateTime getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(LocalDateTime fechaCarga) {
        this.fechaCarga = fechaCarga;
    }
}
