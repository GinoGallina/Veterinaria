package com.veterinaria.vet.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.veterinaria.vet.Models.ReservaProducto;

@Repository
public interface ReservaProductoRepository extends JpaRepository<ReservaProducto, Long>{
  
}
