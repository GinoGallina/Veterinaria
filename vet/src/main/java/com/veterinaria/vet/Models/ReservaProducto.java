package com.veterinaria.vet.Models;

import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservaproducto")
public class ReservaProducto {

    @EmbeddedId
    private ReservaProductoId id;
    
    
    @ManyToOne
    @JoinColumn(name = "ReservaID", nullable = false)
    private Reserva reserva;

    @ManyToOne
    @JoinColumn(name = "ProductoID", nullable = false)
    private Producto producto;
  
    @Column(name = "Precio",nullable = false)
    private BigDecimal precio;

    @Column(name = "Cantidad",nullable = false)
    private int cantidad;


    public String toJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); 
        return objectMapper.writeValueAsString(this);
    }



    public Reserva getReserva() {
      return reserva;
    }

    public void setReserva(Reserva reserva) {
      this.reserva = reserva;
    }

    public Producto getProducto() {
      return producto;
    }

    public void setProducto(Producto producto) {
      this.producto = producto;
    }

    public ReservaProductoId getId() {
      return id;
    }


    public void setId(ReservaProductoId id) {
      this.id = id;
    }



    public BigDecimal getPrecio() {
      return precio;
    }



    public void setPrecio(BigDecimal precio) {
      this.precio = precio;
    }



    public int getCantidad() {
      return cantidad;
    }



    public void setCantidad(int cantidad) {
      this.cantidad = cantidad;
    }

    
}
