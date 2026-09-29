package com.sed.gestor.proyectos.config;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sed.gestor.proyectos.model.Actividad;
import com.sed.gestor.proyectos.model.DetalleMovimiento;
import com.sed.gestor.proyectos.model.MovimientoPresupuestal;
import com.sed.gestor.proyectos.model.Proyecto;
import com.sed.gestor.proyectos.model.enums.FuenteFinanciacion;
import com.sed.gestor.proyectos.model.enums.TipoMovimiento;
import com.sed.gestor.proyectos.repository.ProyectoRepository;
import com.sed.gestor.proyectos.service.PresupuestoService;

@Configuration 
@ConditionalOnProperty(name = "app.database.init-data", havingValue = "true", matchIfMissing = false)
public class DataInitializer {
    
    @Bean 
    CommandLineRunner initDatabase(ProyectoRepository proyectoRepository, PresupuestoService presupuestoService){
        return args -> {
            if(proyectoRepository.count() > 0) return;

            //Crear el proyecto
            Proyecto proyecto = new Proyecto();
            proyecto.setCodigoBpin("2024130010248");
            proyecto.setNombre("Modernización de la Infraestructura Educativa");
            proyecto.setPrograma("MODERNIZACIÓN DE LA INFRAESTRUCTURA EDUCATIVA");

            //Crear las actividades
            Actividad act1 = new Actividad("Mejorar y/o adecuar sedes educativas", null, null, proyecto);
            Actividad act2 = new Actividad("Gastos de viajes y tiquetes", null, null, proyecto);
            Actividad act3 = new Actividad("Dotación de Mobiliario Escolar", null, null, proyecto);
            Actividad act4 = new Actividad("Legalización de predios", null, null, proyecto);
            Actividad act5 = new Actividad("Elaborar Plan Maestro de Infraestructura Educativa", null, null, proyecto);

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
