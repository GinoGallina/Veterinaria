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
@Table(name = "atenciones")
public class Atencion {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @ManyToOne
    @JoinColumn(name = "VeterinarioID", nullable = false)
    private Veterinario veterinario;

    @ManyToOne
    @JoinColumn(name = "MascotaID", nullable = false)
    private Mascota mascota;

    @OneToMany(mappedBy = "atencion", cascade = CascadeType.ALL)
    private List<PracticaAtencion> practicasAtenciones;

    @Column(name = "FechaAtencion", nullable = false)
    private LocalDateTime fechaAtencion;

    @Column(name = "FechaPago")
    private LocalDateTime fechaPago;

    @Column(name = "CreatedAt", nullable = false, insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt", nullable = false, insertable = false)
    private LocalDateTime updatedAt;

    @Column(name = "DeletedAt")
    private LocalDateTime deletedAt;

    public String toJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Crear una lista de atencionesJson en formato JSON
        List<Map<String, Object>> practicasAtencionesJson = new ArrayList<>();
        if (practicasAtenciones != null) {
            for (PracticaAtencion practicaAtencion : practicasAtenciones) {
                Map<String, Object> practicaAtencionMap = new HashMap<>();
                practicaAtencionMap.put("id", practicaAtencion.getId());
                practicaAtencionMap.put("precioPactado", practicaAtencion.getPrecioPactado());
                Map<String, Object>  practicaMap = new HashMap<>();
                Practica practica= practicaAtencion.getPractica();
                practicaMap.put("id", practica.getID());
                practicaMap.put("descripcion", practica.getDescripcion());
                practicaMap.put("createdAt", practica.getCreatedAt());
                practicaMap.put("updatedAt", practica.getUpdatedAt());
                practicaMap.put("deletedAt", practica.getDeletedAt());
                practicaMap.put("precios", practica.preciosJson());
                practicaMap.put("ultimoPrecio", practica.getLastPrice().getValor());
                practicaAtencionMap.put("practica", practicaMap);
                
                
                
                practicasAtencionesJson.add(practicaAtencionMap);
            }
        }

        // Crear un mapa para representar el objeto Practica en JSON
        Map<String, Object> atencionMap = new HashMap<>();
        atencionMap.put("id", ID);
        atencionMap.put("veterinario", veterinario);
        atencionMap.put("mascota", mascota);
        atencionMap.put("fechaAtencion", fechaAtencion);
        atencionMap.put("fechaPago", fechaPago);
        atencionMap.put("createdAt", updatedAt);
        atencionMap.put("updatedAt", updatedAt);
        atencionMap.put("deletedAt", deletedAt);

        if (!practicasAtencionesJson.isEmpty()) {
            atencionMap.put("practicasAtenciones", practicasAtencionesJson);
        };

        return objectMapper.writeValueAsString(atencionMap);
    } 

    public Long getID() {
        return ID;
    }

    public void setID(Long iD) {
        ID = iD;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public LocalDateTime getFechaAtencion() {
        return fechaAtencion;
    }

    public void setFechaAtencion(LocalDateTime fechaAtencion) {
        this.fechaAtencion = fechaAtencion;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
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

    public List<PracticaAtencion> getPracticasAtenciones() {
        return practicasAtenciones;
    }

    public void setPracticasAtenciones(List<PracticaAtencion> practicasAtenciones) {
        this.practicasAtenciones = practicasAtenciones;
    }

}