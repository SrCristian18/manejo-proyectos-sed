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

    @Transactional(readOnly = true)
    public DashboardProyectoDTO obtenerResumenDashboard(Long proyectoId, Integer vigencia) {
        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));

        List<MetaProducto> metas = metaRepository.findByActividadProyectoId(proyectoId);
        List<MetaAvanceDTO> resumenMetas = new ArrayList<>();
        BigDecimal sumaPorcentajes = BigDecimal.ZERO;

        for (MetaProducto meta : metas) {
            BigDecimal logrado = metaRepository.calcularAcumuladoFisicoMeta(meta.getId(), vigencia);
            BigDecimal programado = meta.getProgramacionVigencia();
            
            BigDecimal porcentaje = BigDecimal.ZERO;
            if (programado != null && programado.compareTo(BigDecimal.ZERO) > 0) {
                porcentaje = logrado.multiply(new BigDecimal("100")).divide(programado, 2, RoundingMode.HALF_UP);
            }

            sumaPorcentajes = sumaPorcentajes.add(porcentaje);
            resumenMetas.add(new MetaAvanceDTO(
                    meta.getDescripcionMeta(),
                    meta.getUnidadMedida(),
                    programado,
                    logrado,
                    porcentaje
            ));
        }

        BigDecimal avancePromedio = BigDecimal.ZERO;
        if (!metas.isEmpty()) {
            avancePromedio = sumaPorcentajes.divide(new BigDecimal(metas.size()), 2, RoundingMode.HALF_UP);
        }

        DashboardProyectoDTO dto = new DashboardProyectoDTO();
        dto.setCodigoBpin(proyecto.getCodigoBpin());
        dto.setNombreProyecto(proyecto.getNombre());
        dto.setPorcentajeAvanceFisicoPromedio(avancePromedio);
        dto.setResumenMetas(resumenMetas);

        return dto;
    }
}