package com.veterinaria.vet.DTO;

import java.math.BigDecimal;

import com.veterinaria.vet.Models.Producto;
import com.veterinaria.vet.Models.Reserva;
import com.veterinaria.vet.Models.ReservaProductoId;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;

public class ReservaProdutcoDTO {
    @NotNull(message = "El campo id no puede estar vacío",groups = { PutAndDelete.class })
    private ReservaProductoId id;
    @NotNull(message = "El campo id reserva no puede estar vacío",groups = { PutAndPost.class })
    private Long reservaID;
    @NotNull(message = "El campo id producto no puede estar vacío",groups = { PutAndPost.class })
    private Long productoID;
    @NotNull(message = "El campo precio no puede estar vacía", groups = { PutAndPost.class })
    private BigDecimal precio;
    @NotNull(message = "El campo razonSocial no puede estar vacía", groups = { PutAndPost.class })
    private int cantidad;

    public interface PutAndDelete extends Default {
    }

    public interface PutAndPost extends Default {
    }
}
