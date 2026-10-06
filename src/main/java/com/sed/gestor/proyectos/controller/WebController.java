package com.sed.gestor.proyectos.controller;

import com.sed.gestor.proyectos.dto.DashboardProyectoDTO;
import com.sed.gestor.proyectos.dto.PresupuestoResumenDTO;
import com.sed.gestor.proyectos.dto.ProyectoDTO;
import com.sed.gestor.proyectos.model.Actividad;
import com.sed.gestor.proyectos.model.MetaProducto;
import com.sed.gestor.proyectos.model.Proyecto;
import com.sed.gestor.proyectos.model.enums.FuenteFinanciacion;
import com.sed.gestor.proyectos.model.enums.TipoMovimiento;
import com.sed.gestor.proyectos.repository.ActividadRepository;
import com.sed.gestor.proyectos.repository.MetaProductoRepository;
import com.sed.gestor.proyectos.repository.ProyectoRepository;
import com.sed.gestor.proyectos.service.MetaService;
import com.sed.gestor.proyectos.service.PresupuestoService;
import com.sed.gestor.proyectos.service.ProyectoService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class WebController {

    private final ProyectoRepository proyectoRepository;
    private final ActividadRepository actividadRepository;
    private final MetaProductoRepository metaProductoRepository;
    private final PresupuestoService presupuestoService;
    private final MetaService metaService;
    private final ProyectoService proyectoService;

    public WebController(ProyectoRepository proyectoRepository,
                         ActividadRepository actividadRepository,
                         MetaProductoRepository metaProductoRepository,
                         PresupuestoService presupuestoService,
                         MetaService metaService,
                        ProyectoService proyectoService) {
        this.proyectoRepository = proyectoRepository;
        this.actividadRepository = actividadRepository;
        this.metaProductoRepository = metaProductoRepository;
        this.presupuestoService = presupuestoService;
        this.metaService = metaService;
        this.proyectoService = proyectoService;
    }

    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "2026") Integer vigencia, Model model) {
        Proyecto proyecto = proyectoRepository.findAll().stream().findFirst().orElse(null);

        if (proyecto != null) {
            DashboardProyectoDTO dashboard = metaService.obtenerResumenDashboard(proyecto.getId(), vigencia);
            
            // Datos presupuestales para las tarjetas del Dashboard
            List<Actividad> actividades = actividadRepository.findByProyectoId(proyecto.getId());
            BigDecimal totalDisponiblePropios = BigDecimal.ZERO;
            BigDecimal totalDisponibleSgp = BigDecimal.ZERO;

            for (Actividad act : actividades) {
                totalDisponiblePropios = totalDisponiblePropios.add(presupuestoService.obtenerSaldoDisponible(act.getId(), FuenteFinanciacion.PROPIOS));
                totalDisponibleSgp = totalDisponibleSgp.add(presupuestoService.obtenerSaldoDisponible(act.getId(), FuenteFinanciacion.SGP));
            }

            model.addAttribute("proyecto", proyecto);
            model.addAttribute("dashboard", dashboard);
            model.addAttribute("vigencia", vigencia);
            model.addAttribute("totalPropios", totalDisponiblePropios);
            model.addAttribute("totalSgp", totalDisponibleSgp);
            model.addAttribute("totalGeneral", totalDisponiblePropios.add(totalDisponibleSgp));
        }

        return "index";
    }

    /**
     * Vista de Seguimiento Presupuestal
     */
    @GetMapping("/presupuesto")
    public String verPresupuesto(Model model) {
        Proyecto proyecto = proyectoRepository.findAll().stream().findFirst().orElse(null);

        if (proyecto != null) {
            List<PresupuestoResumenDTO> resumen = presupuestoService.obtenerResumenPresupuestal(proyecto.getId());
            List<Actividad> actividades = actividadRepository.findByProyectoId(proyecto.getId());

            model.addAttribute("proyecto", proyecto);
            model.addAttribute("resumenPresupuestal", resumen);
            model.addAttribute("actividades", actividades);
            model.addAttribute("tiposMovimiento", TipoMovimiento.values());
            model.addAttribute("fuentes", FuenteFinanciacion.values());

        }

        return "presupuesto";
    }

    /**
     * Vista de Seguimiento Físico de Metas PDD
     */
    @GetMapping("/metas")
    public String verMetas(@RequestParam(defaultValue = "2026") Integer vigencia, Model model) {
        Proyecto proyecto = proyectoRepository.findAll().stream().findFirst().orElse(null);

        if (proyecto != null) {
            List<MetaProducto> metas = metaProductoRepository.findByActividadProyectoId(proyecto.getId());
            DashboardProyectoDTO resumen = metaService.obtenerResumenDashboard(proyecto.getId(), vigencia);

            model.addAttribute("proyecto", proyecto);
            model.addAttribute("metas", metas);
            model.addAttribute("resumenMetas", resumen.getResumenMetas());
            model.addAttribute("vigencia", vigencia);
        }

        return "metas";
    }

    /**
     * Procesar Formulario Web de Avance de Metas
     */
    @PostMapping("/metas/reportar")
    public String reportarAvanceMeta(@RequestParam Long metaProductoId,
                                     @RequestParam Integer trimestre,
                                     @RequestParam Integer vigencia,
                                     @RequestParam BigDecimal valorLogrado,
                                     @RequestParam(required = false) String observaciones) {
        metaService.registrarAvance(metaProductoId, trimestre, vigencia, valorLogrado, observaciones);
        return "redirect:/metas?vigencia=" + vigencia;
    }

    @GetMapping ("/proyectos")
    public String listarProyecto(Model model){
        model.addAttribute("proyectos", proyectoService.obtenerTodos());
        model.addAttribute("nuevoProyecto", new ProyectoDTO());
        return "proyectos";
    }

    @PostMapping ("/proyectos/guardar")
    public String guardarProyecto(@ModelAttribute ProyectoDTO proyectoDTO){
        proyectoService.guardarOActualizar(proyectoDTO);
        return "redirect:/proyectos";
    }

    @PostMapping ("/proyectos/{id}/actividad")
    public String agregarActividadProyecto(@PathVariable Long id, 
        @RequestParam String nombreActividad, 
        @RequestParam (required = false) String descripcion){
            proyectoService.agregarActividad(id, nombreActividad, descripcion);
        return "redirect:/proyectos";
    }

}