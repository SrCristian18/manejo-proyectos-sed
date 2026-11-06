package com.sed.gestor.proyectos.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sed.gestor.proyectos.model.enums.FuenteFinanciacion;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity 
@Table (name = "actividades")
public class Actividad {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "nombre", nullable = false, length = 500)
    private String nombre;

    @Column (name = "presupuesto_inicial", precision = 15, scale = 2)
    private BigDecimal presupuestoInicial = BigDecimal.ZERO;

    @Enumerated (EnumType.STRING)
    @Column (name = "fuente", length = 50)
    private FuenteFinanciacion fuente;

    @Column (name = "descripcion", length = 1000)
    private String descripcion;

    @Column (name = "meta_cuatrienio", precision = 12, scale = 2)
    private BigDecimal metaCuatrienio = BigDecimal.ZERO;

    @Column (name = "unidad_medida", length = 100)
    private String unidadMedida;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "proyecto_id", nullable = false)
    @JsonIgnoreProperties ("actividades")
    private Proyecto proyecto;

    @OneToMany (mappedBy = "actividad", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties ("actividad")
    private List<RubroGasto> rubros = new ArrayList<>();

    @OneToMany (mappedBy = "actividad", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties ("actividad")
    private List<AvanceActividad> avances = new ArrayList<>();

    public Actividad(){
    
    }

    public Actividad(String nombre, BigDecimal presupuestoInicial, FuenteFinanciacion fuente, String descripcion,
            Proyecto proyecto) {
        this.nombre = nombre;
        this.presupuestoInicial = presupuestoInicial;
        this.fuente = fuente;
        this.descripcion = descripcion;
        this.proyecto = proyecto;
    }

    public void addRubro(RubroGasto rubro){
        rubros.add(rubro);
        rubro.setActividad(this);
    }

    public void removeRubro(RubroGasto rubro) {
        rubros.remove(rubro);
        rubro.setActividad(null);
    }

    public void addAvance(AvanceActividad avance) {
        avances.add(avance);
        avance.setActividad(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPresupuestoInicial() {
        return presupuestoInicial;
    }

    public void setPresupuestoInicial(BigDecimal presupuestoInicial) {
        this.presupuestoInicial = presupuestoInicial;
    }

    public FuenteFinanciacion getFuente() {
        return fuente;
    }

    public void setFuente(FuenteFinanciacion fuente) {
        this.fuente = fuente;
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

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public List<RubroGasto> getRubros() {
        return rubros;
    }

    public void setRubros(List<RubroGasto> rubros) {
        this.rubros = rubros;
    }

    public List<AvanceActividad> getAvances() {
        return avances;
    }

    public void setAvances(List<AvanceActividad> avances) {
        this.avances = avances;
    }
}
