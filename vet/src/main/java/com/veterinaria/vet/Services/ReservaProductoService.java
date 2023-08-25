package com.veterinaria.vet.Services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veterinaria.vet.Models.ReservaProducto;
import com.veterinaria.vet.Repositories.ReservaProductoRepository;

@Service
public class ReservaProductoService {
    @Autowired
    ReservaProductoRepository reservaProductoRepository;

    public void saveProductos(List<ReservaProducto> productos) {
        reservaProductoRepository.saveAll(productos);
    }
}
