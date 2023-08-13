package com.veterinaria.vet.Repositories;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.veterinaria.vet.Models.Raza;

import jakarta.transaction.Transactional;

@Repository
public interface RazaRepository extends JpaRepository<Raza, Long>{
  Optional<Raza> findByDescripcion(String desc);
  
  @Transactional
  @Modifying
  @Query(value = "UPDATE razas r SET r.DeletedAt = CURRENT_TIMESTAMP WHERE r.id = :id",nativeQuery = true)
  void eliminarLogico(@Param("id") Long id);

  @Transactional
  @Modifying
  @Query(value = "UPDATE razas e SET e.DeletedAt = null WHERE e.id = :id",nativeQuery = true)
  void saveLogico(@Param("id") Long id);

  ArrayList<Raza> findByDeletedAtIsNull();
  
  Optional<Raza> findByIDAndDeletedAtIsNull(Long id);
}
