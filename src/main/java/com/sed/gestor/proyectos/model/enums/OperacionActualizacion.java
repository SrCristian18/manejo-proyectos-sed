package com.sed.gestor.proyectos.model.enums;

public enum OperacionActualizacion {
    
    //opciones
    OP_01_BENEFICIARIOS(1, "1. Disminución o incremento de la cantidad de beneficiarios", false),
    OP_02_ACTIVIDADES_NUEVAS(2, "2. Inclusión de actividades nuevas", false),
    OP_03_REDISTRIBUCION_COSTOS(3, "3. Redistribución de costos entre actividades (mismo valor proyecto)", false),
    OP_04_HORIZONTE_EJECUCION(4, "4. Ampliación en el horizonte de ejecución", false),
    OP_05_LOCALIZACION(5, "5. Cambio en la localización", false),
    OP_06_REGIONALIZACION(6, "6. Cambio en la regionalización del proyecto", false),
    OP_07_AJUSTE_COSTOS_INICIALES(7, "7. Reducción o aumento de los costos iniciales", true),
    OP_08_FUENTES_FINANCIACION(8, "8. Modificación en las fuentes de financiación", false),
    OP_09_META_PRODUCTOS(9, "9. Variación en la meta de los productos o indicadores", false),
    OP_10_POLITICAS_TRANSVERSALES(10, "10. Marcación y/o variación de políticas transversales", false),
    OP_11_INCLUSION_IND_SECUNDARIOS(11, "11. Inclusión de indicadores secundarios", false),
    OP_12_VARIACION_IND_SECUNDARIOS(12, "12. Variación en la meta de indicadores secundarios", false),
    OP_13_CARGUE_DOCUMENTOS(13, "13. Cargue de documentos soporte", false);

    private final int codigo;
    private final String etiqueta;
    private final boolean habilitada;

    OperacionActualizacion(int codigo, String etiqueta, boolean habilitada){
        this.codigo = codigo;
        this.etiqueta = etiqueta;
        this.habilitada = habilitada;
    }
    
    public int getCodigo(){
        return codigo;
    }

    public String getEtiqueta(){
        return etiqueta;
    }

    public boolean isHabilitada(){
        return habilitada;
    }
}
