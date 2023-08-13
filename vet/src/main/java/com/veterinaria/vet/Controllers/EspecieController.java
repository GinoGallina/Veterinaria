package com.veterinaria.vet.Controllers;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.veterinaria.vet.Models.Especie;
import com.veterinaria.vet.Services.EspecieService;


import jakarta.transaction.Transactional;

//@RestController
@Controller
@RequestMapping("/Especies")
public class EspecieController {
     @Autowired
    private EspecieService especieService;

     /*@GetMapping
     public ModelAndView getEspecies(){
      ArrayList<Especie> especies =  this.especieService.getAllEspecies();
      ArrayList<String> header = new ArrayList<>();
      header.add("Descripcion");
      ModelAndView modelAndView = new ModelAndView("Especie");
      
      //ObjectMapper objectMapper = new ObjectMapper();
      //objectMapper.registerModule(new JavaTimeModule()); 
      //String especies = objectMapper.writeValueAsString(especiesArray);
        modelAndView.addObject("especies", especies);
        modelAndView.addObject("header", header);
      return modelAndView;
     }*/
     @GetMapping
     public ModelAndView getEspecies(){
      ArrayList<Especie> especiesArray =  this.especieService.getAllEspecies();
      ArrayList<String> header = new ArrayList<>();
      header.add("Descripcion");
      ModelAndView modelAndView = new ModelAndView("Especie");
      
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.registerModule(new JavaTimeModule()); 
      String especies;
      try {
        especies = objectMapper.writeValueAsString(especiesArray);
        modelAndView.addObject("especiesJSON", especies);
        modelAndView.addObject("especiesNOJSON", especiesArray);
        modelAndView.addObject("header", header);
      } catch (JsonProcessingException e) {
        System.out.println("ERROR AL CONVERTIR A JSON");
        e.printStackTrace();
      }
      return modelAndView;
     }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getEspecieById(@RequestParam("id") Long id){
      Optional<Especie> existingEspecie = especieService.getById(id);
      if(!existingEspecie.isPresent()){
        // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario con ID " + id + " no fue encontrado.");
        return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok(existingEspecie);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Especie Especie){

      Optional<Especie> existingEspecie = especieService.findByDescripcion(Especie.getDescripcion());
      if(existingEspecie.isPresent()){
        if(!especieService.getById(existingEspecie.get().getID()).isPresent()){
          especieService.saveLogico(existingEspecie.get().getID());
          return ResponseEntity.ok(existingEspecie.get());
        }else{
          return ResponseEntity.badRequest().body("Ya existe un Especie con la misma matrícula");
        }
      }
      Especie savedEspecie = especieService.saveEspecie(Especie);
      return ResponseEntity.ok(savedEspecie);
    }

    @PutMapping()
    public ResponseEntity<?>  updateEspecie(@RequestBody Especie Especie){
      Optional<Especie> existingEspecie = especieService.findByDescripcion(Especie.getDescripcion());
      if(existingEspecie.isPresent()){
          return ResponseEntity.badRequest().body("Ya existe un Especie con la misma matrícula");
        
      }
       Especie updatedEspecie=this.especieService.updateById(Especie,(long) Especie.getID());
       return ResponseEntity.ok(updatedEspecie);
    }

    @DeleteMapping()
    @Transactional
    public ResponseEntity<?> eliminarEspecie(@RequestBody Especie Especie) throws Exception {
      Optional<Especie> especie = especieService.getById(Especie.getID());
      if(especie.isEmpty()){
        return ResponseEntity.notFound().build();
      }
      especieService.eliminarLogico(Especie.getID());
      return ResponseEntity.ok().build();
    }
    
}
