package com.sed.gestor.proyectos.controller;

import com.sed.gestor.proyectos.dto.ActividadResumenDTO;
import com.sed.gestor.proyectos.dto.DashboardProyectoDTO;
import com.sed.gestor.proyectos.dto.MetaEstrategicoResumenDTO;
import com.sed.gestor.proyectos.dto.MetaProductoDTO;
import com.sed.gestor.proyectos.dto.MetricasFuenteDTO;
import com.sed.gestor.proyectos.dto.PresupuestoResumenDTO;
import com.sed.gestor.proyectos.dto.ProyectoDTO;
import com.sed.gestor.proyectos.dto.ResumenProyectoPresupuestoDTO;
import com.sed.gestor.proyectos.dto.RubroEjecucionDTO;
import com.sed.gestor.proyectos.model.Actividad;
import com.sed.gestor.proyectos.model.DetalleMovimiento;
import com.sed.gestor.proyectos.model.LineaEstrategica;
import com.sed.gestor.proyectos.model.MetaProducto;
import com.sed.gestor.proyectos.model.MovimientoPresupuestal;
import com.sed.gestor.proyectos.model.Proyecto;
import com.sed.gestor.proyectos.model.RubroGasto;
import com.sed.gestor.proyectos.model.enums.FuenteFinanciacion;
import com.sed.gestor.proyectos.model.enums.OperacionActualizacion;
import com.sed.gestor.proyectos.repository.ActividadRepository;
import com.sed.gestor.proyectos.repository.MovimientoPresupuestalRepository;
import com.sed.gestor.proyectos.repository.RubroGastoRepository;
import com.sed.gestor.proyectos.service.MetaService;
import com.sed.gestor.proyectos.service.PlanDesarrolloService;
import com.sed.gestor.proyectos.service.PresupuestoService;
import com.sed.gestor.proyectos.service.ProyectoService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class WebController {

    private final ActividadRepository actividadRepository;
    private final RubroGastoRepository rubroGastoRepository;
    private final MovimientoPresupuestalRepository movimientoRepository;
    private final PresupuestoService presupuestoService;
    private final MetaService metaService;
    private final ProyectoService proyectoService;
    private final PlanDesarrolloService planDesarrolloService;

    public WebController(ActividadRepository actividadRepository,
                         PresupuestoService presupuestoService,
                         MetaService metaService,
                         ProyectoService proyectoService, RubroGastoRepository rubroGastoRepository,
                         MovimientoPresupuestalRepository movimientoRepository, PlanDesarrolloService planDesarrolloService) {
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
            List<PresupuestoResumenDTO> resumenPresupuestal = presupuestoService.obtenerResumenPresupuestal(proyectoSeleccionado.getId());
            ResumenProyectoPresupuestoDTO resumenProyecto = presupuestoService.obtenerResumenGlobalProyecto(resumenPresupuestal);
            List<MetricasFuenteDTO> metricasFuentes = presupuestoService.obtenerMetricasPorFuenteProyecto(resumenPresupuestal);

            BigDecimal sumaRecursosPropios = BigDecimal.ZERO;
            BigDecimal sumaTotalGeneral = BigDecimal.ZERO;

            for (MetricasFuenteDTO metrica : metricasFuentes) {
                if(metrica.isEsRecursoPropio()){
                    sumaRecursosPropios = sumaRecursosPropios.add(metrica.getSaldoDisponible());
                }
                sumaTotalGeneral = sumaTotalGeneral.add(metrica.getSaldoDisponible());
            }

            model.addAttribute("proyecto", proyectoSeleccionado);
            model.addAttribute("dashboard", dashboard);
            model.addAttribute("resumenProyecto", resumenProyecto);
            model.addAttribute("metricasFuentes", metricasFuentes);
            model.addAttribute("sumaRecursosPropios", sumaRecursosPropios);
            model.addAttribute("totalGeneral", sumaTotalGeneral);
        } else {
            model.addAttribute("proyecto", null);
            model.addAttribute("dashboard", new DashboardProyectoDTO());
            model.addAttribute("resumenProyecto", new ResumenProyectoPresupuestoDTO());
            model.addAttribute("metricasFuentes", new ArrayList<MetricasFuenteDTO>());
            model.addAttribute("sumaRecursosPropios", BigDecimal.ZERO);
            model.addAttribute("totalGeneral", BigDecimal.ZERO);
        }
        return "index";
    }

    
    //Vista de Seguimiento Presupuestal
    @GetMapping("/presupuesto")
    public String verPresupuesto(@RequestParam(required = false) Long proyectoId,
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

            List<PresupuestoResumenDTO> resumen = presupuestoService.obtenerResumenPresupuestal(proyectoSeleccionado.getId());
            ResumenProyectoPresupuestoDTO resumenProyecto = presupuestoService.obtenerResumenGlobalProyecto(resumen);
            List<Actividad> actividades = actividadRepository.findByProyectoId(proyectoSeleccionado.getId());

            model.addAttribute("proyecto", proyectoSeleccionado);
            model.addAttribute("resumenPresupuestal", resumen);
            model.addAttribute("resumenProyecto", resumenProyecto);
            model.addAttribute("actividades", actividades);
            model.addAttribute("fuentes", FuenteFinanciacion.values());
            model.addAttribute("operacionesActualizacion", OperacionActualizacion.values());
        } else {
            model.addAttribute("proyecto", null);
            model.addAttribute("resumenPresupuestal", new ArrayList<>());
            model.addAttribute("resumenProyecto", new ResumenProyectoPresupuestoDTO());
            model.addAttribute("actividades", new ArrayList<>());
            model.addAttribute("fuentes", FuenteFinanciacion.values());
            model.addAttribute("operacionesActualizacion", OperacionActualizacion.values());
        }
        return "presupuesto";
    }

    //% de ejecucion
    @GetMapping("/presupuesto/actividad/{actividadId}/ejecucion-rubros")
    public String verDetalleEjecucionRubros(@PathVariable Long actividadId,
                                            @RequestParam(required = false) Long proyectoId,
                                            @RequestParam(defaultValue = "2026") Integer vigencia,
                                            Model model) {
        Actividad actividad = actividadRepository.findById(actividadId)
                .orElseThrow(() -> new IllegalArgumentException("Actividad no encontrada"));

        List<RubroEjecucionDTO> rubrosResumen = presupuestoService.obtenerDetalleRubrosActividad(actividadId);
        List<DetalleMovimiento> ejecuciones = presupuestoService.obtenerHistorialEjecucionesActividad(actividadId);

        BigDecimal totalAsignado = rubrosResumen.stream()
                .map(RubroEjecucionDTO::getMontoAsignado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalEjecutado = rubrosResumen.stream()
                .map(RubroEjecucionDTO::getMontoEjecutado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalDisponible = totalAsignado.subtract(totalEjecutado);

        model.addAttribute("actividad", actividad);
        model.addAttribute("proyecto", actividad.getProyecto());
        model.addAttribute("vigencia", vigencia);
        model.addAttribute("rubrosResumen", rubrosResumen);
        model.addAttribute("ejecuciones", ejecuciones);
        model.addAttribute("totalAsignado", totalAsignado);
        model.addAttribute("totalEjecutado", totalEjecutado);
        model.addAttribute("totalDisponible", totalDisponible);

        return "ejecucion_rubros";
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
            DashboardProyectoDTO resumen = metaService.obtenerResumenDashboard(proyectoSeleccionado.getId(), vigencia);
            
            List<PresupuestoResumenDTO> resumenPresupuestal = presupuestoService.obtenerResumenPresupuestal(proyectoSeleccionado.getId());
            ResumenProyectoPresupuestoDTO resumenProyecto = presupuestoService.obtenerResumenGlobalProyecto(resumenPresupuestal);

            //Resumen metas (estrategico)
            MetaEstrategicoResumenDTO resumenEstrategico = metaService.obtenerResumenEstrategico(metaProductoAsociada, vigencia);

            // Cargar actividades del proyecto transformadas con métricas acumuladas (% avance)
            List<ActividadResumenDTO> actividadesResumen = metaService.obtenerResumenActividadesProyecto(proyectoSeleccionado.getId(), vigencia);

            model.addAttribute("proyecto", proyectoSeleccionado);
            model.addAttribute("metaProducto", metaProductoAsociada);
            model.addAttribute("resumenProyecto", resumenProyecto);
            model.addAttribute("actividadesResumen", actividadesResumen);
            model.addAttribute("resumenEstrategico", resumenEstrategico);
            model.addAttribute("resumenMetas", resumen != null ? resumen.getResumenMetas() : new ArrayList<>());
        } else {
            model.addAttribute("proyecto", null);
            model.addAttribute("metaProducto", null);
            model.addAttribute("resumenProyecto", new ResumenProyectoPresupuestoDTO());
            model.addAttribute("actividadesResumen", new ArrayList<>());
            model.addAttribute("resumenEstrategico", new ArrayList<>());
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

    //Agregar metas anuales
    @PostMapping("/metas/meta-anual/guardar")
    public String guardarMetaAnual(@RequestParam Long metaProductoId,
                                   @RequestParam Long proyectoId,
                                   @RequestParam Integer vigencia,
                                   @RequestParam(required = false) BigDecimal valorProgramado,
                                   @RequestParam(defaultValue = "false") boolean noProgramada,
                                   RedirectAttributes redirectAttributes) {
        metaService.guardarMetaAnual(metaProductoId, vigencia, valorProgramado, noProgramada);
        redirectAttributes.addFlashAttribute("mensajeExito",
                noProgramada ? "Vigencia marcada como No Programada (NP)." : "Meta anual guardada correctamente.");
        return "redirect:/metas?proyectoId=" + proyectoId + "&vigencia=" + vigencia;
    }

    //agregar metas anuales de actividad
    @PostMapping("/metas/actividad-meta-anual/guardar")
    public String guardarActividadMetaAnual(@RequestParam Long actividadId,
                                            @RequestParam Long proyectoId,
                                            @RequestParam Integer vigencia,
                                            @RequestParam(required = false) BigDecimal valorProgramado,
                                            @RequestParam(defaultValue = "false") boolean noProgramada,
                                            RedirectAttributes redirectAttributes) {
        metaService.guardarActividadMetaAnual(actividadId, vigencia, valorProgramado, noProgramada);
        redirectAttributes.addFlashAttribute("mensajeExito",
                noProgramada ? "Actividad marcada como No Programada (NP) para la vigencia." : "Meta anual de la actividad guardada correctamente.");
        return "redirect:/metas?proyectoId=" + proyectoId + "&vigencia=" + vigencia;
    }

    //Procesar Formulario Web de Avance de Metas
    @PostMapping("/metas/reportar")
    public String reportarAvanceEstrategico(@RequestParam Long metaProductoId,
                                            @RequestParam Long proyectoId,
                                            @RequestParam Integer vigencia,
                                            @RequestParam Integer mes,
                                            @RequestParam BigDecimal valorLogrado,
                                            @RequestParam(required = false) String observaciones,
                                            RedirectAttributes redirectAttributes) {
        metaService.registrarAvanceEstrategico(metaProductoId, vigencia, mes, valorLogrado, observaciones);
        redirectAttributes.addFlashAttribute("mensajeExito", "Avance estratégico registrado correctamente.");
        return "redirect:/metas?proyectoId=" + proyectoId + "&vigencia=" + vigencia;
    }

    @PostMapping("/metas/actividades/reportar")
    public String reportarAvancesActividades(@RequestParam Long proyectoId,
                                             @RequestParam Integer vigencia,
                                             @RequestParam Integer mes,
                                             @RequestParam(required = false) List<Long> actividadIds,
                                             @RequestParam(required = false) List<BigDecimal> avancesFisicos,
                                             @RequestParam(required = false) List<String> observacionesActividades,
                                             RedirectAttributes redirectAttributes) {
        metaService.registrarAvancesActividades(vigencia, mes, actividadIds, avancesFisicos, observacionesActividades);
        redirectAttributes.addFlashAttribute("mensajeExito", "Avance de actividades registrado correctamente.");
        return "redirect:/metas?proyectoId=" + proyectoId + "&vigencia=" + vigencia;
    }

    /* 
    @PostMapping("/metas/reportar")
    public String reportarAvanceMeta(@RequestParam Long metaProductoId,
                                     @RequestParam Integer mes,
                                     @RequestParam Integer vigencia,
                                     @RequestParam BigDecimal valorLogrado,
                                     @RequestParam (required = false) String observaciones,
                                     @RequestParam (required = false) Long proyectoId,
                                     @RequestParam (required = false) List<Long> actividadIds,
                                     @RequestParam (required = false) List<BigDecimal> avancesFisicos,
                                     @RequestParam (required = false) List<String> observacionesActividades,
                                     RedirectAttributes redirectAttributes) {
        
        // 1. Guardar Avance de la Meta Producto
        metaService.registrarAvance(metaProductoId, mes, vigencia, valorLogrado, observaciones);

        // 2. Guardar Avances de cada Actividad si se enviaron valores
        if (actividadIds != null && avancesFisicos != null) {
            for (int i = 0; i < actividadIds.size(); i++) {
                Long actId = actividadIds.get(i);
                BigDecimal avanceVal = (i < avancesFisicos.size()) ? avancesFisicos.get(i) : null;
                String obsAct = (observacionesActividades != null && i < observacionesActividades.size()) 
                        ? observacionesActividades.get(i) : null;

                if (actId != null && avanceVal != null) {
                    metaService.registrarAvanceActividad(actId, mes, vigencia, avanceVal, obsAct);
                }
            }
        }
        redirectAttributes.addFlashAttribute("mensajeExito", "Avances reportados correctamente para el mes " + mes + ".");
        return "redirect:/metas?proyectoId=" + proyectoId + "&vigencia=" + vigencia;
    }
    */

    //Proyectos
    @GetMapping ("/proyectos")
    public String listarProyectos(@RequestParam (defaultValue = "2026") Integer vigencia, Model model){
        List<LineaEstrategica> lineas = planDesarrolloService.obtenerLineaEstrategicas();
        List<Proyecto> proyectos = proyectoService.obtenerPorVigencia(vigencia);
        List<MetaProducto> metasProducto = metaService.obtenerTodasLasMetasProducto();

        Map<Long, ResumenProyectoPresupuestoDTO> presupuestosPorProyecto = new HashMap<>();
        if(proyectos != null){
                for(Proyecto p : proyectos){
                List<PresupuestoResumenDTO> resAct = presupuestoService.obtenerResumenPresupuestal(p.getId());
                presupuestosPorProyecto.put(p.getId(), presupuestoService.obtenerResumenGlobalProyecto(resAct));
            }
        }
        model.addAttribute("lineasEstrategicas", lineas);
        model.addAttribute("proyectos", proyectos != null ? proyectos : new ArrayList<>());
        model.addAttribute("presupuestosPorProyecto", presupuestosPorProyecto);
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
    
    //eliminar este metodo
/* 
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
*/
    @PostMapping ("/actividades/{actividadId}/rubros")
    public String agregarRubroGasto(@PathVariable Long actividadId,
            @RequestParam String concepto,
            @RequestParam FuenteFinanciacion fuente,
            @RequestParam BigDecimal montoAsignado,
            @RequestParam Long proyectoId){
        Actividad actividad = actividadRepository.findById(actividadId)
                .orElseThrow(() -> new IllegalArgumentException("Actividad no encontrada"));
        RubroGasto rubro = new RubroGasto(concepto, fuente, montoAsignado, actividad);
        rubroGastoRepository.save(rubro);

        return "redirect:/presupuesto?proyectoId=" + proyectoId;
    }

    @GetMapping ("/api/actividades/{actividadId}/rubros")
    @ResponseBody
    public List<RubroGasto> obtenerRubrosActividad(@PathVariable Long actividadId){
        return rubroGastoRepository.findByActividadId(actividadId);
    }
}