package com.sed.gestor.proyectos.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration 
@ConditionalOnProperty(name = "app.database.init-data", havingValue = "true", matchIfMissing = false)
public class DataInitializer {
    
}
