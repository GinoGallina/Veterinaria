package com.veterinaria.vet.DTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.groups.Default;

public class ProveedorDTO {
    
    @NotBlank(groups = { PutAndDelete.class })
    private Long ID;


    private String cuil;


    private String direccion;


    private String email;


    private String razonSocial;


    private String telefono;

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


    public String getCuil() {
      return cuil;
    }


    public void setCuil(String cuil) {
      this.cuil = cuil;
    }


    public String getDireccion() {
      return direccion;
    }


    public void setDireccion(String direccion) {
      this.direccion = direccion;
    }


    public String getEmail() {
      return email;
    }


    public void setEmail(String email) {
      this.email = email;
    }


    public String getRazonSocial() {
      return razonSocial;
    }


    public void setRazonSocial(String razonSocial) {
      this.razonSocial = razonSocial;
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
