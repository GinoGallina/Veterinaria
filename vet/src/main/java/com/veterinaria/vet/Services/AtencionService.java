package com.veterinaria.vet.Services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veterinaria.vet.Models.Atencion;
import com.veterinaria.vet.Repositories.AtencionRepository;

@Service
public class AtencionService {
  @Autowired
  AtencionRepository atencionRepository;

  public ArrayList<Atencion> getTodosAtencions(){
    return (ArrayList<Atencion>) atencionRepository.findAll();
  }

  public ArrayList<Atencion> getAllAtencions(){
    return (ArrayList<Atencion>) atencionRepository.findByDeletedAtIsNull();
  }


   public ArrayList<Atencion> getAllAtencionesCliente(long clienteID){
     return (ArrayList<Atencion>) atencionRepository.findByClienteIDAndDeletedAtIsNull(clienteID);
   }
  public Optional<Atencion> getById(Long id){
    return atencionRepository.findByIDAndDeletedAtIsNull(id);
  }

  public Atencion updateById(Atencion ate, Long id){
    Atencion Atencion= atencionRepository.findById(id).get();
    Atencion.setMascota(ate.getMascota());
    Atencion.setVeterinario(ate.getVeterinario());
    Atencion.setFechaPago(ate.getFechaPago());
    Atencion.setFechaAtencion(ate.getFechaAtencion());
    atencionRepository.save(Atencion);
    return Atencion;
  }

  public Atencion saveAtencion(Atencion cli){
     return atencionRepository.save(cli);
  }

   

    public void eliminarLogico(Long id){
      atencionRepository.eliminarLogico(id);
    }
    
    public void saveLogico(Long id) {
      atencionRepository.saveLogico(id);
    }
  }