package com.veterinaria.vet.Models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @ManyToOne
    @JoinColumn(name = "ClienteID", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL)
    private List<ReservaProducto> reservasProductos;
    

    @Column(name = "CreatedAt",nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt",nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "DeletedAt",nullable = false)
    private LocalDateTime deletedAt;


   public String toJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Crear una lista de precios en formato JSON
        List<Map<String, Object>> reservasProductosJson = new ArrayList<>();
        if(reservasProductos!=null){
            for (ReservaProducto reservaProducto : reservasProductos) {
                Map<String, Object> reservaProductoMap = new HashMap<>();
                reservaProductoMap.put("id", reservaProducto.getId());
                reservaProductoMap.put("producto", reservaProducto.getProducto());
                reservaProductoMap.put("precio", reservaProducto.getPrecio());
                reservaProductoMap.put("cantidad", reservaProducto.getCantidad());
                // Agrega otros campos del objeto Precio que quieras incluir en el JSON
                reservasProductosJson.add(reservaProductoMap);
            }
        }

        // Crear un mapa para representar el objeto Practica en JSON
        Map<String, Object> reservaMap = new HashMap<>();
        reservaMap.put("ID", ID);
        reservaMap.put("cliente", cliente);
        reservaMap.put("updatedAt", updatedAt);
        reservaMap.put("createdAt", createdAt);
        reservaMap.put("deletedAt", deletedAt);
        
        if (!reservasProductosJson.isEmpty()) {
            reservaMap.put("reservasProductos", reservasProductosJson);
        }


        return objectMapper.writeValueAsString(reservaMap);
    } 

    public Long getID() {
        return ID;
    }

    public void setID(Long iD) {
        ID = iD;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

   

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public List<ReservaProducto> getReservasProductos() {
        return reservasProductos;
    }

    public void setReservasProductos(List<ReservaProducto> reservasProductos) {
        this.reservasProductos = reservasProductos;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
}