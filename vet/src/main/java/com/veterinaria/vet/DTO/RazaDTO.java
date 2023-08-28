package com.veterinaria.vet.DTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;


public class RazaDTO {
    @NotNull(message = "El campo ID no puede estar vacío",groups = { PutAndDelete.class })
    private Long ID;

    @NotNull(message = "El campo especie no puede estar vacío",groups = { PostAndPut.class })
    private Long especieID;

    @NotBlank(message = "El campo descripción no puede estar vacío",groups = { PostAndPut.class })
    @NotNull(message = "El campo descripción no puede estar vacío", groups = { PostAndPut.class })
    @Pattern(regexp = "^[a-zA-Z\\s\\p{Punct}]*$", message = "La descripción solo debe contener letras, espacios o caracteres especiales", groups = {
            PostAndPut.class })
    private String descripcion;

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

    public Long getEspecieID() {
      return especieID;
    }

    public void setEspecieID(Long especieID) {
      this.especieID = especieID;
    }

    public String getDescripcion() {
      return descripcion;
    }

    public void setDescripcion(String descripcion) {
      this.descripcion = descripcion;
    }
    public interface PutAndDelete extends Default {
    }

    public interface PostAndPut extends Default {
    }
    
}
