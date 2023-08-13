package com.veterinaria.vet.Repositories;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.veterinaria.vet.Models.Veterinario;

import jakarta.transaction.Transactional;
@Repository
public interface VeterinarioRepository extends JpaRepository<Veterinario, Long>{
  Optional<Veterinario> findByMatricula(String matricula);
  
  @Transactional
  @Modifying
  @Query(value = "UPDATE veterinarios v SET v.DeletedAt = CURRENT_TIMESTAMP WHERE v.id = :id",nativeQuery = true)
  void eliminarLogico(@Param("id") Long id);

  @Transactional
  @Modifying
  @Query(value = "UPDATE veterinarios v SET v.DeletedAt = null WHERE v.id = :id", nativeQuery = true)
  void saveLogico(@Param("id") Long id);

  ArrayList<Veterinario> findByDeletedAtIsNull();
  
  Optional<Veterinario> findByIDAndDeletedAtIsNull(Long id);
}
