package com.sed.gestor.proyectos.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DashboardProyectoDTO {

    private BigDecimal porcentajeAvanceFisicoPromedio = BigDecimal.ZERO;
    private List<MetaResumenDTO> resumenMetas = new ArrayList<>();

    public DashboardProyectoDTO() {
    }

    public DashboardProyectoDTO(BigDecimal porcentajeAvanceFisicoPromedio, List<MetaResumenDTO> resumenMetas) {
        this.porcentajeAvanceFisicoPromedio = porcentajeAvanceFisicoPromedio;
        this.resumenMetas = resumenMetas;
    }

    public BigDecimal getPorcentajeAvanceFisicoPromedio() {
        return porcentajeAvanceFisicoPromedio;
    }

    public void setPorcentajeAvanceFisicoPromedio(BigDecimal porcentajeAvanceFisicoPromedio) {
        this.porcentajeAvanceFisicoPromedio = porcentajeAvanceFisicoPromedio;
    }

    public List<MetaResumenDTO> getResumenMetas() {
        return resumenMetas;
    }

    public void setResumenMetas(List<MetaResumenDTO> resumenMetas) {
        this.resumenMetas = resumenMetas;
    }

    // Clase interna para el detalle de avance de cada meta en el gráfico/tabla
    public static class MetaResumenDTO {
        private String descripcionMeta;
        private BigDecimal porcentajeAvanceSimple = BigDecimal.ZERO;

        public MetaResumenDTO() {
        }

        public MetaResumenDTO(String descripcionMeta, BigDecimal porcentajeAvanceSimple) {
            this.descripcionMeta = descripcionMeta;
            this.porcentajeAvanceSimple = porcentajeAvanceSimple;
        }

        public String getDescripcionMeta() {
            return descripcionMeta;
        }

        public void setDescripcionMeta(String descripcionMeta) {
            this.descripcionMeta = descripcionMeta;
        }

        public BigDecimal getPorcentajeAvanceSimple() {
            return porcentajeAvanceSimple;
        }

        public void setPorcentajeAvanceSimple(BigDecimal porcentajeAvanceSimple) {
            this.porcentajeAvanceSimple = porcentajeAvanceSimple;
        }
    }
}