package com.veterinaria.vet.DTO;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.veterinaria.vet.Models.ReservaProducto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;

public class ReservaDTO {
    @NotNull(message = "El campo ID no puede estar vacío",groups = { PutAndDelete.class })
    private Long ID;

    @NotNull(message = "El campo ID cliente no puede estar vacío",groups = { PutAndPost.class })
    private Long clienteID;

    @NotEmpty(message = "El campo descripción no puede estar vacío", groups = { PutAndPost.class })
    @NotNull(message = "El campo descripción no puede estar vacío", groups = { PutAndPost.class })
    private List<ReservaProducto> reservasProductos;

    public String toJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); 
        return objectMapper.writeValueAsString(this);
    }
    public interface PutAndDelete extends Default {
    }

    public interface PutAndPost extends Default {
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

    public List<ReservaProducto> getReservasProductos() {
      return reservasProductos;
    }

    public void setReservasProductos(List<ReservaProducto> reservasProductos) {
      this.reservasProductos = reservasProductos;
    }

  }
