package com.sed.gestor.proyectos.model;

public class Proyecto {
    private Long id;
    private String codigoBpin;
    private String nombre;
    private String programa;
    
    public Proyecto()
    {
        
    }
    
    public Proyecto(Long id, String codigoBpin, String nombre, String programa) {
        this.id = id;
        this.codigoBpin = codigoBpin;
        this.nombre = nombre;
        this.programa = programa;
    }

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

}
