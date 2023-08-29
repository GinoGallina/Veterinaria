package com.veterinaria.vet.Controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.veterinaria.vet.Models.Precio;
import com.veterinaria.vet.Services.PrecioService;

@RestController
@RequestMapping("/Precios")
public class PrecioController {

    @Autowired
    private PrecioService precioService;

    @GetMapping
    public Precio getLastPrice(@RequestParam("id") Long id) {
        return this.precioService.getLastPrice(id);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getPrecioById(@RequestParam("id") Long id) {
        Optional<Precio> existingPrrecio = precioService.getById(id);
        if (!existingPrrecio.isPresent()) {
            // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario con ID "
            // + id + " no fue encontrado.");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(existingPrrecio);
    }
}
