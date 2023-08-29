package com.veterinaria.vet.Services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veterinaria.vet.Models.Especie;
import com.veterinaria.vet.Repositories.EspecieRepository;


@Service
public class EspecieService {
  @Autowired
  EspecieRepository especieRepository;

  public ArrayList<Especie> getTodasEspecies(){
    return (ArrayList<Especie>) especieRepository.findAll();
  }

  public ArrayList<Especie> getAllEspecies(){
     return (ArrayList<Especie>) especieRepository.findByDeletedAtIsNull();
   }

 /*public Optional<Especie> getById(Long id){
    return especieRepository.findById(id);
  }*/
  public Optional<Especie> getById(Long id){
    return especieRepository.findByIDAndDeletedAtIsNull(id);
  }

  public Especie updateById(Especie es, Long id){
    Especie especie= especieRepository.findById(id).get();
    especie.setDescripcion(es.getDescripcion());
    especieRepository.save(especie);
    return especie;
  }

  public Especie saveEspecie(Especie Especie){
     return especieRepository.save(Especie);
  }

   public Optional<Especie> findByDescripcion(String desc){
     return especieRepository.findByDescripcion(desc);
   }
   

    public void eliminarLogico(Long id){
      especieRepository.eliminarLogico(id);
    }
    public void saveLogico(Long id){
      especieRepository.saveLogico(id);
    }

}
