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

    @Column (name = "codigo_programa", length = 20)
    private String codigoPrograma;

    @Column (name = "tipo_meta", nullable = false, length = 50)
    private String tipoMeta;

    @Column (name = "descripcion", nullable = false, length = 500)
    private String descripcion;

    @Column (name = "unidad_medida", nullable = false, length = 100)
    private String unidadMedida;

    @Column (name = "programado_anual", precision = 12, scale = 2)
    private BigDecimal programadoAnual = BigDecimal.ZERO;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "proyecto_id", nullable = false)
    @JsonIgnoreProperties ("metas")
    private Proyecto proyecto;

    @OneToMany (mappedBy = "metaProducto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AvanceMeta> avances = new ArrayList<>();

    public MetaProducto(){
    }

    public BigDecimal getTotalEjecutado(){
        if(avances == null || avances.isEmpty()) return BigDecimal.ZERO;
        return avances.stream()
            .map(AvanceMeta::getValorLogrado)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getPorcentajeCumplimiento(){
        if(programadoAnual == null || programadoAnual.compareTo(BigDecimal.ZERO) == 0){
            return BigDecimal.ZERO;
        }
            return getTotalEjecutado()
                .multiply(new BigDecimal("100"))
                .divide(programadoAnual,2,RoundingMode.HALF_UP); 
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

    public String getTipoMeta() {
        return tipoMeta;
    }

    public void setTipoMeta(String tipoMeta) {
        this.tipoMeta = tipoMeta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public BigDecimal getProgramadoAnual() {
        return programadoAnual;
    }

    public void setProgramadoAnual(BigDecimal programadoAnual) {
        this.programadoAnual = programadoAnual;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public List<AvanceMeta> getAvances() {
        return avances;
    }

    public void setAvances(List<AvanceMeta> avances) {
        this.avances = avances;
    }

/* 
    public void addAvance(AvanceMeta avance){
        avances.add(avance);
        avance.setMetaProducto(this);
    }
*/
}
