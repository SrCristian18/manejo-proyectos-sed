package com.sed.gestor.proyectos.dto;

public class ProyectoDTO {
    private Long id;
    private String codigoBpin;
    private String nombre;
    private String metaResultado;
    private String metaProducto;
    private Integer vigencia;

    public ProyectoDTO() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigoBpin() { return codigoBpin; }
    public void setCodigoBpin(String codigoBpin) { this.codigoBpin = codigoBpin; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getMetaResultado() { return metaResultado; }
    public void setMetaResultado(String metaResultado) { this.metaResultado = metaResultado; }

    public String getMetaProducto() { return metaProducto; }
    public void setMetaProducto(String metaProducto) { this.metaProducto = metaProducto; }

    public Integer getVigencia() { return vigencia; }
    public void setVigencia(Integer vigencia) { this.vigencia = vigencia; }
}