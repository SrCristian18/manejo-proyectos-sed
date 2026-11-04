package com.sed.gestor.proyectos.model.politicapublica;

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
@Table (name = "objetivo_especifico")
public class ObjetivoEspecifico {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column (name = "resultado_esperado", columnDefinition = "TEXT")
    private String resultadoEsperado;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "factor_estrategico_id", nullable = false)
    private FactorEstrategico factorEstrategico;

    @OneToMany (mappedBy = "objetivoEspecifico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineaAccion> lineasAccion = new ArrayList<>();

    public ObjetivoEspecifico(){
    }

    public ObjetivoEspecifico(Long id, String descripcion, String resultadoEsperado,
            FactorEstrategico factorEstrategico) {
        this.id = id;
        this.descripcion = descripcion;
        this.resultadoEsperado = resultadoEsperado;
        this.factorEstrategico = factorEstrategico;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getResultadoEsperado() {
        return resultadoEsperado;
    }

    public void setResultadoEsperado(String resultadoEsperado) {
        this.resultadoEsperado = resultadoEsperado;
    }

    public FactorEstrategico getFactorEstrategico() {
        return factorEstrategico;
    }

    public void setFactorEstrategico(FactorEstrategico factorEstrategico) {
        this.factorEstrategico = factorEstrategico;
    }

    public List<LineaAccion> getLineasAccion() {
        return lineasAccion;
    }

    public void setLineasAccion(List<LineaAccion> lineasAccion) {
        this.lineasAccion = lineasAccion;
    }

    public void addLineaAccion(LineaAccion linea) {
        lineasAccion.add(linea);
        linea.setObjetivoEspecifico(this);
    }

    public void removeLineaAccion(LineaAccion linea) {
        lineasAccion.remove(linea);
        linea.setObjetivoEspecifico(null);
    }
}
