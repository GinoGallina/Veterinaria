package com.veterinaria.vet.Services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veterinaria.vet.Models.Mascota;
import com.veterinaria.vet.Repositories.MascotaRepository;


@Service
public class MascotaService {
  @Autowired
  MascotaRepository mascotaRepository;

  public ArrayList<Mascota> getTodasMascotas(){
    return (ArrayList<Mascota>) mascotaRepository.findAll();
  }

  public ArrayList<Mascota> getAllMascotas(){
     return (ArrayList<Mascota>) mascotaRepository.findByDeletedAtIsNull();
   }
  
   public ArrayList<Mascota> getAllMascotasCliente(long id){
     return (ArrayList<Mascota>) mascotaRepository.findByClienteIDAndDeletedAtIsNull(id);
   }

 /*public Optional<Mascota> getById(Long id){
    return MascotaRepository.findById(id);
  }*/
  public Optional<Mascota> getById(Long id){
    return mascotaRepository.findByIDAndDeletedAtIsNull(id);
  }

  public Mascota updateById(Mascota mas, Long id){
    Mascota mascota= mascotaRepository.findById(id).get();
    mascota.setNombre(mas.getNombre());
    mascota.setNacimiento(mas.getNacimiento());
    mascota.setSexo(mas.getSexo());
    mascota.setRaza(mas.getRaza());
    mascota.setCliente(mas.getCliente());
    mascota.setUpdatedAt(LocalDateTime.now());
    mascotaRepository.save(mascota);
    return mascota;
  }

  public Mascota saveMascota(Mascota Mascota){
     return mascotaRepository.save(Mascota);
  }

  //  public Optional<Mascota> findByDescripcion(String desc){
  //    return mascotaRepository.findByDescripcion(desc);
  //  }
   

    public void eliminarLogico(Long id){
      mascotaRepository.eliminarLogico(id);
    }
    public void saveLogico(Long id){
      mascotaRepository.saveLogico(id);
    }

}
