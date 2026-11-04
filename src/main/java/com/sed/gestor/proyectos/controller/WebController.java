package com.sed.gestor.proyectos.controller;

import com.sed.gestor.proyectos.dto.DashboardProyectoDTO;
import com.sed.gestor.proyectos.dto.MetaProductoDTO;
import com.sed.gestor.proyectos.dto.MetricasFuenteDTO;
import com.sed.gestor.proyectos.dto.PresupuestoResumenDTO;
import com.sed.gestor.proyectos.dto.ProyectoDTO;
import com.sed.gestor.proyectos.dto.SolicitudActualizacionDTO;
import com.sed.gestor.proyectos.model.Actividad;
import com.sed.gestor.proyectos.model.LineaEstrategica;
import com.sed.gestor.proyectos.model.MetaProducto;
import com.sed.gestor.proyectos.model.MovimientoPresupuestal;
import com.sed.gestor.proyectos.model.Proyecto;
import com.sed.gestor.proyectos.model.RubroGasto;
import com.sed.gestor.proyectos.model.enums.FuenteFinanciacion;
import com.sed.gestor.proyectos.model.enums.TipoMovimiento;
import com.sed.gestor.proyectos.repository.ActividadRepository;
import com.sed.gestor.proyectos.repository.MovimientoPresupuestalRepository;
import com.sed.gestor.proyectos.repository.ProyectoRepository;
import com.sed.gestor.proyectos.repository.RubroGastoRepository;
import com.sed.gestor.proyectos.service.MetaService;
import com.sed.gestor.proyectos.service.PlanDesarrolloService;
import com.sed.gestor.proyectos.service.PresupuestoService;
import com.sed.gestor.proyectos.service.ProyectoService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class WebController {

    private final ProyectoRepository proyectoRepository;
    private final ActividadRepository actividadRepository;
    private final RubroGastoRepository rubroGastoRepository;
    private final MovimientoPresupuestalRepository movimientoRepository;
    private final PresupuestoService presupuestoService;
    private final MetaService metaService;
    private final ProyectoService proyectoService;
    private final PlanDesarrolloService planDesarrolloService;

    public WebController(ProyectoRepository proyectoRepository,
                         ActividadRepository actividadRepository,
                         PresupuestoService presupuestoService,
                         MetaService metaService,
                         ProyectoService proyectoService, RubroGastoRepository rubroGastoRepository,
                         MovimientoPresupuestalRepository movimientoRepository, PlanDesarrolloService planDesarrolloService) {
        this.proyectoRepository = proyectoRepository;
        this.actividadRepository = actividadRepository;
        this.presupuestoService = presupuestoService;
        this.metaService = metaService;
        this.proyectoService = proyectoService;
        this.rubroGastoRepository = rubroGastoRepository;
        this.movimientoRepository = movimientoRepository;
        this.planDesarrolloService = planDesarrolloService;
    }

    //Dashboard y visualización de datos
    @GetMapping("/")
    public String index(@RequestParam(required = false) Long proyectoId,
                        @RequestParam(defaultValue = "2026") Integer vigencia,
                        Model model) {

        List<Proyecto> proyectosDeVigencia = proyectoService.obtenerPorVigencia(vigencia);
        model.addAttribute("vigencia", vigencia);
        model.addAttribute("proyectos", proyectosDeVigencia);

        if (proyectosDeVigencia != null && !proyectosDeVigencia.isEmpty()) {
            Proyecto proyectoSeleccionado = null;
            if (proyectoId != null) {
                proyectoSeleccionado = proyectosDeVigencia.stream()
                        .filter(p -> p.getId().equals(proyectoId))
                        .findFirst()
                        .orElse(null);
            }

            if (proyectoSeleccionado == null) {
                proyectoSeleccionado = proyectosDeVigencia.get(0);
            }

            DashboardProyectoDTO dashboard = metaService.obtenerResumenDashboard(proyectoSeleccionado.getId(), vigencia);
            List<Actividad> actividades = actividadRepository.findByProyectoId(proyectoSeleccionado.getId());

            List<MetricasFuenteDTO> metricasFuentes = new ArrayList<>();
            BigDecimal sumaRecursosPropios = BigDecimal.ZERO;
            BigDecimal sumaTotalGeneral = BigDecimal.ZERO;

            for (FuenteFinanciacion fuente : FuenteFinanciacion.values()) {
                BigDecimal acumuladoFuente = BigDecimal.ZERO;

                for (Actividad act : actividades) {
                    acumuladoFuente = acumuladoFuente.add(presupuestoService.obtenerSaldoDisponible(act.getId(), fuente));
                }

                MetricasFuenteDTO metrica = new MetricasFuenteDTO(fuente, acumuladoFuente);
                metricasFuentes.add(metrica);

                if (fuente.isEsRecursoPropio()) {
                    sumaRecursosPropios = sumaRecursosPropios.add(acumuladoFuente);
                }
                sumaTotalGeneral = sumaTotalGeneral.add(acumuladoFuente);
            }

            model.addAttribute("proyecto", proyectoSeleccionado);
            model.addAttribute("dashboard", dashboard);
            model.addAttribute("metricasFuentes", metricasFuentes);
            model.addAttribute("sumaRecursosPropios", sumaRecursosPropios);
            model.addAttribute("totalGeneral", sumaTotalGeneral);
        } else {
            model.addAttribute("proyecto", null);
            model.addAttribute("dashboard", new DashboardProyectoDTO());
            model.addAttribute("metricasFuentes", new ArrayList<MetricasFuenteDTO>());
            model.addAttribute("sumaRecursosPropios", BigDecimal.ZERO);
            model.addAttribute("totalGeneral", BigDecimal.ZERO);
        }
        return "index";
    }

    
    //Vista de Seguimiento Presupuestal
    @GetMapping("/presupuesto")
    public String verPresupuesto(@RequestParam (required = false) Long proyectoId,
                                 @RequestParam (defaultValue = "2026") Integer vigencia, 
                                 Model model){
        List<Proyecto> proyectos = proyectoRepository.findByVigencia(vigencia);
        model.addAttribute("proyectos", proyectos);
        model.addAttribute("vigencia", vigencia);

        if (proyectos != null && !proyectos.isEmpty()) {
            Proyecto proyectoSeleccionado = null;
            if (proyectoId != null){
                proyectoSeleccionado = proyectos.stream()
                    .filter(p -> p.getId().equals(proyectoId))
                    .findFirst()
                    .orElse(null);
            }
            if (proyectoSeleccionado == null) {
                proyectoSeleccionado = proyectos.get(0);
            }

        List<PresupuestoResumenDTO> resumen = presupuestoService.obtenerResumenPresupuestal(proyectoSeleccionado.getId());
        List<Actividad> actividades = actividadRepository.findByProyectoId(proyectoSeleccionado.getId());      

            model.addAttribute("proyecto", proyectoSeleccionado);
            model.addAttribute("resumenPresupuestal", resumen);
            model.addAttribute("actividades", actividades);
            model.addAttribute("tiposMovimiento", TipoMovimiento.values());
            model.addAttribute("fuentes", FuenteFinanciacion.values());
        } else {
            model.addAttribute("proyecto", null);
            model.addAttribute("resumenPresupuestal", new ArrayList<>());
            model.addAttribute("actividades", new ArrayList<>());
            model.addAttribute("tiposMovimiento", TipoMovimiento.values());
            model.addAttribute("fuentes", FuenteFinanciacion.values());
        }
        return "presupuesto";
    }

    @GetMapping ("/presupuesto/trazabilidad")
    public String verTrazabilidad(@RequestParam (required = false) Long proyectoId,
            @RequestParam (required = false) Long actividadId,
            Model model){
        List<MovimientoPresupuestal> movimientos;

        if(actividadId != null){
            movimientos = movimientoRepository.findByDetallesActividadId(actividadId);
        }else if (proyectoId != null){
            movimientos = movimientoRepository.findByProyectoId(proyectoId);
        }else{
            movimientos = movimientoRepository.findAll();
        }

        model.addAttribute("movimientos", movimientos);
        return "trazabilidad";
    }

    //Vista de Seguimiento Físico de Metas PDD
    @GetMapping("/metas")
    public String verMetas(@RequestParam(required = false) Long proyectoId,
                           @RequestParam(defaultValue = "2026") Integer vigencia,
                           Model model) {

        List<Proyecto> proyectos = proyectoService.obtenerPorVigencia(vigencia);
        model.addAttribute("proyectos", proyectos);
        model.addAttribute("vigencia", vigencia);

        if (proyectos != null && !proyectos.isEmpty()) {
            Proyecto proyectoSeleccionado = null;
            if (proyectoId != null) {
                proyectoSeleccionado = proyectos.stream()
                        .filter(p -> p.getId().equals(proyectoId))
                        .findFirst()
                        .orElse(null);
            }
            if (proyectoSeleccionado == null) {
                proyectoSeleccionado = proyectos.get(0);
            }

            MetaProducto metaProductoAsociada = proyectoSeleccionado.getMetaProducto();
            List<Actividad> actividades = actividadRepository.findByProyectoId(proyectoSeleccionado.getId());
            DashboardProyectoDTO resumen = metaService.obtenerResumenDashboard(proyectoSeleccionado.getId(), vigencia);

            model.addAttribute("proyecto", proyectoSeleccionado);
            model.addAttribute("metaProducto", metaProductoAsociada);
            model.addAttribute("actividades", actividades);
            model.addAttribute("resumenMetas", resumen != null ? resumen.getResumenMetas() : new ArrayList<>());
        } else {
            model.addAttribute("proyecto", null);
            model.addAttribute("metaProducto", null);
            model.addAttribute("actividades", new ArrayList<>());
            model.addAttribute("resumenMetas", new ArrayList<>());
        }

        return "metas";
    }

    /* 
    @PostMapping ("/metas/crear")
    public String crearMeta(@RequestParam Long proyectoId,
        @RequestParam String tipoMeta,
        @RequestParam (required = false) String codigoPrograma,
        @RequestParam String descripcion,
        @RequestParam String unidadMedida,
        @RequestParam BigDecimal programadoAnual){

            metaService.crearMeta(proyectoId, tipoMeta, codigoPrograma, descripcion, unidadMedida, programadoAnual);
            return "redirect:/metas?proyectoId=" + proyectoId;
        }
    */

    //Procesar Formulario Web de Avance de Metas
    @PostMapping("/metas/reportar")
    public String reportarAvanceMeta(@RequestParam Long metaProductoId,
                                     @RequestParam Integer trimestre,
                                     @RequestParam Integer vigencia,
                                     @RequestParam BigDecimal valorLogrado,
                                     @RequestParam (required = false) String observaciones,
                                     @RequestParam (required = false) Long proyectoId,
                                     RedirectAttributes redirectAttributes) {
        metaService.registrarAvance(metaProductoId, trimestre, vigencia, valorLogrado, observaciones);
        redirectAttributes.addFlashAttribute("mensajeExito", "Avance registrado exitosamente.");

        String redirectUrl = "redirect:/metas?vigencia=" + vigencia;
        if(proyectoId != null){
            redirectUrl += "&proyectoId=" + proyectoId;
        }
        return redirectUrl;
    }

    //Proyectos
    @GetMapping ("/proyectos")
    public String listarProyectos(@RequestParam (defaultValue = "2026") Integer vigencia, Model model){
        List<LineaEstrategica> lineas = planDesarrolloService.obtenerLineaEstrategicas();
        List<Proyecto> proyectos = proyectoService.obtenerPorVigencia(vigencia);
        List<MetaProducto> metasProducto = metaService.obtenerTodasLasMetasProducto();

        model.addAttribute("lineasEstrategicas", lineas);
        model.addAttribute("proyectos", proyectos != null ? proyectos : new ArrayList<>());
        model.addAttribute("metasProducto", metasProducto);
        model.addAttribute("vigencia", vigencia);
        model.addAttribute("fuentes", FuenteFinanciacion.values());
        model.addAttribute("nuevoProyecto", new ProyectoDTO());
        model.addAttribute("nuevaMetaDTO", new MetaProductoDTO());

        return "proyectos";
    }

    @PostMapping ("/proyectos/guardar")
    public String guardarProyecto(@ModelAttribute ProyectoDTO proyectoDTO){
        proyectoService.guardarOActualizar(proyectoDTO);
        return "redirect:/proyectos?vigencia=" + (proyectoDTO.getVigencia() != null ? proyectoDTO.getVigencia() : 2026);
    }

    @PostMapping("/proyectos/metas-producto/guardar")
    public String guardarMetaProducto(@ModelAttribute MetaProductoDTO metaDTO, RedirectAttributes redirectAttributes) {
        metaService.guardarMetaProducto(metaDTO);
        redirectAttributes.addFlashAttribute("mensajeExito", "Meta Producto creada/actualizada.");
        return "redirect:/proyectos";
    }

    @PostMapping ("/proyectos/{id}/actividad")
    public String agregarActividad(@PathVariable Long id,
        @RequestParam String nombre,
        @RequestParam (required = false) BigDecimal presupuestoInicial,
        @RequestParam (required = false) FuenteFinanciacion fuente,
        @RequestParam (required = false) String descripcion){

        Actividad act = proyectoService.agregarActividad(id, nombre, presupuestoInicial, fuente, descripcion);
        return "redirect:/proyectos?vigencia=" + act.getProyecto().getVigencia();
    }
    
    //Actividades
    @PostMapping("/actividades/editar")
    public String editarActividad(@RequestParam Long actividadId,
        @RequestParam String nombre,
        @RequestParam (required = false) BigDecimal presupuestoInicial,
        @RequestParam (required = false) FuenteFinanciacion fuente,
        @RequestParam (required = false) String descripcion){

        Actividad act = proyectoService.editarActividad(actividadId, nombre, presupuestoInicial, fuente, descripcion);
        return "redirect:/proyectos?vigencia=" +act.getProyecto().getVigencia();
    }

    @PostMapping("/actividades/eliminar/{id}")
    public String eliminarActividad(@PathVariable Long id) {
        proyectoService.eliminarActividad(id);
        return "redirect:/proyectos";
    }

    //Costos y Rubros
    @PostMapping ("/api/proyecto/actualizar-costos")
    @ResponseBody
    public ResponseEntity<?> actualizarCostosProyecto(@RequestBody SolicitudActualizacionDTO solicitud){
    try {
        presupuestoService.procesarActualizacionProyecto(solicitud);
        return ResponseEntity.ok().body("{\"mensaje\": \"Actualización de costos procesada exitosamente\"}");
    } catch (Exception e) {
        return ResponseEntity.badRequest().body("{\"mensaje\": \"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping ("/actividades/{actividadId}/rubros")
    public String agregarRubroGasto(@PathVariable Long actividadId,
            @RequestParam String concepto,
            @RequestParam BigDecimal montoAsignado,
            @RequestParam Long proyectoId){
        Actividad actividad = actividadRepository.findById(actividadId)
                .orElseThrow(() -> new IllegalArgumentException("Actividad no encontrada"));
        RubroGasto rubro = new RubroGasto(concepto, montoAsignado, actividad);
        rubroGastoRepository.save(rubro);

        return "redirect:/presupuesto?proyectoId=" + proyectoId;
    }

    @GetMapping ("/api/actividades/{actividadId}/rubros")
    @ResponseBody
    public List<RubroGasto> obtenerRubrosActividad(@PathVariable Long actividadId){
        return rubroGastoRepository.findByActividadId(actividadId);
    }
}