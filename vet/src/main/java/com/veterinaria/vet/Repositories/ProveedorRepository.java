package com.veterinaria.vet.Repositories;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.veterinaria.vet.Models.Proveedor;
@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long>{
  
  Optional<Proveedor> findByCuil(String desc);
  
  Optional<Proveedor> findByEmail(String desc);

  @Modifying
  @Query(value = "UPDATE proveedores p SET p.DeletedAt = CURRENT_TIMESTAMP WHERE p.id = :id",nativeQuery = true)
  void eliminarLogico(@Param("id") Long id);
  

  ArrayList<Proveedor> findByDeletedAtIsNull();
  
  Optional<Proveedor> findByIDAndDeletedAtIsNull(Long id);
}
