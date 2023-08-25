package com.veterinaria.vet.DTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;

public class ClienteDTO {
    @NotNull(message = "El campo id no puede estar vacío",groups = { PutAndDelete.class })
    private Long ID;

    @NotNull(message = "El campo dni no puede estar vacío",groups = { PutAndDelete.class })
    @NotBlank(message = "El campo dni no puede estar vacío", groups = { PutAndPost.class })
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos", groups = { PutAndPost.class })
    private String dni;

    @NotNull(message = "El campo nombre no puede estar vacío",groups = { PutAndDelete.class })   
    @NotBlank(message = "El campo nombre no puede estar vacío", groups = { PutAndPost.class })
    private String nombre;

    @NotNull(message = "El campo apellido no puede estar vacío",groups = { PutAndDelete.class })
    @NotBlank(message = "El campo apellido no puede estar vacío", groups = { PutAndPost.class })
    private String apellido;

    @NotNull(message = "El campo direccion no puede estar vacío",groups = { PutAndDelete.class })
    @NotBlank(message = "El campo direccion no puede estar vacío", groups = { PutAndPost.class })
    private String direccion;

    @NotNull(message = "El campo telefono no puede estar vacío",groups = { PutAndDelete.class })
    @NotBlank(message = "El campo telefono no puede estar vacío", groups = { PutAndPost.class })
    //VER SI VA ESTA 
    //@Pattern(regexp = "^[+()-]*[0-9][+()-0-9]*$", message = "El teléfono no es válido", groups = { PutAndPost.class, PutAndDelete.class })
    private String telefono;

    @NotNull(message = "El campo email no puede estar vacío",groups = { PutAndDelete.class })
    @NotBlank(message = "El campo email no puede estar vacío", groups = { PutAndPost.class })
    @Email(message = "El campo email debe ser una dirección de correo electrónico válida")
    private String email;

    @NotNull(message = "El campo password no puede estar vacío",groups = { PutAndDelete.class })
    @NotBlank(message = "El campo password no puede estar vacío", groups = { PutAndPost.class })
    private String password;
    
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

    public interface PutAndPost extends Default {
    }

    public String getEmail() {
      return email;
    }


    public void setEmail(String email) {
      this.email = email;
    }


    public String getPassword() {
      return password;
    }


    public void setPassword(String password) {
      this.password = password;
    }


    public String getDni() {
      return dni;
    }


    public void setDni(String dni) {
      this.dni = dni;
    }
  }