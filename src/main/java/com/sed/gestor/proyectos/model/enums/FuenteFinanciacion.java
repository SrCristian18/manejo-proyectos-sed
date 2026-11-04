package com.sed.gestor.proyectos.model.enums;

public enum FuenteFinanciacion {
    LIBRE_DESTINACION("Recursos Propios - Libre Destinación", true, "#0d6efd"),
    DESTINACION_ESPECIFICA("Recursos Propios - Destinación especifica", true, "#0dcaf0"),
    SGP("SGP", false, "#198754");

    private final String etiqueta;
    private final boolean esRecursoPropio;
    private final String colorGrafico;

    FuenteFinanciacion(String etiqueta, boolean esRecursoPropio, String colorGrafico){
        this.etiqueta = etiqueta;
        this.esRecursoPropio = esRecursoPropio;
        this.colorGrafico = colorGrafico;
    }

    public String getEtiqueta(){
        return etiqueta;
    }

    public boolean isEsRecursoPropio(){
        return esRecursoPropio;
    }

    public String getColorGrafico(){
        return colorGrafico;
    }

}