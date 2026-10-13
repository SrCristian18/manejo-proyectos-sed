package com.sed.gestor.proyectos.model.enums;

public enum FuenteFinanciacion {
    LIBRE_DESTINACION("Propios - Libre Destinación"),
    DESTINACION_ESPECIFICA("Propios - Destinación Específica"),
    SGP("SGP");

    private final String etiqueta;

    FuenteFinanciacion(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}