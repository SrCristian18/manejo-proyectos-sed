package com.sed.gestor.proyectos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sed.gestor.proyectos.model.DocumentoSoporte;

public interface DocumentoSoporteRepository extends JpaRepository<DocumentoSoporte, Long>{
     List<DocumentoSoporte> findByDocumentoSoporteId(Long documentoSoporteId);  
}
