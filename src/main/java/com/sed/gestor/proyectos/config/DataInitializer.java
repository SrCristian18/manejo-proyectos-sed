package com.sed.gestor.proyectos.config;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sed.gestor.proyectos.model.Actividad;
import com.sed.gestor.proyectos.model.DetalleMovimiento;
import com.sed.gestor.proyectos.model.MetaProducto;
import com.sed.gestor.proyectos.service.MetaService;
import com.sed.gestor.proyectos.model.MovimientoPresupuestal;
import com.sed.gestor.proyectos.model.Proyecto;
import com.sed.gestor.proyectos.model.enums.FuenteFinanciacion;
import com.sed.gestor.proyectos.model.enums.TipoMovimiento;
import com.sed.gestor.proyectos.repository.ProyectoRepository;
import com.sed.gestor.proyectos.repository.MetaProductoRepository;
import com.sed.gestor.proyectos.service.PresupuestoService;

@Configuration 
@ConditionalOnProperty(name = "app.database.init-data", havingValue = "true", matchIfMissing = false)
public class DataInitializer {
    
    @Bean 
    CommandLineRunner initDatabase(ProyectoRepository proyectoRepository, PresupuestoService presupuestoService, MetaProductoRepository metaProductoRepository, MetaService metaService){
        return args -> {
            if(proyectoRepository.count() > 0) return;

            //Crear el proyecto
            Proyecto proyecto = new Proyecto();
            proyecto.setCodigoBpin("2024130010248");
            proyecto.setNombre("Modernización de la Infraestructura Educativa");
            proyecto.setPrograma("MODERNIZACIÓN DE LA INFRAESTRUCTURA EDUCATIVA");

            //Crear las actividades
            Actividad act1 = new Actividad("Mejorar y/o adecuar sedes educativas", null,null, null, proyecto);
            Actividad act2 = new Actividad("Gastos de viajes y tiquetes", null,null, null, proyecto);
            Actividad act3 = new Actividad("Dotación de Mobiliario Escolar", null, null, null, proyecto);
            Actividad act4 = new Actividad("Legalización de predios", null, null, null, proyecto);
            Actividad act5 = new Actividad("Elaborar Plan Maestro de Infraestructura Educativa", null, null, null, proyecto);

            proyecto.addActividad(act1);
            proyecto.addActividad(act2);
            proyecto.addActividad(act3);
            proyecto.addActividad(act4);
            proyecto.addActividad(act5);

            Proyecto proyectoGuardado = proyectoRepository.save(proyecto);

            // 3. Simular el primer evento: "Apropiación Inicial según Decreto No.1969"
            MovimientoPresupuestal inicial = new MovimientoPresupuestal();
            inicial.setProyecto(proyectoGuardado);
            inicial.setTipoMovimiento(TipoMovimiento.APROPIACION_INICIAL);
            inicial.setNumeroDecreto("Decreto No.1969 del 27 de diciembre de 2024");
            inicial.setFechaDecreto(LocalDate.of(2024, 12, 27));
            inicial.setObservaciones("Apropiación Inicial asignada por decreto");

            // Valores reales del Excel
            inicial.addDetalle(crearDetalle(act1, FuenteFinanciacion.PROPIOS, new BigDecimal("14292276751")));
            inicial.addDetalle(crearDetalle(act1, FuenteFinanciacion.SGP, new BigDecimal("4933693576")));
            inicial.addDetalle(crearDetalle(act2, FuenteFinanciacion.PROPIOS, new BigDecimal("50000000")));
            inicial.addDetalle(crearDetalle(act3, FuenteFinanciacion.PROPIOS, new BigDecimal("1500000000")));
            inicial.addDetalle(crearDetalle(act4, FuenteFinanciacion.PROPIOS, new BigDecimal("500000000")));
            inicial.addDetalle(crearDetalle(act5, FuenteFinanciacion.PROPIOS, new BigDecimal("250000000")));

            presupuestoService.registrarMovimiento(inicial);
            System.out.println(">>> [INIT_DATA=true] Base de datos inicializada con el proyecto BPIN 2024130010248.");

            // Dentro del método initDatabase en DataInitializer.java (después de guardar la Apropiación Inicial):

            // Crear Meta PDD 1
            MetaProducto meta1 = new MetaProducto();
            meta1.setCodigoPrograma("02-02-07");
            meta1.setDescripcionMeta("Mejorar y/o adecuar ochenta (80) sedes educativas.");
            meta1.setUnidadMedida("Número");
            meta1.setProgramacionCuatrienio(new BigDecimal("80"));
            meta1.setProgramacionVigencia(new BigDecimal("20")); // Meta 2026
            meta1.setPonderacion(new BigDecimal("0.30"));
            meta1.setActividad(act1); // Asociada a la Actividad "Mejorar y/o adecuar sedes educativas"

            // Crear Meta PDD 2
            MetaProducto meta2 = new MetaProducto();
            meta2.setCodigoPrograma("02-02-03");
            meta2.setDescripcionMeta("Formular e implementar un (1) Plan Maestro de Infraestructura Educativa.");
            meta2.setUnidadMedida("Porcentaje");
            meta2.setProgramacionCuatrienio(new BigDecimal("100"));
            meta2.setProgramacionVigencia(new BigDecimal("100"));
            meta2.setPonderacion(new BigDecimal("0.10"));
            meta2.setActividad(act5); // Asociada a "Elaborar Plan Maestro"

            metaProductoRepository.save(meta1);
            metaProductoRepository.save(meta2);

            // Agregar avance de prueba
            metaService.registrarAvance(meta1.getId(), 1, 2026, new BigDecimal("5"), "Concluidas 5 sedes en Q1");// Dentro del método initDatabase en DataInitializer.java (después de guardar la Apropiación Inicial):

        };
    }

    private DetalleMovimiento crearDetalle(Actividad actividad, FuenteFinanciacion fuente, BigDecimal monto){
      DetalleMovimiento d = new DetalleMovimiento();
        d.setActividad(actividad);
        d.setFuente(fuente);
        d.setMonto(monto);
        return d;  
    }
}
