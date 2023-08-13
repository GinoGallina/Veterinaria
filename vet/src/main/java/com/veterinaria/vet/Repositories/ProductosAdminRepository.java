package com.veterinaria.vet.Repositories;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.veterinaria.vet.Models.Producto;

@Repository
public interface ProductosAdminRepository extends JpaRepository<Producto, Long>{
  
  Optional<Producto> findByDescripcion(String desc);

  @Modifying
  @Query(value = "UPDATE productos p SET p.DeletedAt = CURRENT_TIMESTAMP WHERE p.id = :id",nativeQuery = true)
  void eliminarLogico(@Param("id") Long id);

  ArrayList<Producto> findByDeletedAtIsNull();
  
  Optional<Producto> findByIDAndDeletedAtIsNull(Long id);
}