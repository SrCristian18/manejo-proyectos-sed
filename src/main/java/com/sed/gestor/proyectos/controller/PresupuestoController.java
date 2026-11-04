package com.sed.gestor.proyectos.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sed.gestor.proyectos.dto.MovimientoRequest;
import com.sed.gestor.proyectos.dto.SaldoResponse;
import com.sed.gestor.proyectos.model.Actividad;
import com.sed.gestor.proyectos.model.DetalleMovimiento;
import com.sed.gestor.proyectos.model.DocumentoSoporte;
import com.sed.gestor.proyectos.model.MovimientoPresupuestal;
import com.sed.gestor.proyectos.model.Proyecto;
import com.sed.gestor.proyectos.model.RubroGasto;
import com.sed.gestor.proyectos.model.enums.FuenteFinanciacion;
import com.sed.gestor.proyectos.model.enums.TipoMovimiento;
import com.sed.gestor.proyectos.repository.ActividadRepository;
import com.sed.gestor.proyectos.repository.DocumentoSoporteRepository;
import com.sed.gestor.proyectos.repository.ProyectoRepository;
import com.sed.gestor.proyectos.repository.RubroGastoRepository;
import com.sed.gestor.proyectos.service.PresupuestoService;

@RestController 
@RequestMapping ("/api/presupuesto")
public class PresupuestoController {
    private final PresupuestoService presupuestoService;
    private final ProyectoRepository proyectoRepository;
    private final RubroGastoRepository rubroGastoRepository;
    private final ActividadRepository actividadRepository;
    private final DocumentoSoporteRepository documentoSoporteRepository;
    
    public PresupuestoController(PresupuestoService presupuestoService, ProyectoRepository proyectoRepository,
            ActividadRepository actividadRepository,
            RubroGastoRepository rubroGastoRepository,
            DocumentoSoporteRepository documentoSoporteRepository) {
        this.presupuestoService = presupuestoService;
        this.proyectoRepository = proyectoRepository;
        this.actividadRepository = actividadRepository;
        this.rubroGastoRepository = rubroGastoRepository;
        this.documentoSoporteRepository = documentoSoporteRepository;
    }

    @GetMapping ("/rubros")
    public ResponseEntity<List<RubroGasto>> obtenerRubrosPorActividad(@RequestParam Long actividadId){
        List<RubroGasto> rubros = rubroGastoRepository.findByActividadId(actividadId);
        return ResponseEntity.ok(rubros);
    }

    @PostMapping("/rubros")
    public ResponseEntity<RubroGasto> agregarRubro(@RequestBody Map<String, Object> payload) {
        Long actividadId = Long.valueOf(payload.get("actividadId").toString());
        String concepto = payload.get("concepto").toString();
        BigDecimal monto = new BigDecimal(payload.get("monto").toString());

        Actividad actividad = actividadRepository.findById(actividadId)
                .orElseThrow(() -> new IllegalArgumentException("Actividad no encontrada"));

        RubroGasto nuevoRubro = new RubroGasto(concepto, monto, actividad);
        RubroGasto guardado = rubroGastoRepository.save(nuevoRubro);

        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @GetMapping ("/saldo")
    public ResponseEntity<SaldoResponse> consultarSaldo(
        @RequestParam Long actividadId,
        @RequestParam FuenteFinanciacion fuente){
        
        BigDecimal saldo = presupuestoService.obtenerSaldoDisponible(actividadId, fuente);
        return ResponseEntity.ok(new SaldoResponse(actividadId, fuente, saldo));
        }

    
    @PostMapping ("/movimiento")
    public ResponseEntity<Map<String, Object>> registrarMovimiento(@RequestBody MovimientoRequest request) {
        Proyecto proyecto = proyectoRepository.findById(request.getProyectoId())
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado con ID: " + request.getProyectoId()));

        MovimientoPresupuestal movimiento = new MovimientoPresupuestal();
        movimiento.setProyecto(proyecto);
        movimiento.setTipoMovimiento(request.getTipoMovimiento());
        movimiento.setNumeroDecreto(request.getNumeroDecreto());
        movimiento.setFechaDecreto(request.getFechaDecreto());
        movimiento.setObservaciones(request.getObservaciones());

        request.getDetalles().forEach(d -> {
            Actividad actividad = actividadRepository.findById(d.getActividadId())
                    .orElseThrow(() -> new IllegalArgumentException("Actividad no encontrada con ID: " + d.getActividadId()));

            DetalleMovimiento detalle = new DetalleMovimiento();
            detalle.setActividad(actividad);
            detalle.setFuente(d.getFuente());
            detalle.setMonto(d.getMonto());
            detalle.setNumeroCdp(d.getNumeroCdp());
            detalle.setNumeroRp(d.getNumeroRp());

            movimiento.addDetalle(detalle);
    });
        MovimientoPresupuestal guardado = presupuestoService.registrarMovimiento(movimiento);

        // Respuesta limpia para evitar serializar entidades compuestas
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("mensaje", "Movimiento registrado exitosamente");
        response.put("id", guardado.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
   @PostMapping(value = "/movimiento-soporte", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> registrarMovimientoConSoporte(
            @RequestParam("proyectoId") Long proyectoId,
            @RequestParam("tipoMovimiento") TipoMovimiento tipoMovimiento,
            @RequestParam("numeroDecreto") String numeroDecreto,
            @RequestParam("actividadId") Long actividadId,
            @RequestParam("fuente") FuenteFinanciacion fuente,
            @RequestParam("monto") BigDecimal monto,
            @RequestParam(value = "archivoSoporte", required = false) MultipartFile archivoSoporte) {

        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));

        MovimientoPresupuestal movimiento = new MovimientoPresupuestal();
        movimiento.setProyecto(proyecto);
        movimiento.setTipoMovimiento(tipoMovimiento);
        movimiento.setNumeroDecreto(numeroDecreto);
        movimiento.setFechaRegistro(LocalDateTime.now());

        Actividad actividad = actividadRepository.findById(actividadId)
                .orElseThrow(() -> new IllegalArgumentException("Actividad no encontrada"));

        DetalleMovimiento detalle = new DetalleMovimiento();
        detalle.setActividad(actividad);
        detalle.setFuente(fuente);
        detalle.setMonto(monto);
        movimiento.addDetalle(detalle);

        MovimientoPresupuestal guardado = presupuestoService.registrarMovimiento(movimiento);

        // Guardar documento anexo si fue proporcionado
        if (archivoSoporte != null && !archivoSoporte.isEmpty()) {
            String nombreOriginal = archivoSoporte.getOriginalFilename();
            String rutaAlmacenamiento = "/uploads/" + System.currentTimeMillis() + "_" + nombreOriginal;
            
            DocumentoSoporte doc = new DocumentoSoporte();
            doc.setNombreArchivo(nombreOriginal);
            doc.setRutaArchivo(rutaAlmacenamiento);
            doc.setTipoContenido(archivoSoporte.getContentType());
            doc.setFechaCarga(LocalDateTime.now());
            doc.setMovimiento(guardado);

            documentoSoporteRepository.save(doc);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("mensaje", "Movimiento y soporte registrados correctamente");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
