package com.sed.gestor.proyectos.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity 
@Table (name = "metas_producto")
public class MetaProducto {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "codigo_programa", nullable = false, length = 50)
    private String codigoPrograma;
    
    @Column (name = "descripcion_meta", nullable = false, length = 500)
    private String descripcionMeta;

    @Column (name = "unidad_medida", nullable = false, length = 100)
    private String unidadMedida;

    @Column (name = "programacion_cuatrienio", precision = 12, scale = 2)
    private BigDecimal programacionCuatrienio;

    @Column (name = "programacion_vigencia", precision = 12, scale = 2)
    private BigDecimal programacionVigencia;

    @Column (name = "ponderacion", precision = 12, scale = 2)
    private BigDecimal ponderacion;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "actividad_id", nullable = false)
    private Actividad actividad;

    @OneToMany (mappedBy = "metaProducto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AvanceMeta> avances = new ArrayList<>();

    public MetaProducto(){
    }
    public void addAvance(AvanceMeta avance){
        avances.add(avance);
        avance.setMetaProducto(this);
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCodigoPrograma() {
        return codigoPrograma;
    }
    public void setCodigoPrograma(String codigoPrograma) {
        this.codigoPrograma = codigoPrograma;
    }
    public String getDescripcionMeta() {
        return descripcionMeta;
    }
    public void setDescripcionMeta(String descripcionMeta) {
        this.descripcionMeta = descripcionMeta;
    }
    public String getUnidadMedida() {
        return unidadMedida;
    }
    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }
    public BigDecimal getProgramacionCuatrienio() {
        return programacionCuatrienio;
    }
    public void setProgramacionCuatrienio(BigDecimal programacionCuatrienio) {
        this.programacionCuatrienio = programacionCuatrienio;
    }
    public BigDecimal getProgramacionVigencia() {
        return programacionVigencia;
    }
    public void setProgramacionVigencia(BigDecimal programacionVigencia) {
        this.programacionVigencia = programacionVigencia;
    }
    public BigDecimal getPonderacion() {
        return ponderacion;
    }
    public void setPonderacion(BigDecimal ponderacion) {
        this.ponderacion = ponderacion;
    }
    public Actividad getActividad() {
        return actividad;
    }
    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }
    public List<AvanceMeta> getAvances() {
        return avances;
    }
    public void setAvances(List<AvanceMeta> avances) {
        this.avances = avances;
    }
}
