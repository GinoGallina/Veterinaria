package com.veterinaria.vet.DTO;

import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.veterinaria.vet.DTO.RazaDTO.PostAndPut;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;

public class ProductoDTO {
    @NotNull(message = "El campo id no puede estar vacío", groups = { PutAndDelete.class })
    private Long ID;

    @NotBlank(message = "El campo descripcion no puede estar vacío", groups = { PutAndPost.class })
    @NotNull(message = "El campo descripcion no puede estar vacío", groups = { PutAndPost.class })
    @Pattern(regexp = "^[a-zA-Z\\s\\p{Punct}]*$", message = "La descripción solo debe contener letras, espacios o caracteres especiales", groups = {
            PostAndPut.class })
    private String descripcion;

    @NotNull(message = "El campo precio no puede estar vacío", groups = { PutAndPost.class })
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a cero", groups = { PutAndPost.class })
    private BigDecimal precio;

    @NotNull(message = "El campo stock no puede estar vacío", groups = { PutAndPost.class })
    @Min(value = 0, message = "El stock debe ser mayor o igual a cero", groups = { PutAndPost.class })
    private Integer stock;

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

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public interface PutAndDelete extends Default {
    }

    public interface PutAndPost extends Default {
    }
    // public String getImgRuta() {
    // return imgRuta;
    // }

    // public void setImgRuta(String imgRuta) {
    // this.imgRuta = imgRuta;
    // }

}
