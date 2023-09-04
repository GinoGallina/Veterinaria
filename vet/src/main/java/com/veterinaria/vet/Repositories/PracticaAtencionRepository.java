package com.veterinaria.vet.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.veterinaria.vet.Models.PracticaAtencion;

@Repository
public interface PracticaAtencionRepository extends JpaRepository<PracticaAtencion, Long>{
  
}
