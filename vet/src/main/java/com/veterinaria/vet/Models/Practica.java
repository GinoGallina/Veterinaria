package com.veterinaria.vet.Models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "practicas")
public class Practica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "PracticaID")
    private List<Precio> precios;

    @Transient
    private Precio ultimoPrecio;

    @Column(name = "Descripcion", nullable = false)
    private String descripcion;

    @Column(name = "CreatedAt", nullable = false, insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt", nullable = false, insertable = false)
    private LocalDateTime updatedAt;

    @Column(name = "DeletedAt")
    private LocalDateTime deletedAt;

    public List<Map<String, Object>> preciosJson(){
        List<Map<String, Object>> preciosJson = new ArrayList<>();
        if(precios!=null){
            for (Precio precio : precios) {
                Map<String, Object> precioMap = new HashMap<>();
                precioMap.put("id", precio.getID());
                precioMap.put("valor", precio.getValor());
                precioMap.put("createdAt", precio.getCreatedAt());
                // Agrega otros campos del objeto Precio que quieras incluir en el JSON
                preciosJson.add(precioMap);
            }
        }
        return preciosJson;
    }

    public String toJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Crear una lista de precios en formato JSON
        List<Map<String, Object>> preciosJson = this.preciosJson();

        // Crear un mapa para representar el objeto Practica en JSON
        Map<String, Object> practicaMap = new HashMap<>();
        practicaMap.put("ID", ID);
        practicaMap.put("descripcion", descripcion);
        practicaMap.put("descripcion", descripcion);
        practicaMap.put("updatedAt", updatedAt);
        practicaMap.put("deletedAt", deletedAt);
        
        if (!preciosJson.isEmpty()) {
            practicaMap.put("precios", preciosJson);
        }

        // Agregar el último precio si está presente
        if (ultimoPrecio != null) {
            Map<String, Object> ultimoPrecioMap = new HashMap<>();
            ultimoPrecioMap.put("valor", ultimoPrecio.getValor());
            practicaMap.put("ultimoPrecio", ultimoPrecioMap);
        }

        return objectMapper.writeValueAsString(practicaMap);
    }

    public Precio getLastPrice() {
        if (precios != null && !precios.isEmpty()) {
            LocalDateTime now = LocalDateTime.now();
            ultimoPrecio = precios.stream()
                    .filter(precio -> precio.getCreatedAt().isBefore(now))
                    .max(Comparator.comparing(Precio::getCreatedAt))
                    .orElse(null);
        }
        return ultimoPrecio;
    }

    public Practica() {
        // Inicializa el último precio con el precio de la fecha más cercana en la lista
        // de precios
        if (precios != null && !precios.isEmpty()) {
            LocalDateTime now = LocalDateTime.now();
            ultimoPrecio = precios.stream()
                    .filter(precio -> precio.getCreatedAt().isBefore(now))
                    .max(Comparator.comparing(Precio::getCreatedAt))
                    .orElse(null);
        }
    }
    public Long getID() {
        return ID;
    }

    public void setID(Long iD) {
        ID = iD;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public List<Precio> getPrecios() {
        return precios;
    }

    public void setPrecios(List<Precio> precios) {
        this.precios = precios;
    }

    public Precio getUltimoPrecio() {
        return ultimoPrecio;
    }

    public void setUltimoPrecio(Precio ultimoPrecio) {
        this.ultimoPrecio = ultimoPrecio;
    }
}