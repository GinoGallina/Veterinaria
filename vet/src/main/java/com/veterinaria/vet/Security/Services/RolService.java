package com.veterinaria.vet.Security.Services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veterinaria.vet.Security.Models.Rol;
import com.veterinaria.vet.Security.Repositories.RolRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RolService {

    @Autowired
    RolRepository rolRepository;

    public Optional<Rol> getByRolNombre(String desc){
        return rolRepository.findByDescripcion(desc);
    }

    public void save(Rol rol){
        rolRepository.save(rol);
    }
}