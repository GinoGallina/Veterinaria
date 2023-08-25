package com.veterinaria.vet.Repositories;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.veterinaria.vet.Models.Cliente;

import jakarta.transaction.Transactional;

@Repository
public interface ClienteReposioty extends JpaRepository<Cliente, Long>{
  Optional<Cliente> findByDni(String dni);
  
  @Transactional
  @Modifying
  @Query(value = "UPDATE clientes c SET c.DeletedAt = CURRENT_TIMESTAMP WHERE c.id = :id",nativeQuery = true)
  void eliminarLogico(@Param("id") Long id);

  @Transactional
  @Modifying
  @Query(value = "UPDATE clientes c SET c.DeletedAt = null WHERE c.id = :id", nativeQuery = true)
  void saveLogico(@Param("id") Long id);

  ArrayList<Cliente> findByDeletedAtIsNull();
  
  Optional<Cliente> findByIDAndDeletedAtIsNull(Long id);

  Optional<Cliente> findByUserIDAndDeletedAtIsNull(Long id);
}
