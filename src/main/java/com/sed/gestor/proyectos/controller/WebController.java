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
    public String index(@RequestParam (required = false) Long proyectoId,
                        @RequestParam (defaultValue = "2026") Integer vigencia,
                        Model model){
        List<Proyecto> proyectos = proyectoRepository.findAll();
        if (proyectos.isEmpty()) {
            return "index";
        }
        
        Proyecto proyectoSeleccionado = (proyectoId != null)
            ? proyectoRepository.findById(proyectoId).orElse(proyectos.get(0))
            : proyectos.get(0);
        
        DashboardProyectoDTO dashboard = metaService.obtenerResumenDashboard(proyectoSeleccionado.getId(), vigencia);

        List<Actividad> actividades = actividadRepository.findByProyectoId(proyectoSeleccionado.getId());
        BigDecimal totalPropios = BigDecimal.ZERO;
        BigDecimal totalSgp = BigDecimal.ZERO;

        for(Actividad act : actividades){
            totalPropios = totalPropios.add(presupuestoService.obtenerSaldoDisponible(act.getId(),FuenteFinanciacion.PROPIOS));
            totalSgp = totalSgp.add(presupuestoService.obtenerSaldoDisponible(act.getId(), FuenteFinanciacion.SGP));
        }

        model.addAttribute("proyectos", proyectos);
        model.addAttribute("proyecto", proyectoSeleccionado);
        model.addAttribute("dashboard", dashboard);
        model.addAttribute("vigencia", vigencia);
        model.addAttribute("totalPropios", totalPropios);
        model.addAttribute("totalSgp", totalSgp);

        return "index";
    }   

    /**
     * Vista de Seguimiento Presupuestal
     */
    @GetMapping("/presupuesto")
    public String verPresupuesto(@RequestParam (required = false) Long proyectoId, Model model){
        List<Proyecto> proyectos = proyectoRepository.findAll();

        model.addAttribute("tiposMovimiento", TipoMovimiento.values());
        model.addAttribute("fuentes", FuenteFinanciacion.values());
        model.addAttribute("proyectos", proyectos);

        if (!proyectos.isEmpty()) {
            Proyecto proyectoSeleccionado = (proyectoId != null)
            ? proyectoRepository.findById(proyectoId).orElse(proyectos.get(0))
            : proyectos.get(0);
        
        List<PresupuestoResumenDTO> resumen = presupuestoService.obtenerResumenPresupuestal(proyectoSeleccionado.getId());
        List<Actividad> actividades = actividadRepository.findByProyectoId(proyectoSeleccionado.getId());

            model.addAttribute("proyecto", proyectoSeleccionado);
            model.addAttribute("resumenPresupuestal", resumen);
            model.addAttribute("actividades", actividades);
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

    @PostMapping("/actividades/editar")
    public String editarActividad(@RequestParam Long actividadId, 
                              @RequestParam String nombre, 
                              @RequestParam(required = false) String descripcion) {
    proyectoService.editarActividad(actividadId, nombre, descripcion);
    return "redirect:/proyectos";
}

    @PostMapping("/actividades/eliminar/{id}")
    public String eliminarActividad(@PathVariable Long id) {
        proyectoService.eliminarActividad(id);
    return "redirect:/proyectos";
}

}