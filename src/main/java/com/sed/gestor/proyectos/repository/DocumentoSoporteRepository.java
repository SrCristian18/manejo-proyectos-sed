package com.sed.gestor.proyectos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sed.gestor.proyectos.model.DocumentoSoporte;

@Repository 
public interface DocumentoSoporteRepository extends JpaRepository<DocumentoSoporte, Long>{
     List<DocumentoSoporte> findByMovimientoId(Long movimientoId);  
}
