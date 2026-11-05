package com.sed.gestor.proyectos.service;

import com.sed.gestor.proyectos.dto.ActividadAvanceDTO;
import com.sed.gestor.proyectos.dto.DashboardProyectoDTO;
import com.sed.gestor.proyectos.dto.DashboardProyectoDTO.MetaResumenDTO;
import com.sed.gestor.proyectos.dto.MetaProductoDTO;
import com.sed.gestor.proyectos.dto.ReporteTrimestralProyectoFormDTO;
import com.sed.gestor.proyectos.model.Actividad;
import com.sed.gestor.proyectos.model.AvanceActividad;
import com.sed.gestor.proyectos.model.AvanceMeta;
import com.sed.gestor.proyectos.model.MetaProducto;
import com.sed.gestor.proyectos.model.Programa;
import com.sed.gestor.proyectos.model.Proyecto;
import com.sed.gestor.proyectos.repository.ActividadRepository;
import com.sed.gestor.proyectos.repository.AvanceActividadRepository;
import com.sed.gestor.proyectos.repository.AvanceMetaRepository;
import com.sed.gestor.proyectos.repository.MetaProductoRepository;
import com.sed.gestor.proyectos.repository.ProgramaRepository;
import com.sed.gestor.proyectos.repository.ProyectoRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MetaService {

    private final MetaProductoRepository metaProductoRepository;
    private final ProgramaRepository programaRepository;
    private final AvanceMetaRepository avanceMetaRepository;
    private final ProyectoRepository proyectoRepository;
    private final ActividadRepository actividadRepository;
    private final AvanceActividadRepository avanceActividadRepository;

    @Autowired 
    public MetaService(MetaProductoRepository metaProductoRepository, ProgramaRepository programaRepository,
        AvanceMetaRepository avanceMetaRepository, ProyectoRepository proyectoRepository, ActividadRepository actividadRepository,
        AvanceActividadRepository avanceActividadRepository) {
        this.metaProductoRepository = metaProductoRepository;
        this.programaRepository = programaRepository;
        this.avanceMetaRepository = avanceMetaRepository;
        this.proyectoRepository = proyectoRepository;
        this.actividadRepository = actividadRepository;
        this.avanceActividadRepository = avanceActividadRepository;
    }

    //Dashboard
    @Transactional(readOnly = true)
    public DashboardProyectoDTO obtenerResumenDashboard(Long proyectoId, Integer vigencia) {
        Proyecto proyecto = proyectoRepository.findById(proyectoId).orElse(null);

        if (proyecto == null || proyecto.getMetaProducto() == null) {
            return new DashboardProyectoDTO();
        }

        MetaProducto metaProducto = proyecto.getMetaProducto();
        List<AvanceMeta> avances = metaProducto.getAvances();

        BigDecimal acumulado = BigDecimal.ZERO;
        if (avances != null && !avances.isEmpty()) {
            for (AvanceMeta avance : avances) {
                if (vigencia == null || avance.getVigencia().equals(vigencia)) {
                    acumulado = acumulado.add(avance.getValorLogrado());
                }
            }
        }

        BigDecimal metaCuatrienio = metaProducto.getMetaCuatrienio() != null 
                ? BigDecimal.valueOf(metaProducto.getMetaCuatrienio()) 
                : BigDecimal.ZERO;

        BigDecimal porcentajeAvance = BigDecimal.ZERO;
        if (metaCuatrienio.compareTo(BigDecimal.ZERO) > 0) {
            porcentajeAvance = acumulado.multiply(BigDecimal.valueOf(100))
                    .divide(metaCuatrienio, 2, RoundingMode.HALF_UP);
        }

        List<MetaResumenDTO> resumenLista = new ArrayList<>();
        resumenLista.add(new MetaResumenDTO(metaProducto.getDescripcion(), porcentajeAvance));

        return new DashboardProyectoDTO(porcentajeAvance, resumenLista);
    }

    //Manejo de Metas
    @Transactional (readOnly = true)
    public List<MetaProducto> obtenerTodasLasMetasProducto(){
        return metaProductoRepository.findAll();
    }

    public MetaProducto obtenerMetaProductoPorId(Long id){
        return metaProductoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Meta de producto no encontrada con ID: "+ id));
    }

    @Transactional 
    public MetaProducto guardarMetaProducto(MetaProductoDTO dto){
        MetaProducto metaProducto;
        if(dto.getId() != null){
            metaProducto = obtenerMetaProductoPorId(dto.getId());
        }else{
            metaProducto = new MetaProducto();
        }

        metaProducto.setCodigo(dto.getCodigo());
        metaProducto.setDescripcion(dto.getDescripcion());
        metaProducto.setLineaBase(dto.getLineaBase());
        metaProducto.setMetaCuatrienio(dto.getMetaCuatrienio());
        metaProducto.setUnidadMedida(dto.getUnidadMedida());

        if(dto.getProgramaId() != null){
            Programa programa = programaRepository.findById(dto.getProgramaId())
                .orElseThrow(() -> new EntityNotFoundException("Programa no encontrado con ID: "+ dto.getProgramaId()));
            metaProducto.setPrograma(programa);
        }
        return metaProductoRepository.save(metaProducto);
    }

    @Transactional
    public void eliminarMetaProducto(Long id) {
        if (!metaProductoRepository.existsById(id)) {
            throw new EntityNotFoundException("Meta Producto no encontrada con ID: " + id);
        }
        metaProductoRepository.deleteById(id);
    }

    //Avances e informacion
    public AvanceMeta registrarAvance(Long metaProductoId, Integer trimestre, Integer vigencia, BigDecimal valorLogrado, String observaciones){
        MetaProducto metaProducto = obtenerMetaProductoPorId(metaProductoId);
        AvanceMeta avance = new AvanceMeta(trimestre, vigencia, valorLogrado, observaciones, LocalDateTime.now(), metaProducto);
        metaProducto.addAvance(avance);

        return avanceMetaRepository.save(avance);
    }

    //nuevo
    @Transactional(readOnly = true)
    public ReporteTrimestralProyectoFormDTO obtenerFormularioReporteProyecto(Long proyectoId, Integer vigencia, Integer trimestre) {
        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new EntityNotFoundException("Proyecto no encontrado con ID: " + proyectoId));

        ReporteTrimestralProyectoFormDTO form = new ReporteTrimestralProyectoFormDTO();
        form.setProyectoId(proyectoId);
        form.setVigencia(vigencia);
        form.setTrimestre(trimestre);

        if (proyecto.getMetaProducto() != null) {
            form.setMetaProductoId(proyecto.getMetaProducto().getId());
        }

        // Cargar las actividades asociadas al proyecto
        List<Actividad> actividades = actividadRepository.findByProyectoId(proyectoId);
        
        // Buscar avances existentes de actividades para la vigencia y trimestre indicados
        List<AvanceActividad> avancesExistentes = avanceActividadRepository
                .findByActividadProyectoIdAndVigenciaAndTrimestre(proyectoId, vigencia, trimestre);

        Map<Long, AvanceActividad> mapaAvances = avancesExistentes.stream()
                .collect(Collectors.toMap(a -> a.getActividad().getId(), a -> a));

        List<ActividadAvanceDTO> actividadesDTO = actividades.stream().map(act -> {
            AvanceActividad av = mapaAvances.get(act.getId());
            ActividadAvanceDTO dto = new ActividadAvanceDTO();
            dto.setActividadId(act.getId());
            dto.setDescripcion(act.getDescripcion());
            if (av != null) {
                dto.setAvanceFisico(av.getAvanceFisico());
                dto.setObservaciones(av.getObservaciones());
            }
            return dto;
        }).collect(Collectors.toList());

        form.setActividades(actividadesDTO);
        return form;
    }

    @Transactional
    public void guardarReporteTrimestralCompleto(ReporteTrimestralProyectoFormDTO form) {
        // 1. Guardar avance de la meta del proyecto si se proporcionó valor
        if (form.getMetaProductoId() != null && form.getAvanceLogradoMeta() != null) {
            registrarAvance(
                form.getMetaProductoId(),
                form.getTrimestre(),
                form.getVigencia(),
                form.getAvanceLogradoMeta(),
                form.getObservacionesMeta()
            );
        }

        // 2. Guardar o actualizar avance individual de cada actividad
        if (form.getActividades() != null) {
            for (ActividadAvanceDTO actDTO : form.getActividades()) {
                if (actDTO.getActividadId() == null) continue;

                AvanceActividad avance = avanceActividadRepository
                        .findByActividadIdAndVigenciaAndTrimestre(actDTO.getActividadId(), form.getVigencia(), form.getTrimestre())
                        .orElseGet(() -> {
                            AvanceActividad nuevo = new AvanceActividad();
                            Actividad act = actividadRepository.getReferenceById(actDTO.getActividadId());
                            nuevo.setActividad(act);
                            nuevo.setVigencia(form.getVigencia());
                            nuevo.setTrimestre(form.getTrimestre());
                            return nuevo;
                        });

                avance.setAvanceFisico(actDTO.getAvanceFisico());
                avance.setObservaciones(actDTO.getObservaciones());
                avanceActividadRepository.save(avance);
            }
        }
    }

}