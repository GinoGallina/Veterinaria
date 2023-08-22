package com.veterinaria.vet.Models;

import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "atencionpractica")
public class PracticaAtencion {

    @EmbeddedId
    private PracticaAtencionId id;

    @ManyToOne
    @JoinColumn(name = "AtencionID", nullable = false)
    private Atencion atencion;

    @ManyToOne
    @JoinColumn(name = "PracticaID", nullable = false)
    private Practica practica;
  
    @Column(name = "PrecioPactado",nullable = false)
    private BigDecimal precioPactado;

    public PracticaAtencionId getId() {
      return id;
    }

    public void setId(PracticaAtencionId id) {
      this.id = id;
    }

    public Atencion getAtencion() {
      return atencion;
    }

    public void setAtencion(Atencion atencion) {
      this.atencion = atencion;
    }

    public Practica getPractica() {
      return practica;
    }

    public void setPractica(Practica practica) {
      this.practica = practica;
    }

    public BigDecimal getPrecioPactado() {
      return precioPactado;
    }

    public void setPrecioPactado(BigDecimal precioPactado) {
      this.precioPactado = precioPactado;
    }



}
