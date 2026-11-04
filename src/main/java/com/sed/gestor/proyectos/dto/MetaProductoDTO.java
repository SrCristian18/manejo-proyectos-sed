package com.sed.gestor.proyectos.dto;

public class MetaProductoDTO {

    private Long id;
    private String codigo;
    private String descripcion;
    private Double lineaBase;
    private Double metaCuatrienio;
    private String unidadMedida;
    private Long programaId; // ID del Programa al que pertenece

    public MetaProductoDTO() {
    }

    public MetaProductoDTO(Long id, String codigo, String descripcion, Double lineaBase, Double metaCuatrienio, String unidadMedida, Long programaId) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.lineaBase = lineaBase;
        this.metaCuatrienio = metaCuatrienio;
        this.unidadMedida = unidadMedida;
        this.programaId = programaId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getLineaBase() {
        return lineaBase;
    }

    public void setLineaBase(Double lineaBase) {
        this.lineaBase = lineaBase;
    }

    public Double getMetaCuatrienio() {
        return metaCuatrienio;
    }

    public void setMetaCuatrienio(Double metaCuatrienio) {
        this.metaCuatrienio = metaCuatrienio;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public Long getProgramaId() {
        return programaId;
    }

    public void setProgramaId(Long programaId) {
        this.programaId = programaId;
    }
}