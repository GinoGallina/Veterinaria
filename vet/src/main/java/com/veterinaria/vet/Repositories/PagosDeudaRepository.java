package com.veterinaria.vet.Repositories;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.veterinaria.vet.Models.PagosDeuda;

import jakarta.transaction.Transactional;

@Repository
public interface PagosDeudaRepository extends JpaRepository<PagosDeuda, Long>{

  
  @Transactional
  @Modifying
  @Query(value = "UPDATE mascotas m SET m.DeletedAt = CURRENT_TIMESTAMP WHERE m.id = :id",nativeQuery = true)
  void eliminarLogico(@Param("id") Long id);


  ArrayList<PagosDeuda> findByDeletedAtIsNull();
  
  Optional<PagosDeuda> findByIDAndDeletedAtIsNull(Long id);
}
