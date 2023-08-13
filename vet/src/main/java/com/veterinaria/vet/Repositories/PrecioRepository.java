package com.veterinaria.vet.Repositories;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.veterinaria.vet.Models.Precio;

@Repository
public interface PrecioRepository extends JpaRepository<Precio, Long>{

  
  Precio findFirstByPracticaIDOrderByCreatedAtDesc(Long idProducto);
}
