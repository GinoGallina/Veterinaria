package com.veterinaria.vet.DTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;

public class ProveedorDTO {
    
    @NotNull(message = "El campo descripción no puede estar vacío",groups = { PutAndDelete.class })
    private Long ID;

    @NotBlank(message = "El campo cuil no puede estar vacío",groups = { PutAndPost.class })
    @NotNull(message = "El campo cuil no puede estar vacío",groups = { PutAndPost.class })
    //VER
    // @Pattern(regexp = "^\\d{2}-\\d{8}-\\d{1}$", message = "El CUIL no es válido", groups = { PutAndPost.class })
    private String cuil;

    @NotBlank(message = "El campo direccion no puede estar vacío",groups = { PutAndPost.class })
    @NotNull(message = "El campo direccion no puede estar vacío",groups = { PutAndPost.class })
    @Pattern(regexp = "^[a-zA-Z0-9\\s]*$", message = "La dirección debe contener letras, números o espacios", groups = {
        PutAndPost.class })
    private String direccion;

    @NotBlank(message = "El campo email no puede estar vacío",groups = { PutAndPost.class })
    @Email(message = "El campo email debe ser una dirección de correo electrónico válida")
    @NotNull(message = "El campo email no puede estar vacío",groups = { PutAndPost.class })
    private String email;

    @NotBlank(message = "El campo razonSocial no puede estar vacío",groups = { PutAndPost.class })
    @NotNull(message = "El campo razonSocial no puede estar vacío",groups = { PutAndPost.class })
    private String razonSocial;

    @NotBlank(message = "El campo telefono no puede estar vacío",groups = { PutAndPost.class })
    @NotNull(message = "El campo telefono no puede estar vacío",groups = { PutAndPost.class })
    @Pattern(regexp = "^[0-9]+$", message = "El telefono debe contener sólo números", groups = {
        PutAndPost.class })
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
    public interface PutAndPost extends Default {
    }
  }
