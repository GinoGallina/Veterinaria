package com.veterinaria.vet.Repositories;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.veterinaria.vet.Models.Atencion;


import jakarta.transaction.Transactional;

public interface AtencionRepository extends JpaRepository<Atencion, Long>{
  //VER
  //Optional<Atencion> findByDni(String dni);
  
  @Query("SELECT a FROM Atencion a WHERE a.mascota.cliente.ID = :clienteID")
  ArrayList<Atencion> findByClienteID(@Param("clienteID") Long clienteID);

  @Transactional
  @Modifying
  @Query(value = "UPDATE atenciones a SET a.DeletedAt = CURRENT_TIMESTAMP WHERE a.id = :id",nativeQuery = true)
  void eliminarLogico(@Param("id") Long id);

  @Transactional
  @Modifying
  @Query(value = "UPDATE atenciones a SET a.DeletedAt = null WHERE a.id = :id", nativeQuery = true)
  void saveLogico(@Param("id") Long id);

  ArrayList<Atencion> findByDeletedAtIsNull();
  
  Optional<Atencion> findByIDAndDeletedAtIsNull(Long id);


}