package com.sed.gestor.proyectos.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

    @Column (name = "codigo", nullable = false, length = 20)
    private String codigo;

    @Column (name = "descripcion", nullable = false, length = 500)
    private String descripcion;

    @Column (name = "linea_base")
    private Double lineaBase;

    @Column (name = "meta_cuatrienio")
    private Double metaCuatrienio;

    @Column (name = "unidad_medida", length = 50)
    private String unidadMedida;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "programa_id", nullable = false)
    @JsonIgnoreProperties ("metasProducto")
    private Programa programa;

    @OneToMany (mappedBy = "metaProducto", cascade = CascadeType.ALL)
    @JsonIgnoreProperties ("metaProducto")
    private List<Proyecto> proyectos = new ArrayList<>();
    
    @OneToMany (mappedBy = "metaProducto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties ("metaProducto")
    private List<AvanceMeta> avances = new ArrayList<>();

    public MetaProducto(){
    }

    public MetaProducto(String codigo, String descripcion, Double lineaBase, Double metaCuatrienio, String unidadMedida,
            Programa programa) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.lineaBase = lineaBase;
        this.metaCuatrienio = metaCuatrienio;
        this.unidadMedida = unidadMedida;
        this.programa = programa;
    }

    public void addProyecto(Proyecto proyecto) {
        proyectos.add(proyecto);
        proyecto.setMetaProducto(this);
    }

    public void removeProyecto(Proyecto proyecto) {
        proyectos.remove(proyecto);
        proyecto.setMetaProducto(null);
    }

    public void addAvance(AvanceMeta avance) {
        avances.add(avance);
        avance.setMetaProducto(this);
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

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }

    public List<Proyecto> getProyectos() {
        return proyectos;
    }

    public void setProyectos(List<Proyecto> proyectos) {
        this.proyectos = proyectos;
    }

    public List<AvanceMeta> getAvances() {
        return avances;
    }

    public void setAvances(List<AvanceMeta> avances) {
        this.avances = avances;
    }

    //Metodos auxiliares
    public BigDecimal getAvanceAcumulado() {
        if (avances == null || avances.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return avances.stream()
                .map(AvanceMeta::getValorLogrado)
                .filter(v -> v != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getPorcentajeAvance() {
        if (metaCuatrienio == null || metaCuatrienio == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal acumulado = getAvanceAcumulado();
        BigDecimal meta = BigDecimal.valueOf(metaCuatrienio);
        return acumulado.multiply(BigDecimal.valueOf(100))
                .divide(meta, 2, RoundingMode.HALF_UP);
    }
}
