package com.sed.gestor.proyectos.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity 
@Table (name = "documentos_soporte")
public class DocumentoSoporte {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "nombre_archivo", nullable = false, length = 255)
    private String nombreArchivo;

    @Column (name = "ruta_archivo", nullable = false, length = 500)
    private String rutaArchivo;

    @Column (name = "tipo_contenido", length = 100)
    private String tipoContenido;

    @Column (name = "fecha_carga", nullable = false)
    private LocalDateTime fechaCarga;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "movimiento_id", nullable = false)
    @JsonIgnoreProperties ("documentos")
    private MovimientoPresupuestal movimiento;

    public DocumentoSoporte(){
    }

    public DocumentoSoporte(String nombreArchivo, String rutaArchivo, String tipoContenido, LocalDateTime fechaCarga,
            MovimientoPresupuestal movimiento) {
        this.nombreArchivo = nombreArchivo;
        this.rutaArchivo = rutaArchivo;
        this.tipoContenido = tipoContenido;
        this.fechaCarga = fechaCarga;
        this.movimiento = movimiento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTipoContenido() {
        return tipoContenido;
    }

    public void setTipoContenido(String tipoContenido) {
        this.tipoContenido = tipoContenido;
    }

    public LocalDateTime getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(LocalDateTime fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    public MovimientoPresupuestal getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(MovimientoPresupuestal movimiento) {
        this.movimiento = movimiento;
    }
}
