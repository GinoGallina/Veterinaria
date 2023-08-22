package com.veterinaria.vet.Repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.veterinaria.vet.Models.Practica;
import jakarta.transaction.Transactional;

@Repository
public interface PracticaRepository extends JpaRepository<Practica, Long> {
    Optional<Practica> findByDescripcion(String desc);

    @Transactional
    @Modifying
    @Query(value = "UPDATE practicas p SET p.DeletedAt = CURRENT_TIMESTAMP WHERE p.id = :id", nativeQuery = true)
    void eliminarLogico(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE practicas p SET p.DeletedAt = null WHERE p.id = :id", nativeQuery = true)
    void saveLogico(@Param("id") Long id);

    ArrayList<Practica> findByDeletedAtIsNull();

    Optional<Practica> findByIDAndDeletedAtIsNull(Long id);

    ArrayList<Practica> findByIDIn(List<Long> ids);
}
