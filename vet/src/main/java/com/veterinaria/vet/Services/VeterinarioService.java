package com.veterinaria.vet.Services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veterinaria.vet.Models.Veterinario;
import com.veterinaria.vet.Repositories.VeterinarioRepository;

@Service
public class VeterinarioService {
   @Autowired
  VeterinarioRepository veterinarioRepository;

  public ArrayList<Veterinario> getTodosVeterinarios(){
    return (ArrayList<Veterinario>) veterinarioRepository.findAll();
  }

    public ArrayList<Veterinario> getAllVeterinarios(){
    return (ArrayList<Veterinario>) veterinarioRepository.findByDeletedAtIsNull();
  }

 /*public Optional<Veterinario> getById(Long id){
    return veterinarioRepository.findById(id);
  }*/
  public Optional<Veterinario> getById(Long id){
    return veterinarioRepository.findByIDAndDeletedAtIsNull(id);
  }

  public Veterinario updateById(Veterinario vet, Long id){
    Veterinario veterianrio= veterinarioRepository.findById(id).get();
    veterianrio.setNombre(vet.getNombre());
    veterianrio.setApellido(vet.getApellido());
    veterianrio.setMatricula(vet.getMatricula());
    veterianrio.setDireccion(vet.getDireccion());
    veterianrio.setTelefono(vet.getTelefono());
    veterianrio.setUpdatedAt(LocalDateTime.now());
    veterinarioRepository.save(veterianrio);
    return veterianrio;
  }

  public Veterinario saveVeterinario(Veterinario veterinario){
     return veterinarioRepository.save(veterinario);
  }

   public Optional<Veterinario> findByMatricula(String matricula){
     return veterinarioRepository.findByMatricula(matricula);
   }
   

  /*public Boolean deleteVeterinario(Long id){
    try{
      veterinarioRepository.deleteById(id);
      return true; 

    }catch(Exception e){
      return false;
    }*/
    public void eliminarLogico(Long id){
      veterinarioRepository.eliminarLogico(id);
    }
    
    public void saveLogico(Long id) {
      veterinarioRepository.saveLogico(id);
    }

}
