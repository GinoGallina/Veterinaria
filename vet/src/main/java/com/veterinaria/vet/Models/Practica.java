package com.veterinaria.vet.Models;

import java.time.LocalDateTime;
import java.util.List;

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

        @Column(name = "Descripcion",nullable = false)
        private String descripcion;

        @Column(name = "CreatedAt",nullable = false, insertable = false)
        private LocalDateTime createdAt;

        @Column(name = "UpdatedAt",nullable = false, insertable = false)
        private LocalDateTime updatedAt;

        @Column(name = "DeletedAt")
        private LocalDateTime deletedAt;

        public String toJson() throws JsonProcessingException {
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