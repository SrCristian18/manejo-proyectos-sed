package com.sed.gestor.proyectos.service;

import com.sed.gestor.proyectos.dto.DashboardProyectoDTO;
import com.sed.gestor.proyectos.dto.MetaAvanceDTO;
import com.sed.gestor.proyectos.model.AvanceMeta;
import com.sed.gestor.proyectos.model.MetaProducto;
import com.sed.gestor.proyectos.model.Proyecto;
import com.sed.gestor.proyectos.repository.AvanceMetaRepository;
import com.sed.gestor.proyectos.repository.MetaProductoRepository;
import com.sed.gestor.proyectos.repository.ProyectoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MetaService {

    private final MetaProductoRepository metaRepository;
    private final AvanceMetaRepository avanceRepository;
    private final ProyectoRepository proyectoRepository;

    public MetaService(MetaProductoRepository metaRepository,
                       AvanceMetaRepository avanceRepository,
                       ProyectoRepository proyectoRepository) {
        this.metaRepository = metaRepository;
        this.avanceRepository = avanceRepository;
        this.proyectoRepository = proyectoRepository;
    }

    @Transactional
    public AvanceMeta registrarAvance(Long metaProductoId, Integer trimestre, Integer vigencia, BigDecimal valorLogrado, String observaciones) {
        MetaProducto meta = metaRepository.findById(metaProductoId)
                .orElseThrow(() -> new IllegalArgumentException("Meta de producto no encontrada"));

        AvanceMeta avance = new AvanceMeta();
        avance.setMetaProducto(meta);
        avance.setTrimestre(trimestre);
        avance.setVigencia(vigencia);
        avance.setValorLogrado(valorLogrado);
        avance.setObservaciones(observaciones);
        avance.setFechaReporte(LocalDateTime.now());

        return avanceRepository.save(avance);
    }

    @Transactional 
    public MetaProducto crearMeta(Long proyectoId, String tipoMeta, String codigoPrograma, String descripcion, String unidadMedida, BigDecimal programadoAnual){
        Proyecto proyecto = proyectoRepository.findById(proyectoId)
            .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));

        MetaProducto meta = new MetaProducto();
        meta.setProyecto(proyecto);
        meta.setTipoMeta(tipoMeta);
        meta.setCodigoPrograma(codigoPrograma);
        meta.setDescripcion(descripcion);
        meta.setUnidadMedida(unidadMedida);
        meta.setProgramadoAnual(programadoAnual);

        return metaRepository.save(meta);
    }

    //ajustar
    @Transactional(readOnly = true)
    public DashboardProyectoDTO obtenerResumenDashboard(Long proyectoId, Integer vigencia) {
        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));

        List<MetaProducto> metas = metaRepository.findByProyectoId(proyectoId);
        List<MetaAvanceDTO> resumenMetas = new ArrayList<>();
        BigDecimal sumaPorcentajes = BigDecimal.ZERO;

        for (MetaProducto meta : metas) {
            BigDecimal logrado = meta.getAvances().stream()
                .filter(a -> a.getVigencia() != null && a.getVigencia().equals(vigencia))
                .map(a -> a.getValorLogrado() != null ? a.getValorLogrado() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            BigDecimal programado = meta.getProgramadoAnual() != null ? meta.getProgramadoAnual() : BigDecimal.ZERO;
                        
            BigDecimal porcentaje = BigDecimal.ZERO;
            if (programado.compareTo(BigDecimal.ZERO) > 0) {
                porcentaje = logrado.multiply(new BigDecimal("100")).divide(programado, 2, RoundingMode.HALF_UP);
            }

            sumaPorcentajes = sumaPorcentajes.add(porcentaje);

            String textoDescripcion = (meta.getCodigoPrograma() != null && !meta.getCodigoPrograma().isBlank())
                ? "[" + meta.getCodigoPrograma() + "]" + meta.getDescripcion()
                : meta.getDescripcion();

            resumenMetas.add(new MetaAvanceDTO(
                textoDescripcion,
                meta.getUnidadMedida(),
                programado,
                logrado,
                porcentaje
            ));
        }

        BigDecimal promedioAvance = BigDecimal.ZERO;
        if (!metas.isEmpty()) {
            promedioAvance = sumaPorcentajes.divide(new BigDecimal(metas.size()), 2, RoundingMode.HALF_UP);
        }

        return new DashboardProyectoDTO(
            proyecto.getNombre(),
            proyecto.getCodigoBpin(),
            promedioAvance,
            resumenMetas
        );
    }
}