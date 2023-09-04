package com.veterinaria.vet.Security.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.veterinaria.vet.Security.Models.User;

import jakarta.transaction.Transactional;
@Repository
public interface UserRepository extends JpaRepository<User,Long>{
  Optional<User> findByEmail(String email);

  Optional<User> findById(long id);

  @Transactional
  @Modifying
  @Query(value = "UPDATE users u SET u.DeletedAt = CURRENT_TIMESTAMP WHERE u.id = :id",nativeQuery = true)
  void eliminarLogico(@Param("id") Long id);

  @Transactional
  @Modifying
  @Query(value = "UPDATE users u SET u.DeletedAt = null WHERE u.id = :id",nativeQuery = true)
  void saveLogico(@Param("id") Long id);

  boolean existsByEmail(String email);
}
