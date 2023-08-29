package com.veterinaria.vet.DTO;

import java.math.BigDecimal;

import com.veterinaria.vet.DTO.RazaDTO.PostAndPut;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;

public class DeudaDTO {
    @NotNull(message = "El campo id no puede estar vacío", groups = { PutAndDelete.class, Put.class })
    private Long ID;

    @NotNull(message = "El campo ID del proveedor no puede estar vacío",groups = {  PutAndPost.class })
    private Long proveedorID;

    @NotBlank(message = "El campo descripción no puede estar vacío",groups = { PutAndPost.class })
    @NotNull(message = "El campo descripción no puede estar vacío", groups = { PutAndPost.class })
    @Pattern(regexp = "^[a-zA-Z\\s\\p{Punct}]*$", message = "La descripción solo debe contener letras, espacios o caracteres especiales", groups = {
            PostAndPut.class })
    private String descripcion;

    @NotNull(message = "El campo precio no puede estar vacío", groups = { PutAndPost.class })
    @DecimalMin(value = "0",inclusive=false, message = "El precio debe ser mayor a cero", groups = { PutAndPost.class })
    private BigDecimal precio;
    
    @NotNull(message = "El campo precio no puede estar vacío", groups = { Put.class })
    @DecimalMin(value = "0",inclusive=false, message = "El precio debe ser mayor a cero", groups = { Put.class })
    private BigDecimal pago;


    public interface PutAndDelete extends Default {
    }

    public interface PutAndPost extends Default {
    }
    public interface Put extends Default {
    }

    public Long getProveedorID() {
      return proveedorID;
    }

    public void setProveedorID(Long proveedorID) {
      this.proveedorID = proveedorID;
    }

    public String getDescripcion() {
      return descripcion;
    }

    public void setDescripcion(String descripcion) {
      this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
      return precio;
    }
    

    public void setPrecio(BigDecimal precio) {
      this.precio = precio;
    }

    public Long getID() {
      return ID;
    }

    public void setID(Long iD) {
      ID = iD;
    }

    public BigDecimal getPago() {
      return pago;
    }

    public void setPago(BigDecimal pago) {
      this.pago = pago;
    }
}
