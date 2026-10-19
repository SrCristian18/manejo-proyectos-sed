package com.sed.gestor.proyectos.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DashboardProyectoDTO {
    private String nombreProyecto;
    private String codigoBpin;
    private BigDecimal porcentajeAvanceFisicoPromedio;
    private List<MetaAvanceDTO> resumenMetas = new ArrayList<>();

    public DashboardProyectoDTO(){}

    public DashboardProyectoDTO(String nombreProyecto, String codigoBpin, BigDecimal porcentajeAvanceFisicoPromedio,
            List<MetaAvanceDTO> resumenMetas) {
        this.nombreProyecto = nombreProyecto;
        this.codigoBpin = codigoBpin;
        this.porcentajeAvanceFisicoPromedio = porcentajeAvanceFisicoPromedio != null ? porcentajeAvanceFisicoPromedio : BigDecimal.ZERO;
        this.resumenMetas = resumenMetas != null ? resumenMetas : new ArrayList<>();
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public String getCodigoBpin() {
        return codigoBpin;
    }

    public void setCodigoBpin(String codigoBpin) {
        this.codigoBpin = codigoBpin;
    }

    public BigDecimal getPorcentajeAvanceFisicoPromedio() {
        return porcentajeAvanceFisicoPromedio;
    }

    public void setPorcentajeAvanceFisicoPromedio(BigDecimal porcentajeAvanceFisicoPromedio) {
        this.porcentajeAvanceFisicoPromedio = porcentajeAvanceFisicoPromedio;
    }

    public List<MetaAvanceDTO> getResumenMetas() {
        return resumenMetas;
    }

    public void setResumenMetas(List<MetaAvanceDTO> resumenMetas) {
        this.resumenMetas = resumenMetas;
    }
}
