package com.veterinaria.vet.DTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.persistence.Column;

public class EspecieDTO {

    @Column(name = "Descripcion",nullable = false)
    private String descripcion;

    public String toJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); 
        return objectMapper.writeValueAsString(this);
    }
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descipcion) {
        this.descripcion = descipcion;
    }
}
