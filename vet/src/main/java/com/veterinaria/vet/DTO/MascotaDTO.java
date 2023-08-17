package com.veterinaria.vet.DTO;

import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.groups.Default;

public class MascotaDTO {
    @NotNull(message = "El campo ID no puede estar vacío",groups = { PutAndDelete.class })
    private Long ID;

    @NotNull(message = "El campo ID del cliente no puede estar vacío",groups = {  PutAndPost.class })
    private Long clienteID;

    @NotNull(message = "El campo raza no puede estar vacío",groups = {  PutAndPost.class })
    private Long razaID;

    @PastOrPresent(message = "La fecha de nacimiento debe ser en el pasado o la fecha actual",groups = { PutAndPost.class })
    @NotNull(message = "La fecha de nacimiento no puede estar vacía",groups = { PutAndPost.class })
    private LocalDate nacimiento;

    @NotBlank(message = "El campo nombre no puede estar vacío",groups = { PutAndPost.class })
    private String nombre;

    @NotBlank(message = "El campo sexo no puede estar vacío", groups = { PutAndPost.class })
    //@Pattern(regexp = "[mf]", message = "El campo sexo debe ser 'm' o 'f'",groups = { PutAndPost.class })
    private String sexo;

    public interface PutAndDelete extends Default {
    }
    public interface PutAndPost extends Default {
    }

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
    public Long getClienteID() {
      return clienteID;
    }
    public void setClienteID(Long clienteID) {
      this.clienteID = clienteID;
    }
    public Long getRazaID() {
      return razaID;
    }
    public void setRazaID(Long razaID) {
      this.razaID = razaID;
    }
    public LocalDate getNacimiento() {
      return nacimiento;
    }
    public void setNacimiento(LocalDate nacimiento) {
      this.nacimiento = nacimiento;
    }
    public String getNombre() {
      return nombre;
    }
    public void setNombre(String nombre) {
      this.nombre = nombre;
    }
    public String getSexo() {
      return sexo;
    }
    public void setSexo(String sexo) {
      this.sexo = sexo;
    }

}
