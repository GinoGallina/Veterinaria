package com.veterinaria.vet.DTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.groups.Default;

public class VeterinarioDTO {
    
    @NotBlank(groups = { PutAndDelete.class })
    private Long ID;

    private String matricula;


    private String nombre;


    private String apellido;


    private String direccion;


    private String telefono;


    public Long getID() {
      return ID;
    }


    public void setID(Long iD) {
      ID = iD;
    }

    public String toJson() throws JsonProcessingException {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.registerModule(new JavaTimeModule()); 
      return objectMapper.writeValueAsString(this);
    }

    public String getMatricula() {
      return matricula;
    }


    public void setMatricula(String matricula) {
      this.matricula = matricula;
    }


    public String getNombre() {
      return nombre;
    }


    public void setNombre(String nombre) {
      this.nombre = nombre;
    }


    public String getApellido() {
      return apellido;
    }


    public void setApellido(String apellido) {
      this.apellido = apellido;
    }


    public String getDireccion() {
      return direccion;
    }


    public void setDireccion(String direccion) {
      this.direccion = direccion;
    }


    public String getTelefono() {
      return telefono;
    }


    public void setTelefono(String telefono) {
      this.telefono = telefono;
    }
    public interface PutAndDelete extends Default {
    }
  }