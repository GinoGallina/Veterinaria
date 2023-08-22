package com.veterinaria.vet.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veterinaria.vet.Models.Practica;
import com.veterinaria.vet.Repositories.PracticaRepository;


@Service
public class PracticaService {
  
  @Autowired
  PracticaRepository practicaRepository;

  public ArrayList<Practica> getTodasPracticas(){
    return (ArrayList<Practica>) practicaRepository.findAll();
  }

  public ArrayList<Practica> getAllPracticas(){
     return (ArrayList<Practica>) practicaRepository.findByDeletedAtIsNull();
   }

  public ArrayList<Practica> getPracticasElegidas(List<Long> ids){
    return (ArrayList<Practica>) practicaRepository.findByIDIn(ids);
  }


 /*public Optional<Practica> getById(Long id){
    return practicaRepository.findById(id);
  }*/
  public Optional<Practica> getById(Long id){
    return practicaRepository.findByIDAndDeletedAtIsNull(id);
  }

  public Practica updateById(Practica es, Long id){
    Practica Practica= practicaRepository.findById(id).get();
    Practica.setDescripcion(es.getDescripcion());
    practicaRepository.save(Practica);
    return Practica;
  }

  public Practica savePractica(Practica Practica){
     return practicaRepository.save(Practica);
  }

   public Optional<Practica> findByDescripcion(String desc){
     return practicaRepository.findByDescripcion(desc);
   }
   

    public void eliminarLogico(Long id){
      practicaRepository.eliminarLogico(id);
    }

    public void saveLogico(Long id) {
      practicaRepository.saveLogico(id);
    }

}