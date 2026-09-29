package com.sed.gestor.proyectos.model;

import java.math.BigDecimal;
import java.util.Optional;

import com.sed.gestor.proyectos.model.enums.FuenteFinanciacion;

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
import jakarta.persistence.Table;

@Entity 
@Table (name = "detalles_movimiento")
public class DetalleMovimiento {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "movimiento_id", nullable = false)
    private MovimientoPresupuestal movimiento;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "actividad_id", nullable = false)
    private Actividad actividad;

    @Enumerated (EnumType.STRING)
    @Column(name = "fuente", nullable = false)
    private FuenteFinanciacion fuente;

    @Column (name = "monto", nullable = false, precision = 19, scale = 2)
    private BigDecimal monto;

    @Column (name = "numero_cdp")
    private String numeroCdp;

    @Column (name = "numero_rp")
    private String numeroRp;

    public DetalleMovimiento(){

    }

    public DetalleMovimiento(Long id, MovimientoPresupuestal movimiento, Actividad actividad, FuenteFinanciacion fuente,
            BigDecimal monto, String numeroCdp, String numeroRp) {
        this.id = id;
        this.movimiento = movimiento;
        this.actividad = actividad;
        this.fuente = fuente;
        this.monto = monto;
        this.numeroCdp = numeroCdp;
        this.numeroRp = numeroRp;
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

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    public FuenteFinanciacion getFuente() {
        return fuente;
    }

    public void setFuente(FuenteFinanciacion fuente) {
        this.fuente = fuente;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Optional<String> getNumeroCdp() {
        return Optional.ofNullable(numeroCdp);
    }

    public void setNumeroCdp(String numeroCdp) {
        this.numeroCdp = numeroCdp;
    }

    public Optional<String> getNumeroRp() {
        return Optional.ofNullable(numeroRp);
    }

    public void setNumeroRp(String numeroRp) {
        this.numeroRp = numeroRp;
    }

}
