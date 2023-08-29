package com.veterinaria.vet.Repositories;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.veterinaria.vet.Models.Deuda;

import jakarta.transaction.Transactional;

@Repository
public interface DeudaRepository extends JpaRepository<Deuda, Long> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE deudas d SET d.DeletedAt = CURRENT_TIMESTAMP WHERE d.id = :id", nativeQuery = true)
    void eliminarLogico(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE deudas d SET d.DeletedAt = null WHERE d.id = :id", nativeQuery = true)
    void saveLogico(@Param("id") Long id);

    ArrayList<Deuda> findByDeletedAtIsNull();
    
    Optional<Deuda> findByIDAndDeletedAtIsNull(Long id);
    
    ArrayList<Deuda> findByProveedorIDAndDeletedAtIsNull(Long id);
}
