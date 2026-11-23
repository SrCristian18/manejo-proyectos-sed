package com.sed.gestor.proyectos.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.sed.gestor.proyectos.model.AvanceMeta;

public class MetaEstrategicoResumenDTO {
    private BigDecimal metaCuatrienio = BigDecimal.ZERO;
    private BigDecimal metaAnualProgramada = BigDecimal.ZERO;
    private BigDecimal lineaBaseOriginal = BigDecimal.ZERO;
    private BigDecimal acumuladoAnterior = BigDecimal.ZERO;
    private BigDecimal puntoPartidaAnual = BigDecimal.ZERO;
    private BigDecimal avanceAnual = BigDecimal.ZERO;
    private BigDecimal acumuladoAnual = BigDecimal.ZERO;
    private BigDecimal acumuladoGeneral = BigDecimal.ZERO;
    
    private BigDecimal porcentajeCuatrienio = BigDecimal.ZERO;
    private BigDecimal porcentajeAnual = BigDecimal.ZERO;
    private BigDecimal promedioSimpleAnual = BigDecimal.ZERO;
    private BigDecimal promedioPonderadoAnual = BigDecimal.ZERO;
    private BigDecimal promedioSimpleCuatrienio = BigDecimal.ZERO;
    private BigDecimal promedioPonderadoCuatrienio = BigDecimal.ZERO;

    // Suma dinámica por Trimestre a partir de los meses
    private BigDecimal trimestre1 = BigDecimal.ZERO;
    private BigDecimal trimestre2 = BigDecimal.ZERO;
    private BigDecimal trimestre3 = BigDecimal.ZERO;
    private BigDecimal trimestre4 = BigDecimal.ZERO;

    private boolean noProgramada = false;

    private List<AvanceMeta> avancesVigencia = new ArrayList<>();


    public MetaEstrategicoResumenDTO(){}

    public BigDecimal getMetaCuatrienio() {
        return metaCuatrienio;
    }

    public void setMetaCuatrienio(BigDecimal metaCuatrienio) {
        this.metaCuatrienio = metaCuatrienio;
    }

    public BigDecimal getLineaBaseOriginal() {
        return lineaBaseOriginal;
    }

    public void setLineaBaseOriginal(BigDecimal lineaBaseOriginal) {
        this.lineaBaseOriginal = lineaBaseOriginal;
    }

    public BigDecimal getAcumuladoAnterior() {
        return acumuladoAnterior;
    }

    public void setAcumuladoAnterior(BigDecimal acumuladoAnterior) {
        this.acumuladoAnterior = acumuladoAnterior;
    }

    public BigDecimal getPuntoPartidaAnual() {
        return puntoPartidaAnual;
    }

    public void setPuntoPartidaAnual(BigDecimal puntoPartidaAnual) {
        this.puntoPartidaAnual = puntoPartidaAnual;
    }

    public BigDecimal getAvanceAnual() {
        return avanceAnual;
    }

    public void setAvanceAnual(BigDecimal avanceAnual) {
        this.avanceAnual = avanceAnual;
    }

    public BigDecimal getAcumuladoAnual() {
        return acumuladoAnual;
    }

    public void setAcumuladoAnual(BigDecimal acumuladoAnual) {
        this.acumuladoAnual = acumuladoAnual;
    }

    public BigDecimal getAcumuladoGeneral() {
        return acumuladoGeneral;
    }

    public void setAcumuladoGeneral(BigDecimal acumuladoGeneral) {
        this.acumuladoGeneral = acumuladoGeneral;
    }

    public BigDecimal getPorcentajeCuatrienio() {
        return porcentajeCuatrienio;
    }

    public void setPorcentajeCuatrienio(BigDecimal porcentajeCuatrienio) {
        this.porcentajeCuatrienio = porcentajeCuatrienio;
    }

    public BigDecimal getPorcentajeAnual() {
        return porcentajeAnual;
    }

    public void setPorcentajeAnual(BigDecimal porcentajeAnual) {
        this.porcentajeAnual = porcentajeAnual;
    }

    public BigDecimal getPromedioSimpleAnual() {
        return promedioSimpleAnual;
    }

    public void setPromedioSimpleAnual(BigDecimal promedioSimpleAnual) {
        this.promedioSimpleAnual = promedioSimpleAnual;
    }

    public BigDecimal getPromedioPonderadoAnual() {
        return promedioPonderadoAnual;
    }

    public void setPromedioPonderadoAnual(BigDecimal promedioPonderadoAnual) {
        this.promedioPonderadoAnual = promedioPonderadoAnual;
    }

    public BigDecimal getTrimestre1() {
        return trimestre1;
    }

    public void setTrimestre1(BigDecimal trimestre1) {
        this.trimestre1 = trimestre1;
    }

    public BigDecimal getTrimestre2() {
        return trimestre2;
    }

    public void setTrimestre2(BigDecimal trimestre2) {
        this.trimestre2 = trimestre2;
    }

    public BigDecimal getTrimestre3() {
        return trimestre3;
    }

    public void setTrimestre3(BigDecimal trimestre3) {
        this.trimestre3 = trimestre3;
    }

    public BigDecimal getTrimestre4() {
        return trimestre4;
    }

    public void setTrimestre4(BigDecimal trimestre4) {
        this.trimestre4 = trimestre4;
    }

    public BigDecimal getMetaAnualProgramada() {
        return metaAnualProgramada;
    }

    public void setMetaAnualProgramada(BigDecimal metaAnualProgramada) {
        this.metaAnualProgramada = metaAnualProgramada;
    }

    public BigDecimal getPromedioSimpleCuatrienio() {
        return promedioSimpleCuatrienio;
    }

    public void setPromedioSimpleCuatrienio(BigDecimal promedioSimpleCuatrienio) {
        this.promedioSimpleCuatrienio = promedioSimpleCuatrienio;
    }

    public BigDecimal getPromedioPonderadoCuatrienio() {
        return promedioPonderadoCuatrienio;
    }

    public void setPromedioPonderadoCuatrienio(BigDecimal promedioPonderadoCuatrienio) {
        this.promedioPonderadoCuatrienio = promedioPonderadoCuatrienio;
    }

    public List<AvanceMeta> getAvancesVigencia() {
        return avancesVigencia;
    }

    public void setAvancesVigencia(List<AvanceMeta> avancesVigencia) {
        this.avancesVigencia = avancesVigencia;
    }

    public boolean isNoProgramada() {
        return noProgramada;
    }

    public void setNoProgramada(boolean noProgramada) {
        this.noProgramada = noProgramada;
    }
}
