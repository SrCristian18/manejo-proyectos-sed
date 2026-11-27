package com.sed.gestor.proyectos.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sed.gestor.proyectos.dto.ProyectoDTO;
import com.sed.gestor.proyectos.model.Actividad;
import com.sed.gestor.proyectos.model.MetaProducto;
import com.sed.gestor.proyectos.model.Proyecto;
import com.sed.gestor.proyectos.model.enums.FuenteFinanciacion;
import com.sed.gestor.proyectos.repository.ProyectoRepository;
import com.sed.gestor.proyectos.repository.RubroGastoRepository;

import jakarta.persistence.EntityNotFoundException;

import com.sed.gestor.proyectos.repository.ActividadRepository;
import com.sed.gestor.proyectos.repository.DetalleMovimientoRepository;
import com.sed.gestor.proyectos.repository.MetaProductoRepository;

@Service 
public class ProyectoService {
    private final ProyectoRepository proyectoRepository;
    private final MetaProductoRepository metaProductoRepository;
    private final ActividadRepository actividadRepository;
    private final DetalleMovimientoRepository detalleMovimientoRepository;
    private final RubroGastoRepository rubroGastoRepository;

    //Lecturas generales

    @Autowired 
    public ProyectoService(ProyectoRepository proyectoRepository, MetaProductoRepository metaProductoRepository,
            ActividadRepository actividadRepository, DetalleMovimientoRepository detalleMovimientoRepository,
            RubroGastoRepository rubroGastoRepository) {
        this.proyectoRepository = proyectoRepository;
        this.metaProductoRepository = metaProductoRepository;
        this.actividadRepository = actividadRepository;
        this.detalleMovimientoRepository = detalleMovimientoRepository;
        this.rubroGastoRepository = rubroGastoRepository;
    }

    @Transactional (readOnly = true)
    public List<Proyecto> obtenerTodos(){
        return proyectoRepository.findAll();
    }

    @Transactional (readOnly = true)
    public List<Proyecto> obtenerPorVigencia(Integer vigencia){
        if (vigencia == null){
            return proyectoRepository.findAll();
        }
        return proyectoRepository.findActivosEnVigencia(vigencia);
    }

    @Transactional (readOnly = true)
    public Proyecto obtenerPorId(Long id){
        return proyectoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado con ID: "+ id));
    }

    //Proyectos
    @Transactional
    public Proyecto guardarOActualizar(ProyectoDTO dto) {
        if (dto.getCodigoBpin() == null || dto.getCodigoBpin().isBlank()) {
            throw new IllegalArgumentException("El código BPIN es obligatorio.");
        }
        if (dto.getVigencia() == null) {
            throw new IllegalArgumentException("La vigencia inicial es obligatoria.");
        }

        String bpinLimpio = dto.getCodigoBpin().trim();
        proyectoRepository.findByCodigoBpin(bpinLimpio).ifPresent(existente -> {
            if (dto.getId() == null || !existente.getId().equals(dto.getId())) {
                throw new IllegalArgumentException("Ya existe un proyecto registrado con el código BPIN: " + bpinLimpio);
            }
        });

        MetaProducto metaProducto = null;
        if (dto.getMetaProductoId() != null) {
            metaProducto = metaProductoRepository.findById(dto.getMetaProductoId())
                    .orElseThrow(() -> new EntityNotFoundException("Meta Producto no encontrada con ID: " + dto.getMetaProductoId()));
        }

        Integer vigenciaFinCalculada = (dto.getVigenciaFin() != null)
                ? dto.getVigenciaFin()
                : Proyecto.resolverVigenciaFin(dto.getVigencia(), metaProducto);

        if (vigenciaFinCalculada < dto.getVigencia()) {
            throw new IllegalArgumentException("La vigencia de cierre no puede ser menor a la vigencia inicial.");
        }

        Proyecto proyecto = (dto.getId() != null) ? obtenerPorId(dto.getId()) : new Proyecto();
        proyecto.setCodigoBpin(bpinLimpio);
        proyecto.setNombre(dto.getNombre().trim());
        proyecto.setVigencia(dto.getVigencia());
        proyecto.setVigenciaFin(vigenciaFinCalculada);
        proyecto.setMetaProducto(metaProducto);

        return proyectoRepository.save(proyecto);
    }

