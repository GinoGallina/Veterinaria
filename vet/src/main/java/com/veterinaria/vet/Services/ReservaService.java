package com.veterinaria.vet.Services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veterinaria.vet.Models.Reserva;
import com.veterinaria.vet.Repositories.ReservaRepository;

@Service
public class ReservaService {
    @Autowired
    ReservaRepository reservaRepository;

    public ArrayList<Reserva> getTodosReservas() {
        return (ArrayList<Reserva>) reservaRepository.findAll();
    }

    public ArrayList<Reserva> getAllReservas() {
        return (ArrayList<Reserva>) reservaRepository.findByDeletedAtIsNull();
    }

    public ArrayList<Reserva> getReservasCliente(Long clienteID) {
        return (ArrayList<Reserva>) reservaRepository.findByClienteIDAndDeletedAtIsNull(clienteID);
    }

    public Optional<Reserva> getById(Long id) {
        return reservaRepository.findByIDAndDeletedAtIsNull(id);
    }

    public Reserva updateById(Reserva res, Long id) {
        Reserva reserva = reservaRepository.findById(id).get();
        reserva.setCliente(res.getCliente());
        reserva.setReservasProductos(res.getReservasProductos());
        reservaRepository.save(reserva);
        return reserva;
    }

    public Reserva saveReserva(Reserva Reserva) {
        return reservaRepository.save(Reserva);
    }

    public void eliminarLogico(Long id) {
        reservaRepository.eliminarLogico(id);
    }

    public void saveLogico(Long id) {
        reservaRepository.saveLogico(id);
    }

}