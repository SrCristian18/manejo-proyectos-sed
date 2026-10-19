package com.sed.gestor.proyectos.dto;

import java.util.ArrayList;
import java.util.List;

public class SolicitudActualizacionDTO {
    private Long proyectoId;
    private String numeroDecreto;
    private String justificacion;
    private List<AjusteCostoDTO> ajustes = new ArrayList<>();

    public SolicitudActualizacionDTO(){
    }

    public Long getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(Long proyectoId) {
        this.proyectoId = proyectoId;
    }

    public String getNumeroDecreto() {
        return numeroDecreto;
    }

    public void setNumeroDecreto(String numeroDecreto) {
        this.numeroDecreto = numeroDecreto;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

    public List<AjusteCostoDTO> getAjustes() {
        return ajustes;
    }

    public void setAjustes(List<AjusteCostoDTO> ajustes) {
        this.ajustes = ajustes;
    }

}
