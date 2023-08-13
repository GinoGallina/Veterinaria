package com.veterinaria.vet.Models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "precios")
public class Precio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @ManyToOne
    @JoinColumn(name = "PracticaID", nullable = false)
    private Practica practica;

    @Column(name = "Valor",nullable = false, insertable = false)
    private BigDecimal valor;

    @Column(name = "CreatedAt",nullable = false, insertable = false)
    private LocalDateTime createdAt;
    

    public Precio(Practica practica, BigDecimal valor) {
        this.practica = practica;
        this.valor = valor;
    }

    public Precio() {
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long iD) {
        ID = iD;
    }

    public Practica getPractica() {
        return practica;
    }

    public void setPractica(Practica practica) {
        this.practica = practica;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

  

    
  }