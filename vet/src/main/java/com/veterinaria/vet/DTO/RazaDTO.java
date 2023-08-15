package com.veterinaria.vet.DTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.veterinaria.vet.Models.Especie;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.groups.Default;


public class RazaDTO {
    @NotBlank(groups = { PutAndDelete.class })
    private Long ID;

    @NotBlank
    private Especie especie;

    @NotBlank
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

    public Especie getEspecie() {
      return especie;
    }

    public void setEspecie(Especie especie) {
      this.especie = especie;
    }

    public String getDescripcion() {
      return descripcion;
    }

    public void setDescripcion(String descripcion) {
      this.descripcion = descripcion;
    }
    public interface PutAndDelete extends Default {
    }
    
}
