package com.veterinaria.vet.DTO;

import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;

public class ProductoDTO {
    @NotNull(message = "El campo id no puede estar vacío",groups = { PutAndDelete.class })
    private Long ID;
    
    @NotBlank(message = "El campo descripcion no puede estar vacía", groups = { PutAndPost.class })
    private String descripcion;

    @NotNull(message = "El campo precio no puede estar vacía", groups = { PutAndPost.class })
    private BigDecimal precio;
    @NotBlank(message = "El campo img no puede estar vacía",groups = { PutAndPost.class })
    private byte[] img;

    @NotNull(message = "El campo razonSocial no puede estar vacía",groups = { PutAndPost.class })
    private int stock;


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

    public BigDecimal getPrecio() {
      return precio;
    }

    public void setPrecio(BigDecimal precio) {
      this.precio = precio;
    }

    public byte[] getImg() {
      return img;
    }

    public void setImg(byte[] img) {
      this.img = img;
    }

    public int getStock() {
      return stock;
    }

    public void setStock(int stock) {
      this.stock = stock;
    }
    public interface PutAndDelete extends Default {
    }
    public interface PutAndPost extends Default {
    }


  }
