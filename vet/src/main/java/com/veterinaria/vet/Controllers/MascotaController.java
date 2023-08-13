package com.veterinaria.vet.Controllers;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.veterinaria.vet.Models.Mascota;
import com.veterinaria.vet.Services.MascotaService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/Mascotas")
public class MascotaController {
    @Autowired
    private MascotaService mascotaService;

    @GetMapping
    public ArrayList<Mascota> getMascotas(){
      return this.mascotaService.getAllMascotas();
    }
    
    @GetMapping(path = "/cliente")
    public ArrayList<Mascota> getMascotasCliente(@RequestParam("id") Long id){
      return this.mascotaService.getAllMascotasCliente(id);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getMascotaById(@RequestParam("id") Long id){
      Optional<Mascota> existingMascota = mascotaService.getById(id);
      if(!existingMascota.isPresent()){
        // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario con ID " + id + " no fue encontrado.");
        return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok(existingMascota);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Mascota Mascota){
      
      // VER QUE CAMPOS NO REPETIR


      // Optional<Mascota> existingMascota = MascotaService.findByDescripcion(Mascota.getDescripcion());
      // if(existingMascota.isPresent()){
      //   if(!mascotaService.getById(existingMascota.get().getID()).isPresent()){
      //     mascotaService.saveLogico(existingMascota.get().getID());
      //     return ResponseEntity.ok(existingMascota.get());
      //   }else{
      //     return ResponseEntity.badRequest().body("Ya existe un Mascota con la misma matrícula");
      //   }
      // }
      Mascota savedMascota = mascotaService.saveMascota(Mascota);
      return ResponseEntity.ok(savedMascota);
    }

    @PutMapping()
    public ResponseEntity<?>  updateMascota(@RequestBody Mascota Mascota){
      // Optional<Mascota> existingMascota = MascotaService.findByDescripcion(Mascota.getDescripcion());
      // if(existingMascota.isPresent()){
      //     return ResponseEntity.badRequest().body("Ya existe un Mascota con la misma matrícula");
        
      // }
       Mascota updatedMascota=this.mascotaService.updateById(Mascota,(long) Mascota.getID());
       return ResponseEntity.ok(updatedMascota);
    }

    @DeleteMapping()
    @Transactional
    public ResponseEntity<?> eliminarMascota(@RequestBody Mascota Mascota) throws Exception {
      Optional<Mascota> mascota = mascotaService.getById(Mascota.getID());
      if(mascota.isEmpty()){
        return ResponseEntity.notFound().build();
      }
      mascotaService.eliminarLogico(Mascota.getID());
      return ResponseEntity.ok().build();
    }
    
}
