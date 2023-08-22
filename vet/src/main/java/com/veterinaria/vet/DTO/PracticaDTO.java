package com.veterinaria.vet.DTO;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.groups.Default;

public class PracticaDTO {
    @NotNull(message = "El campo id no puede estar vacío",groups = { PutAndDelete.class })
    private Long ID;
    
    @NotNull(message = "El precio no debe ser nulo", groups = { PutAndPost.class })
    @Positive(message = "El precio debe ser mayor que cero", groups = { PutAndPost.class })
    private BigDecimal precio;

    @NotNull(message = "El campo razonSocial no puede estar vacía", groups = { PutAndPost.class })
    private String descripcion;

    public Long getID() {
      return ID;
    }

    public void setID(Long iD) {
      ID = iD;
    }

    public BigDecimal getPrecio() {
      return precio;
    }

    public void setPrecio(BigDecimal precio) {
      this.precio = precio;
    }

    public String getDescripcion() {
      return descripcion;
    }

    public void setDescripcion(String descripcion) {
      this.descripcion = descripcion;
    }

    public interface PutAndDelete extends Default {
    }
    public interface PutAndPost extends Default {
    }
      
}
