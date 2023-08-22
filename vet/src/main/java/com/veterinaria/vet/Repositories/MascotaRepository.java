package com.veterinaria.vet.Repositories;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.veterinaria.vet.Models.Mascota;

import jakarta.transaction.Transactional;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long>{
 
  ArrayList<Mascota> findByClienteIDAndDeletedAtIsNull(Long clienteId);
  
  @Transactional
  @Modifying
  @Query(value = "UPDATE mascotas m SET m.DeletedAt = CURRENT_TIMESTAMP WHERE m.id = :id",nativeQuery = true)
  void eliminarLogico(@Param("id") Long id);

  @Transactional
  @Modifying
  @Query(value = "UPDATE mascotas m SET m.DeletedAt = null WHERE m.id = :id", nativeQuery = true)
  void saveLogico(@Param("id") Long id);

  ArrayList<Mascota> findByDeletedAtIsNull();
  
  Optional<Mascota> findByIDAndDeletedAtIsNull(Long id);
}
