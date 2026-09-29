package com.sed.gestor.proyectos.model;

import java.time.LocalDate;

public class Actividad {
    private Long id;
    private Proyecto proyecto;
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFinal;

    public Actividad()
    {

    }

    public Actividad(Long id, Proyecto proyecto, String nombre, LocalDate fechaInicio, LocalDate fechaFinal) {
        this.id = id;
        this.proyecto = proyecto;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(LocalDate fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

}
