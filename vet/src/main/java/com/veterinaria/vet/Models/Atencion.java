package com.veterinaria.vet.Models;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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

    
    @ManyToMany
    @JoinTable(
        name = "atencionpractica",
        joinColumns = @JoinColumn(name = "AtencionID"),
        inverseJoinColumns = @JoinColumn(name = "PracticaID")
    )
    private List<Practica> practicas;

    @Column(name = "FechaAtencion",nullable = false)
    private LocalDateTime fechaAtencion;

    @Column(name = "FechaPago")
    private LocalDateTime fechaPago;

    @Column(name = "CreatedAt",nullable = false, insertable = false)
    private LocalDateTime createdAt;
    

    @Column(name = "UpdatedAt",nullable = false, insertable = false)
    private LocalDateTime updatedAt;

    @Column(name = "DeletedAt")
    private LocalDateTime deletedAt;

    public String toJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        //String atencionesJSON=objectMapper.writeValueAsString(this.getPracticas());
        System.out.println("ACA VAN PRACTICAS");
        System.out.println(objectMapper.writeValueAsString(this.getPracticas()));
        System.out.println(this.getPracticas());
        System.out.println(this);
        //atencionesJSON+=atencionesJSON+objectMapper.writeValueAsString(this.practicas);
        return "atencionesJSON";
    }

    // Método para convertir la instancia en formato JSON
    public String toJsonWithLists() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper.writeValueAsString(this);
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

    public List<Practica> getPracticas() {
        return practicas;
    }

    public void setPracticas(List<Practica> practicas) {
        this.practicas = practicas;
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

    

}