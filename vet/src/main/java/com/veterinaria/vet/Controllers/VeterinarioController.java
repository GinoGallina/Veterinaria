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

import com.veterinaria.vet.Models.Veterinario;
import com.veterinaria.vet.Services.VeterinarioService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/Veterinarios")
public class VeterinarioController{
  
    @Autowired
    private VeterinarioService veterinarioService;



    /*@GetMapping(path = "/{todos}")
    public ArrayList<Veterinario> getTodosVeterinarios(){
      return this.veterinarioService.getTodosVeterinarios();
    }*/

    @GetMapping
    public ArrayList<Veterinario> getVeterinarios(){
      return this.veterinarioService.getAllVeterinarios();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getVeterinarioById(@RequestParam("id") Long id){
      Optional<Veterinario> existingVeterinario = veterinarioService.getById(id);
      if(!existingVeterinario.isPresent()){
        // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario con ID " + id + " no fue encontrado.");
        return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok(existingVeterinario);
    }

    @PostMapping()
    public ResponseEntity<?> save(@RequestBody Veterinario Veterinario){

      Optional<Veterinario> existingVeterinario = veterinarioService.findByMatricula(Veterinario.getMatricula());
      if(existingVeterinario.isPresent()){
          if (!veterinarioService.getById(existingVeterinario.get().getID()).isPresent()) {
          veterinarioService.saveLogico(existingVeterinario.get().getID());
          return ResponseEntity.ok(existingVeterinario.get());
        } else {
        return ResponseEntity.badRequest().body("Ya existe un veterinario con la misma matrícula");
      }}
      Veterinario savedVeterinario = veterinarioService.saveVeterinario(Veterinario);
      return ResponseEntity.ok(savedVeterinario);
    }

    @PutMapping()
    public ResponseEntity<?>  updateVeterinario(@RequestBody Veterinario Veterinario){
      Optional<Veterinario> existingVeterinario = veterinarioService.findByMatricula(Veterinario.getMatricula());
      if(existingVeterinario.isPresent() && (existingVeterinario.get().getID()!=Veterinario.getID() )){
        return ResponseEntity.badRequest().body("Ya existe un veterinario con la misma matrícula");
      }
      Veterinario updatedVeterinario=this.veterinarioService.updateById(Veterinario,(long) Veterinario.getID());

       return ResponseEntity.ok(updatedVeterinario);
    }

    @DeleteMapping()
    @Transactional
    public ResponseEntity<?> eliminarVeterinario(@RequestBody Veterinario Veterinario) throws Exception {
      Long id= (long) Veterinario.getID();
      Optional<Veterinario> veterinario = veterinarioService.getById(id);
      if(veterinario.isEmpty()){
        return ResponseEntity.notFound().build();
      }
      veterinarioService.eliminarLogico(id);
      return ResponseEntity.ok().build();
    }
    

}