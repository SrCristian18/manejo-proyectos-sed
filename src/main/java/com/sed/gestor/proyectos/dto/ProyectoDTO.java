package com.sed.gestor.proyectos.dto;

import java.math.BigDecimal;

public class ProyectoDTO {
    private Long id;
    private String codigoBpin;
    private String nombre;
    private String programa;
    private String politicaPublica;
    private String ejeEstrategico;
    private BigDecimal presupuestoInicialGlobal = BigDecimal.ZERO;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCodigoBpin() {
        return codigoBpin;
    }
    public void setCodigoBpin(String codigoBpin) {
        this.codigoBpin = codigoBpin;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getPrograma() {
        return programa;
    }
    public void setPrograma(String programa) {
        this.programa = programa;
    }
    public String getPoliticaPublica() {
        return politicaPublica;
    }
    public void setPoliticaPublica(String politicaPublica) {
        this.politicaPublica = politicaPublica;
    }
    public String getEjeEstrategico() {
        return ejeEstrategico;
    }
    public void setEjeEstrategico(String ejeEstrategico) {
        this.ejeEstrategico = ejeEstrategico;
    }
    public BigDecimal getPresupuestoInicialGlobal() {
        return presupuestoInicialGlobal;
    }
    public void setPresupuestoInicialGlobal(BigDecimal presupuestoInicialGlobal) {
        this.presupuestoInicialGlobal = presupuestoInicialGlobal;
    }
    
}
