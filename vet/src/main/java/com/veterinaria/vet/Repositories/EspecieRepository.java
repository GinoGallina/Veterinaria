package com.veterinaria.vet.Repositories;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.veterinaria.vet.Models.Especie;

import jakarta.transaction.Transactional;


@Repository
public interface EspecieRepository extends JpaRepository<Especie, Long>{
  Optional<Especie> findByDescripcion(String desc);

  @Transactional
  @Modifying
  @Query(value = "UPDATE especies e SET e.DeletedAt = CURRENT_TIMESTAMP WHERE e.id = :id",nativeQuery = true)
  void eliminarLogico(@Param("id") Long id);

  @Transactional
  @Modifying
  @Query(value = "UPDATE especies e SET e.DeletedAt = null WHERE e.id = :id",nativeQuery = true)
  void saveLogico(@Param("id") Long id);

  ArrayList<Especie> findByDeletedAtIsNull();
  
  Optional<Especie> findByIDAndDeletedAtIsNull(Long id);
}
