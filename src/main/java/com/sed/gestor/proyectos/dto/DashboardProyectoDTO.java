package com.sed.gestor.proyectos.dto;

import java.math.BigDecimal;
import java.util.List;

public class DashboardProyectoDTO {
    private String codigoBpin;
    private String nombreProyecto;
    private BigDecimal presupuestoApropiadoTotal;
    private BigDecimal presupuestoEjecutadoTotal;
    private BigDecimal porcentajeEjecucionFinanciera;
    private BigDecimal porcentajeAvanceFisicoPromedio;
    private List<MetaAvanceDTO> resumenMetas;

    public DashboardProyectoDTO(){

    }

    public String getCodigoBpin() {
        return codigoBpin;
    }

    public void setCodigoBpin(String codigoBpin) {
        this.codigoBpin = codigoBpin;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public BigDecimal getPresupuestoApropiadoTotal() {
        return presupuestoApropiadoTotal;
    }

    public void setPresupuestoApropiadoTotal(BigDecimal presupuestoApropiadoTotal) {
        this.presupuestoApropiadoTotal = presupuestoApropiadoTotal;
    }

    public BigDecimal getPresupuestoEjecutadoTotal() {
        return presupuestoEjecutadoTotal;
    }

    public void setPresupuestoEjecutadoTotal(BigDecimal presupuestoEjecutadoTotal) {
        this.presupuestoEjecutadoTotal = presupuestoEjecutadoTotal;
    }

    public BigDecimal getPorcentajeEjecucionFinanciera() {
        return porcentajeEjecucionFinanciera;
    }

    public void setPorcentajeEjecucionFinanciera(BigDecimal porcentajeEjecucionFinanciera) {
        this.porcentajeEjecucionFinanciera = porcentajeEjecucionFinanciera;
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
