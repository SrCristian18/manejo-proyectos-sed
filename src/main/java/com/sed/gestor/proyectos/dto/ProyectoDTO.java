package com.sed.gestor.proyectos.dto;

public class ProyectoDTO {

    private Long id;
    private String codigoBpin;
    private String nombre;
    private Integer vigencia;
    private Long metaProductoId; // ID de la MetaProducto a relacionar

    public ProyectoDTO() {
    }

    public ProyectoDTO(Long id, String codigoBpin, String nombre, Integer vigencia, Long metaProductoId) {
        this.id = id;
        this.codigoBpin = codigoBpin;
        this.nombre = nombre;
        this.vigencia = vigencia;
        this.metaProductoId = metaProductoId;
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

    public Integer getVigencia() {
        return vigencia;
    }

    public void setVigencia(Integer vigencia) {
        this.vigencia = vigencia;
    }

    public Long getMetaProductoId() {
        return metaProductoId;
    }

    public void setMetaProductoId(Long metaProductoId) {
        this.metaProductoId = metaProductoId;
    }
}