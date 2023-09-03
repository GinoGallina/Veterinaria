package com.veterinaria.vet.DTO;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.groups.Default;

public class AtencionDTO {
    @NotNull(message = "El campo id no puede estar vacío",groups = { PutAndDelete.class })
    private Long ID;

    @NotNull(message = "El campo mascota no puede estar vacío", groups = { PutAndPost.class })
    private Long mascotaID;

    
    @NotEmpty(message = "Debe seleccionar al menos una práctica", groups = { PutAndPost.class })
    private List<Long> practicasID;


    @PastOrPresent(message = "La fecha de atencion debe ser en el pasado o la fecha actual",groups = { PutAndPost.class })
    @NotNull(message = "La fecha de atencion no puede estar vacía",groups = { PutAndPost.class })
    private LocalDate fechaAtencion;


    private LocalDate fechaPago; 

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

    public Long getMascotaID() {
      return mascotaID;
    }

    public void setMascotaID(Long mascotaID) {
      this.mascotaID = mascotaID;
    }

    public LocalDate getFechaAtencion() {
      return fechaAtencion;
    }

    public void setFechaAtencion(LocalDate fechaAtencion) {
      this.fechaAtencion = fechaAtencion;
    }

    public LocalDate getFechaPago() {
      return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
      this.fechaPago = fechaPago;
    }

    public List<Long> getPracticasID() {
      return practicasID;
    }

    public void setPracticasID(List<Long> practicasID) {
      this.practicasID = practicasID;
    }

}
