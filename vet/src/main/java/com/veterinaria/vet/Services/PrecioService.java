package com.veterinaria.vet.Services;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veterinaria.vet.Models.Precio;
import com.veterinaria.vet.Repositories.PrecioRepository;

@Service
public class PrecioService {
  @Autowired
  PrecioRepository precioRepository;

  public Precio getLastPrice(Long id){
     return (Precio) precioRepository.findFirstByPracticaIDOrderByCreatedAtDesc(id);
   }
  public Optional<Precio> getById(Long id){
    return precioRepository.findById(id);
  }
  public Precio savePrecio(Precio Precio){
     return precioRepository.save(Precio);
  }
}
