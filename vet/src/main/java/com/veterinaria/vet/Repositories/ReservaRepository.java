package com.veterinaria.vet.Repositories;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.veterinaria.vet.Models.Reserva;
import jakarta.transaction.Transactional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE reservas r SET r.DeletedAt = CURRENT_TIMESTAMP WHERE r.id = :id", nativeQuery = true)
    void eliminarLogico(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE reservas  SET r.DeletedAt = null WHERE r.id = :id", nativeQuery = true)
    void saveLogico(@Param("id") Long id);

    @Query("SELECT r FROM Reserva r WHERE r.cliente.ID = :clienteID")
    ArrayList<Reserva> findByClienteIDAndDeletedAtIsNull(@Param("clienteID") Long clienteID);

    ArrayList<Reserva> findByDeletedAtIsNull();

    Optional<Reserva> findByIDAndDeletedAtIsNull(Long id);
}
