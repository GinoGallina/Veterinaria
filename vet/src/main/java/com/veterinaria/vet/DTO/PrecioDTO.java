package com.veterinaria.vet.DTO;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;

public class PrecioDTO {
    @NotNull(message = "El campo id no puede estar vacío",groups = { PutAndDelete.class })
    private Long ID;
    @NotNull(message = "El campo practicaID no puede estar vacía", groups = { PutAndDelete.class })
    private Long practicaID;
    
    @NotNull(message = "El valor no debe ser nulo", groups = { PutAndPost.class })
    @NotBlank(message = "El valor no debe estar en blanco", groups = { PutAndPost.class })
    @DecimalMin(value = "0.01", message = "El valor debe ser mayor a cero", groups = { PutAndPost.class })
    private BigDecimal valor;


    public Long getID() {
      return ID;
    }

    public void setID(Long iD) {
      ID = iD;
    }

    public BigDecimal getvalor() {
      return valor;
    }

    public void setvalor(BigDecimal valor) {
      this.valor = valor;
    }



    public interface PutAndDelete extends Default {
    }
    public interface PutAndPost extends Default {
    }
    public Long getPracticaID() {
      return practicaID;
    }

    public void setPracticaID(Long practicaID) {
      this.practicaID = practicaID;
    }

    public BigDecimal getValor() {
      return valor;
    }

    public void setValor(BigDecimal valor) {
      this.valor = valor;
    }
      
}
