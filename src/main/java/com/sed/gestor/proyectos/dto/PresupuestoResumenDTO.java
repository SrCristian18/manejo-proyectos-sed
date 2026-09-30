package com.sed.gestor.proyectos.dto;

import com.sed.gestor.proyectos.model.enums.FuenteFinanciacion;
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

public class PresupuestoResumenDTO {

    private Long actividadId;
    private String nombreActividad;
    
    // Mapa dinámico por cada fuente existente en el Enum
    private Map<FuenteFinanciacion, MetricasFuenteDTO> valoresPorFuente = new EnumMap<>(FuenteFinanciacion.class);

    // Totales consolidados (Suma de todas las fuentes)
    private BigDecimal disponibleTotal = BigDecimal.ZERO;
    private BigDecimal porcentajeEjecucion = BigDecimal.ZERO;

    public PresupuestoResumenDTO() {}

    // Helper para obtener o inicializar el DTO de una fuente
    public MetricasFuenteDTO getMetrica(FuenteFinanciacion fuente) {
        if(this.valoresPorFuente == null){
            this.valoresPorFuente = new EnumMap<>(FuenteFinanciacion.class);
        }
        return this.valoresPorFuente.computeIfAbsent(fuente, k -> new MetricasFuenteDTO());
    }

    // Getters y Setters
    public Long getActividadId() { return actividadId; }
    public void setActividadId(Long actividadId) { this.actividadId = actividadId; }

    public String getNombreActividad() { return nombreActividad; }
    public void setNombreActividad(String nombreActividad) { this.nombreActividad = nombreActividad; }

    public Map<FuenteFinanciacion, MetricasFuenteDTO> getValoresPorFuente() { return valoresPorFuente; }
    public void setValoresPorFuente(Map<FuenteFinanciacion, MetricasFuenteDTO> valoresPorFuente) { this.valoresPorFuente = valoresPorFuente; }

    public BigDecimal getDisponibleTotal() { return disponibleTotal; }
    public void setDisponibleTotal(BigDecimal disponibleTotal) { this.disponibleTotal = disponibleTotal; }

    public BigDecimal getPorcentajeEjecucion() { return porcentajeEjecucion; }
    public void setPorcentajeEjecucion(BigDecimal porcentajeEjecucion) { this.porcentajeEjecucion = porcentajeEjecucion; }
}