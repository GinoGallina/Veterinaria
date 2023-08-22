package com.veterinaria.vet.Models;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class PracticaAtencionId implements Serializable {

    private Long atencionID;
    private Long practicaID;

    public Long getAtencionID() {
        return atencionID;
    }

    public void setAtencionID(Long atencionID) {
        this.atencionID = atencionID;
    }

    public Long getPracticaID() {
        return practicaID;
    }

    public void setPracticaID(Long practicaID) {
        this.practicaID = practicaID;
    }

    public PracticaAtencionId() {
    }

    public PracticaAtencionId(Long atencionID, Long practicaID) {
        this.atencionID = atencionID;
        this.practicaID = practicaID;
    }
    

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PracticaAtencionId that = (PracticaAtencionId) o;
        return Objects.equals(atencionID, that.atencionID) &&
               Objects.equals(practicaID, that.practicaID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(atencionID, practicaID);
    }

    @Override
    public String toString() {
        return "PracticaAtencionId{" +
               "atencionID=" + atencionID +
               ", practicaID=" + practicaID +
               '}';

              }
}
