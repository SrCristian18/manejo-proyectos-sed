package com.sed.gestor.proyectos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.sed.gestor.proyectos.model.AvanceActividad;

public class ActividadResumenDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal metaCuatrienio = BigDecimal.ZERO;
    private BigDecimal metaAnualProgramada = BigDecimal.ZERO;
    private BigDecimal puntoPartidaAnual = BigDecimal.ZERO;
    private BigDecimal avanceAnual = BigDecimal.ZERO;
    private BigDecimal acumuladoAnual = BigDecimal.ZERO;
    private BigDecimal acumuladoGeneral = BigDecimal.ZERO;
    private String unidadMedida;
    
    private BigDecimal porcentajeAnual = BigDecimal.ZERO;
    private BigDecimal porcentajeCuatrienio = BigDecimal.ZERO;
    private BigDecimal porcentajeSimpleAnual = BigDecimal.ZERO;

    private LocalDate fechaInicio;
    private LocalDate fechaCierre;

    // Consolidado Dinámico Trimestral (Suma de Meses)
    private BigDecimal trimestre1 = BigDecimal.ZERO;
    private BigDecimal trimestre2 = BigDecimal.ZERO;
    private BigDecimal trimestre3 = BigDecimal.ZERO;
    private BigDecimal trimestre4 = BigDecimal.ZERO;

    private List<AvanceActividad> avances = new ArrayList<>();

    private boolean noProgramada = false;

    public ActividadResumenDTO() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getMetaCuatrienio() { return metaCuatrienio; }
    public void setMetaCuatrienio(BigDecimal metaCuatrienio) { this.metaCuatrienio = metaCuatrienio; }

    public BigDecimal getMetaAnualProgramada() { return metaAnualProgramada; }
    public void setMetaAnualProgramada(BigDecimal metaAnualProgramada) { this.metaAnualProgramada = metaAnualProgramada; }

    public BigDecimal getPuntoPartidaAnual() { return puntoPartidaAnual; }
    public void setPuntoPartidaAnual(BigDecimal puntoPartidaAnual) { this.puntoPartidaAnual = puntoPartidaAnual; }

    public BigDecimal getAvanceAnual() { return avanceAnual; }
    public void setAvanceAnual(BigDecimal avanceAnual) { this.avanceAnual = avanceAnual; }

    public BigDecimal getAcumuladoAnual() { return acumuladoAnual; }
    public void setAcumuladoAnual(BigDecimal acumuladoAnual) { this.acumuladoAnual = acumuladoAnual; }

    public BigDecimal getAcumuladoGeneral() { return acumuladoGeneral; }
    public void setAcumuladoGeneral(BigDecimal acumuladoGeneral) { this.acumuladoGeneral = acumuladoGeneral; }

    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }

    public BigDecimal getPorcentajeAnual() { return porcentajeAnual; }
    public void setPorcentajeAnual(BigDecimal porcentajeAnual) { this.porcentajeAnual = porcentajeAnual; }

    public BigDecimal getPorcentajeCuatrienio() { return porcentajeCuatrienio; }
    public void setPorcentajeCuatrienio(BigDecimal porcentajeCuatrienio) { this.porcentajeCuatrienio = porcentajeCuatrienio; }

    public BigDecimal getPorcentajeSimpleAnual() { return porcentajeSimpleAnual; }
    public void setPorcentajeSimpleAnual(BigDecimal porcentajeSimpleAnual) { this.porcentajeSimpleAnual = porcentajeSimpleAnual; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaCierre() { return fechaCierre; }
    public void setFechaCierre(LocalDate fechaCierre) { this.fechaCierre = fechaCierre; }

    public BigDecimal getTrimestre1() { return trimestre1; }
    public void setTrimestre1(BigDecimal trimestre1) { this.trimestre1 = trimestre1; }

    public BigDecimal getTrimestre2() { return trimestre2; }
    public void setTrimestre2(BigDecimal trimestre2) { this.trimestre2 = trimestre2; }

    public BigDecimal getTrimestre3() { return trimestre3; }
    public void setTrimestre3(BigDecimal trimestre3) { this.trimestre3 = trimestre3; }

    public BigDecimal getTrimestre4() { return trimestre4; }
    public void setTrimestre4(BigDecimal trimestre4) { this.trimestre4 = trimestre4; }

    public List<AvanceActividad> getAvances() { return avances; }
    public void setAvances(List<AvanceActividad> avances) { this.avances = avances; }

    public boolean isNoProgramada() {return noProgramada;}
    public void setNoProgramada(boolean noProgramada) {this.noProgramada = noProgramada;}
}