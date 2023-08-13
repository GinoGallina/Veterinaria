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

import com.veterinaria.vet.Models.Raza;
import com.veterinaria.vet.Services.RazaService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/Razas")
public class RazaController {
    @Autowired
    private RazaService razaService;

    @GetMapping
    public ArrayList<Raza> getRazas(){
      return this.razaService.getAllRazas();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getRazaById(@RequestParam("id") Long id){
      Optional<Raza> existingRaza = razaService.getById(id);
      if(!existingRaza.isPresent()){
        // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario con ID " + id + " no fue encontrado.");
        return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok(existingRaza);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Raza Raza){

      Optional<Raza> existingRaza = razaService.findByDescripcion(Raza.getDescripcion());
      if(existingRaza.isPresent()){
        if(!razaService.getById(existingRaza.get().getID()).isPresent()){
          razaService.saveLogico(existingRaza.get().getID());
          return ResponseEntity.ok(existingRaza.get());
        }else{
        return ResponseEntity.badRequest().body("Ya existe un Raza con la misma descripcion");
      }}
      Raza savedRaza = razaService.saveRaza(Raza);
      return ResponseEntity.ok(savedRaza);
    }

    @PutMapping()
    public ResponseEntity<?>  updateRaza(@RequestBody Raza Raza){
      Optional<Raza> existingRaza = razaService.findByDescripcion(Raza.getDescripcion());
      if(existingRaza.isPresent() && (existingRaza.get().getID()!=Raza.getID())){
        return ResponseEntity.badRequest().body("Ya existe un Raza con la misma matrícula");
      }
       Raza updatedRaza=this.razaService.updateById(Raza,(long) Raza.getID());
       return ResponseEntity.ok(updatedRaza);
    }

    @DeleteMapping()
    @Transactional
    public ResponseEntity<?> eliminarRaza(@RequestBody Raza raza) throws Exception {
      Long id = (long) raza.getID();
      System.out.println(id);
      Optional<Raza> Raza = razaService.getById(id);
      System.out.println(Raza.get());
      if(Raza.isEmpty()){
        return ResponseEntity.notFound().build();
      }
      razaService.eliminarLogico(id);
      return ResponseEntity.ok().build();
    }
}
