package com.veterinaria.vet.Security.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.veterinaria.vet.Security.Models.User;
@Repository
public interface UserRepository extends JpaRepository<User,Long>{
  //public Optional<User> findByUsername(String username);
  Optional<User> findByEmail(String email);

  Optional<User> findById(long id);
  
  //Optional<User> findByUsernameOrEmail(String nombreUsuario, String email);

  //Optional<User> findByTokenPassword(String tokenPassword);
  Optional<User> findByUserToken(String userToken);

  //boolean existsByUsername(String nombreUsuario);

  boolean existsByEmail(String email);
}
