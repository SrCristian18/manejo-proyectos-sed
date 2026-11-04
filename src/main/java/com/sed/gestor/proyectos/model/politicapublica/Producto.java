package com.sed.gestor.proyectos.model.politicapublica;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.sed.gestor.proyectos.model.Proyecto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity 
@Table (name = "producto")
public class Producto {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (length = 50)
    private String codigo;

    @Column (nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column (name = "meta_cuatrienio", precision = 12, scale = 2)
    private BigDecimal metaCuatrienio;

    @Column (name = "unidad_medida", length = 100)
    private String unidadMedida;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "linea_accion_id", nullable = false)
    private LineaAccion lineaAccion;

    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable (
        name = "producto_proyecto",
        joinColumns = @JoinColumn (name = "producto_id"),
        inverseJoinColumns = @JoinColumn (name = "proyecto_id")
    )
    private Set<Proyecto> proyectos = new HashSet<>();

    public Producto(){
    }

    public Producto(Long id, String codigo, String descripcion, BigDecimal metaCuatrienio, String unidadMedida,
            LineaAccion lineaAccion) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.metaCuatrienio = metaCuatrienio;
        this.unidadMedida = unidadMedida;
        this.lineaAccion = lineaAccion;
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

    public BigDecimal getMetaCuatrienio() {
        return metaCuatrienio;
    }

    public void setMetaCuatrienio(BigDecimal metaCuatrienio) {
        this.metaCuatrienio = metaCuatrienio;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public LineaAccion getLineaAccion() {
        return lineaAccion;
    }

    public void setLineaAccion(LineaAccion lineaAccion) {
        this.lineaAccion = lineaAccion;
    }

    public Set<Proyecto> getProyectos() {
        return proyectos;
    }

    public void setProyectos(Set<Proyecto> proyectos) {
        this.proyectos = proyectos;
    }

    public void addProyectoPdd(Proyecto proyecto) {
        this.proyectos.add(proyecto);
    }

    public void removeProyectoPdd(Proyecto proyecto) {
        this.proyectos.remove(proyecto);
    }

}
