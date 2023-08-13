package com.veterinaria.vet.Services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veterinaria.vet.Models.Raza;
import com.veterinaria.vet.Repositories.RazaRepository;



@Service
public class RazaService {
  @Autowired
  RazaRepository razaRepository;

  public ArrayList<Raza> getTodasRazas(){
    return (ArrayList<Raza>) razaRepository.findAll();
  }

  public ArrayList<Raza> getAllRazas(){
     return (ArrayList<Raza>) razaRepository.findByDeletedAtIsNull();
   }

 /*public Optional<Raza> getById(Long id){
    return razaRepository.findById(id);
  }*/
  public Optional<Raza> getById(Long id){
    return razaRepository.findByIDAndDeletedAtIsNull(id);
  }

  public Raza updateById(Raza ra, Long id){
    Raza Raza= razaRepository.findById(id).get();
    Raza.setDescripcion(ra.getDescripcion());
    Raza.setEspecie(ra.getEspecie());
    razaRepository.save(Raza);
    return Raza;
  }

  public Raza saveRaza(Raza Raza){
     return razaRepository.save(Raza);
  }

   public Optional<Raza> findByDescripcion(String desc){
     return razaRepository.findByDescripcion(desc);
   }
   

  /*public Boolean deleteRaza(Long id){
    try{
      razaRepository.deleteById(id);
      return true; 

    }catch(Exception e){
      return false;
    }*/
    public void eliminarLogico(Long id){
      razaRepository.eliminarLogico(id);
    }
    
    public void saveLogico(Long id) {
      razaRepository.saveLogico(id);
    }

}


