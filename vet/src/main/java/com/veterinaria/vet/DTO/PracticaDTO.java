package com.veterinaria.vet.DTO;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;

public class PracticaDTO {
    @NotNull(message = "El campo id no puede estar vacío",groups = { PutAndDelete.class })
    private Long ID;
    
    @NotNull(message = "El precio no debe ser nulo", groups = { PutAndPost.class })
    @DecimalMin(value = "0",inclusive = false, message = "El precio debe ser mayor a cero", groups = { PutAndPost.class })
    private BigDecimal precio;

    @NotNull(message = "El campo descripcion no puede estar vacía", groups = { PutAndPost.class })
    @NotBlank(message = "El campo descripcion no puede estar vacío", groups = { PutAndPost.class })
        @Pattern(regexp = "^[a-zA-Z\\s\\p{Punct}]*$", message = "La descripción solo debe contener letras, espacios o caracteres especiales", groups = {
            PutAndPost.class })
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
