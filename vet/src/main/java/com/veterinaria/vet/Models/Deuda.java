package com.veterinaria.vet.Models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "deudas")
public class Deuda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @ManyToOne
    @JoinColumn(name = "ProovedorID", nullable = false)
    private Proveedor proveedor;

    @Column(name = "Descripcion",nullable = false)
    private String descripcion;

    @Column(name = "Precio",nullable = false)
    private Long precio;

    @Column(name = "CreatedAt",nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt",nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "DeletedAt")
    private LocalDateTime deletedAt;

    public Long getID() {
      return ID;
    }

    public void setID(Long iD) {
      ID = iD;
    }

    public Proveedor getProveedor() {
      return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
      this.proveedor = proveedor;
    }

    public String getDescripcion() {
      return descripcion;
    }

    public void setDescripcion(String descripcion) {
      this.descripcion = descripcion;
    }

    public Long getPrecio() {
      return precio;
    }

    public void setPrecio(Long precio) {
      this.precio = precio;
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

  
    
}
