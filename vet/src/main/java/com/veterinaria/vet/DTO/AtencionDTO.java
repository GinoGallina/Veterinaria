package com.veterinaria.vet.DTO;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.veterinaria.vet.DTO.RazaDTO.PostAndPut;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;

public class AtencionDTO {
    @NotNull(message = "El campo id no puede estar vacío",groups = { PutAndDelete.class })
    private Long ID;

    @NotNull(message = "El campo especie no puede estar vacío",groups = { PostAndPut.class })
    private Long veterinarioID;

    @NotNull(message = "El campo especie no puede estar vacío", groups = { PostAndPut.class })
    private Long mascotaID;

    
    @NotNull(message = "El campo practicasID no puede estar vacío", groups = { PutAndPost.class })
    private List<Long> practicasID;

    //Nose si van
    //@PastOrPresent(message = "La fecha de atencion debe ser en el pasado o la fecha actual",groups = { PutAndPost.class })
    @NotNull(message = "La fecha de atencion no puede estar vacía",groups = { PutAndPost.class })
    private LocalDateTime fechaAtencion;

    //Nose si van
    //@PastOrPresent(message = "La fecha de pago debe ser en el pasado o la fecha actual", groups = {PutAndPost.class })
    //@NotNull(message = "La fecha de pago no puede estar vacía", groups = { PutAndPost.class })
    private LocalDateTime fechaPago; 

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

    public Long getVeterinarioID() {
      return veterinarioID;
    }

    public void setVeterinarioID(Long veterinarioID) {
      this.veterinarioID = veterinarioID;
    }

    public Long getMascotaID() {
      return mascotaID;
    }

    public void setMascotaID(Long mascotaID) {
      this.mascotaID = mascotaID;
    }



    public LocalDateTime getFechaAtencion() {
      return fechaAtencion;
    }

    public void setFechaAtencion(LocalDateTime fechaAtencion) {
      this.fechaAtencion = fechaAtencion;
    }

    public LocalDateTime getFechaPago() {
      return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
      this.fechaPago = fechaPago;
    }

    public List<Long> getPracticasID() {
      return practicasID;
    }

    public void setPracticasID(List<Long> practicasID) {
      this.practicasID = practicasID;
    }

    

}
