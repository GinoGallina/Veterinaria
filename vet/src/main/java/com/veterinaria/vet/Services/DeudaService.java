package com.veterinaria.vet.Services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veterinaria.vet.Models.Deuda;
import com.veterinaria.vet.Repositories.DeudaRepository;

@Service
public class DeudaService {
    @Autowired
    DeudaRepository deudaRepository;

    public ArrayList<Deuda> getTodasDeudas() {
        return (ArrayList<Deuda>) deudaRepository.findAll();
    }

    public ArrayList<Deuda> getAllDeudas() {
        return (ArrayList<Deuda>) deudaRepository.findByDeletedAtIsNull();
    }

    public ArrayList<Deuda> getDeudasByProveedorId(Long id) {
        return (ArrayList<Deuda>) deudaRepository.findByProveedorIDAndDeletedAtIsNull(id);
    }

    public Optional<Deuda> getById(Long id) {
        return deudaRepository.findByIDAndDeletedAtIsNull(id);
    }

    public Deuda saveDeuda(Deuda deuda) {
        return deudaRepository.save(deuda);
    }

    public void eliminarLogico(Long id) {
        deudaRepository.eliminarLogico(id);
    }

    public void saveLogico(Long id) {
        deudaRepository.saveLogico(id);
    }
}
