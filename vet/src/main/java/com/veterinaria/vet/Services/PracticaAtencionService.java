package com.veterinaria.vet.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veterinaria.vet.Models.PracticaAtencion;
import com.veterinaria.vet.Repositories.PracticaAtencionRepository;

@Service
public class PracticaAtencionService {
  @Autowired
  PracticaAtencionRepository practicaAtencionRepository;

  public void delete(PracticaAtencion practicaAEliminar) {
    practicaAtencionRepository.delete(practicaAEliminar);

  }
  
}
