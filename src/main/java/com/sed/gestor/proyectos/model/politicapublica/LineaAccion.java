package com.sed.gestor.proyectos.model.politicapublica;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity 
@Table (name = "linea_accion")
public class LineaAccion {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false, length = 255)
    private String nombre;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "objetivo_especifico_id", nullable = false)
    private ObjetivoEspecifico objetivoEspecifico;

    @OneToMany (mappedBy = "lineaAccion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Producto> productos = new ArrayList<>();

    public LineaAccion(){
    }

    public LineaAccion(Long id, String nombre, ObjetivoEspecifico objetivoEspecifico) {
        this.id = id;
        this.nombre = nombre;
        this.objetivoEspecifico = objetivoEspecifico;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ObjetivoEspecifico getObjetivoEspecifico() {
        return objetivoEspecifico;
    }

    public void setObjetivoEspecifico(ObjetivoEspecifico objetivoEspecifico) {
        this.objetivoEspecifico = objetivoEspecifico;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public void addProducto(Producto producto) {
        productos.add(producto);
        producto.setLineaAccion(this);
    }

    public void removeProducto(Producto producto) {
        productos.remove(producto);
        producto.setLineaAccion(null);
    }
}
