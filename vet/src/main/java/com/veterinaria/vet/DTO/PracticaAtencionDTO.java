package com.veterinaria.vet.DTO;

import java.math.BigDecimal;

import com.veterinaria.vet.Models.PracticaAtencionId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;

public class PracticaAtencionDTO {
    @NotNull(message = "El campo id no puede estar vacío",groups = { PutAndDelete.class })
    private PracticaAtencionId id;
    @NotNull(message = "El campo id practica no puede estar vacío",groups = { PutAndPost.class })
    private Long practicaID;
    @NotNull(message = "El campo id atencion no puede estar vacío",groups = { PutAndPost.class })
    private Long atencionID;
    @NotNull(message = "El campo precio no puede estar vacía", groups = { PutAndPost.class })
    private BigDecimal precioPactado;


    public interface PutAndDelete extends Default {
    }

    public interface PutAndPost extends Default {
    }  
}
