package com.sed.gestor.proyectos.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sed.gestor.proyectos.model.politicapublica.Producto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity 
@Table (name = "proyectos")
public class Proyecto {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "codigo_bpin", nullable = false, unique = true, length = 50)
    private String codigoBpin;

    @Column (name = "nombre", nullable = false, length = 255)
    private String nombre;

    @Column (name = "vigencia", nullable = false)
    private Integer vigencia;

    @Column (name = "vigencia_fin", nullable = false)
    private Integer vigenciaFin;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "meta_producto_id", nullable = true)
    @JsonIgnoreProperties ("proyectos")
    private MetaProducto metaProducto;

    @OneToMany (mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties ("proyecto")
    private List<Actividad> actividades = new ArrayList<>();

    @ManyToMany (mappedBy = "proyectos", fetch = FetchType.LAZY)
    private Set<Producto> productos = new HashSet<>();
    
    public Proyecto(){
    }

    public Proyecto(String codigoBpin, String nombre, Integer vigencia, MetaProducto metaProducto) {
        this.codigoBpin = codigoBpin;
        this.nombre = nombre;
        this.vigencia = vigencia;
        this.vigenciaFin = calcularVigenciaFinPorDefecto(vigencia);
        this.metaProducto = metaProducto;
    }

    public Proyecto(String codigoBpin, String nombre, Integer vigencia, Integer vigenciaFin,
            MetaProducto metaProducto) {
        this.codigoBpin = codigoBpin;
        this.nombre = nombre;
        this.vigencia = vigencia;
        this.vigenciaFin = (vigenciaFin != null) ? vigenciaFin : calcularVigenciaFinPorDefecto(vigencia);
        this.metaProducto = metaProducto;
    }

    @PrePersist 
    @PreUpdate 
    public void asegurarHorizonteVigencias() {
        if (this.vigencia != null && this.vigenciaFin == null) {
            this.vigenciaFin = resolverVigenciaFin(this.vigencia, this.metaProducto);
        }
    }

    public static Integer resolverVigenciaFin(Integer vigenciaInicio, MetaProducto metaProducto) {
        if (vigenciaInicio == null) {
            return null;
        }
        // 1. Prioridad: tomar anoFin del Plan de Desarrollo vinculado en la jerarquía
        if (metaProducto != null
                && metaProducto.getPrograma() != null
                && metaProducto.getPrograma().getMetaResultado() != null
                && metaProducto.getPrograma().getMetaResultado().getImpulsorAvance() != null
                && metaProducto.getPrograma().getMetaResultado().getImpulsorAvance().getLineaEstrategica() != null
                && metaProducto.getPrograma().getMetaResultado().getImpulsorAvance().getLineaEstrategica().getPlanDesarrollo() != null) {
            Integer anoFinPdd = metaProducto.getPrograma().getMetaResultado()
                    .getImpulsorAvance().getLineaEstrategica().getPlanDesarrollo().getAnoFin();
            if (anoFinPdd != null && anoFinPdd >= vigenciaInicio) {
                return anoFinPdd;
            }
        }
        // 2. Respaldo dinámico: cierre matemático del cuatrienio territorial (ciclos de 4 años)
        return calcularVigenciaFinPorDefecto(vigenciaInicio);
    }

    //parametrizar para cualquier cuatrienio
    public static Integer calcularVigenciaFinPorDefecto(Integer vigenciaInicio) {
        if (vigenciaInicio == null) {
            return null;
        }
        int offsetDentroDelCuatrienio = Math.floorMod(vigenciaInicio, 4);
        return vigenciaInicio - offsetDentroDelCuatrienio + 3;
    }

    public boolean estaActivoEnVigencia(Integer anioConsulta) {
        if (anioConsulta == null || this.vigencia == null) {
            return true;
        }
        int fin = (this.vigenciaFin != null) ? this.vigenciaFin : resolverVigenciaFin(this.vigencia, this.metaProducto);
        return anioConsulta >= this.vigencia && anioConsulta <= fin;
    }

    //auxiliar
    public List<Integer> getVigenciasActivas() {
        List<Integer> anios = new ArrayList<>();
        if (this.vigencia == null) {
            return anios;
        }
        int fin = (this.vigenciaFin != null) ? this.vigenciaFin : resolverVigenciaFin(this.vigencia, this.metaProducto);
        for (int a = this.vigencia; a <= fin; a++) {
            anios.add(a);
        }
        return anios;
    }

    public String getEtiquetaHorizonte() {
        if (this.vigencia == null) {
            return "";
        }
        int fin = (this.vigenciaFin != null) ? this.vigenciaFin : resolverVigenciaFin(this.vigencia, this.metaProducto);
        return this.vigencia.equals(fin) ? String.valueOf(this.vigencia) : (this.vigencia + " - " + fin);
    }

    public void addActividad(Actividad actividad){
        actividades.add(actividad);
        actividad.setProyecto(this);
    }

    public void removeActividad(Actividad actividad){
        actividades.remove(actividad);
        actividad.setProyecto(null);
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

    public MetaProducto getMetaProducto() {
        return metaProducto;
    }

    public void setMetaProducto(MetaProducto metaProducto) {
        this.metaProducto = metaProducto;
    }

    public List<Actividad> getActividades() {
        return actividades;
    }

    public void setActividades(List<Actividad> actividades) {
        this.actividades = actividades;
    }

    public Set<Producto> getProductos() {
        return productos;
    }

    public void setProductos(Set<Producto> productos) {
        this.productos = productos;
    }

    public Integer getVigenciaFin() {
        return vigenciaFin;
    }

    public void setVigenciaFin(Integer vigenciaFin) {
        this.vigenciaFin = vigenciaFin;
    }
}