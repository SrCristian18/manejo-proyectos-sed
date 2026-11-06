package com.sed.gestor.proyectos.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.sed.gestor.proyectos.model.AvanceActividad;

public class ActividadResumenDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal metaCuatrienio;
    private String unidadMedida;
    private BigDecimal avanceAcumulado;
    private BigDecimal porcentajeAvance;
    private List<AvanceActividad> avances = new ArrayList<>();

    public ActividadResumenDTO() {
    }

    public ActividadResumenDTO(Long id, String nombre, String descripcion, BigDecimal metaCuatrienio,
                               String unidadMedida, BigDecimal avanceAcumulado, BigDecimal porcentajeAvance) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.metaCuatrienio = metaCuatrienio;
        this.unidadMedida = unidadMedida;
        this.avanceAcumulado = avanceAcumulado;
        this.porcentajeAvance = porcentajeAvance;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getMetaCuatrienio() { return metaCuatrienio; }
    public void setMetaCuatrienio(BigDecimal metaCuatrienio) { this.metaCuatrienio = metaCuatrienio; }

    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }

    public BigDecimal getAvanceAcumulado() { return avanceAcumulado; }
    public void setAvanceAcumulado(BigDecimal avanceAcumulado) { this.avanceAcumulado = avanceAcumulado; }

    public BigDecimal getPorcentajeAvance() { return porcentajeAvance; }
    public void setPorcentajeAvance(BigDecimal porcentajeAvance) { this.porcentajeAvance = porcentajeAvance; }

    public List<AvanceActividad> getAvances() {return avances;}
    public void setAvances(List<AvanceActividad> avances) {this.avances = avances;}
}