    @Transactional 
    public Proyecto guardarDesdeDTO(ProyectoDTO dto){
        Proyecto proyecto;
        if(dto.getId() != null){
            proyecto = obtenerPorId(dto.getId());
        }else{
            proyecto = new Proyecto();
        }
        proyecto.setCodigoBpin(dto.getCodigoBpin());
        proyecto.setNombre(dto.getNombre());
        proyecto.setVigencia(dto.getVigencia());

        if(dto.getMetaProductoId() != null){
            MetaProducto metaProducto = metaProductoRepository.findById(dto.getMetaProductoId())
                .orElseThrow(() -> new IllegalArgumentException("Meta producto no encontrada con ID: "+ dto.getMetaProductoId()));
            proyecto.setMetaProducto(metaProducto);
        }else{
            proyecto.setMetaProducto(null);
        }
        return proyectoRepository.save(proyecto);
    }
   
    @Transactional
    public Proyecto guardar(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }

    @Transactional
    public void eliminar(Long id) {
        if(!proyectoRepository.existsById(id)){
            throw new EntityNotFoundException("Proyecto no encontrado con ID: "+ id);
        }
        proyectoRepository.deleteById(id);
    }

    //Actividades
    @Transactional
    public Actividad agregarActividad(Long proyectoId, String nombre, BigDecimal presupuestoInicial, FuenteFinanciacion fuente, String descripcion) {
        Proyecto proyecto = obtenerPorId(proyectoId);
        Actividad actividad = new Actividad(nombre, presupuestoInicial, fuente, descripcion, proyecto);
        proyecto.addActividad(actividad);
        return actividadRepository.save(actividad);
    }

    @Transactional
    public Actividad editarActividad(Long actividadId, String nombre, BigDecimal presupuestoInicial,
                                     FuenteFinanciacion fuente, String descripcion) {
        Actividad actividad = actividadRepository.findById(actividadId)
                .orElseThrow(() -> new EntityNotFoundException("Actividad no encontrada con ID: " + actividadId));

        boolean tieneMovimientos = !detalleMovimientoRepository.findByActividadId(actividadId).isEmpty();
        boolean tieneRubros = !rubroGastoRepository.findByActividadId(actividadId).isEmpty();

        // Proteger cambio de fuente si ya existen movimientos o rubros asociados
        if ((tieneMovimientos || tieneRubros) && fuente != null && actividad.getFuente() != fuente) {
            throw new IllegalStateException(
                    "No se puede cambiar la fuente de financiación de la actividad porque ya tiene rubros o movimientos presupuestales registrados. Use la Operación #8 en Seguimiento Presupuestal.");
        }

        // Proteger reducción del presupuesto inicial por debajo de lo ya distribuido en rubros
        if (presupuestoInicial != null && tieneRubros) {
            BigDecimal totalRubros = rubroGastoRepository.findByActividadId(actividadId).stream()
                    .map(r -> r.getMontoAsignado() != null ? r.getMontoAsignado() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (presupuestoInicial.compareTo(totalRubros) < 0) {
                throw new IllegalStateException(
                        String.format("El presupuesto inicial ($%,.2f) no puede ser menor al total ya distribuido en rubros ($%,.2f).",
                                presupuestoInicial, totalRubros));
            }
        }

        actividad.setNombre(nombre);
        if (presupuestoInicial != null) {
            actividad.setPresupuestoInicial(presupuestoInicial);
        }
        if (fuente != null) {
            actividad.setFuente(fuente);
        }
        actividad.setDescripcion(descripcion);
        return actividadRepository.save(actividad);
    }

    @Transactional
    public void eliminarActividad(Long actividadId) {
        if (!actividadRepository.existsById(actividadId)) {
            throw new EntityNotFoundException("Actividad no encontrada con ID: " + actividadId);
        }
        actividadRepository.deleteById(actividadId);
    }
}
