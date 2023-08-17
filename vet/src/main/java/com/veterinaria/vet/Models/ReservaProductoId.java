package com.veterinaria.vet.Models;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class ReservaProductoId implements Serializable {

    private Long reservaID;
    private Long productoID;

    // Constructores, getters, setters y métodos equals/hashCode

    // Constructor vacío
    public ReservaProductoId() {
    }

    // Constructor con valores
    public ReservaProductoId(Long reservaID, Long productoID) {
        this.reservaID = reservaID;
        this.productoID = productoID;
    }

    // Getters y setters
    public Long getReservaID() {
        return reservaID;
    }

    public void setReservaID(Long reservaID) {
        this.reservaID = reservaID;
    }

    public Long getProductoID() {
        return productoID;
    }

    public void setProductoID(Long productoID) {
        this.productoID = productoID;
    }

    // Equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservaProductoId that = (ReservaProductoId) o;
        return Objects.equals(reservaID, that.reservaID) &&
                Objects.equals(productoID, that.productoID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservaID, productoID);
    }
  }