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

import com.veterinaria.vet.Models.Atencion;
import com.veterinaria.vet.Services.AtencionService;


import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/Atenciones")
public class AtencionController {
    @Autowired
    private AtencionService atencionService;


    @GetMapping(path = "/cliente")
    public ArrayList<Atencion> getAtencionesCliente(@RequestParam("id") Long id){
      return this.atencionService.getAllAtencionesCliente(id);
    }



    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getAtencionById(@RequestParam("id") Long id){
      Optional<Atencion> existingAtencion = atencionService.getById(id);
      if(!existingAtencion.isPresent()){
        // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario con ID " + id + " no fue encontrado.");
        return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok(existingAtencion);
    }

    @PostMapping()
    public ResponseEntity<?> save(@RequestBody Atencion Atencion){

      // Optional<Atencion> existingAtencion = atencionService.findByDni(Atencion.getDni());
      // if(existingAtencion.isPresent()){
      //   if (!atencionService.getById(existingAtencion.get().getID()).isPresent()) {
      //     atencionService.saveLogico(existingAtencion.get().getID());
      //     return ResponseEntity.ok(existingAtencion.get());
      //   } else {
      //   return ResponseEntity.badRequest().body("Ya existe un Atencion con el mismo dni");
      // }}
      Atencion savedAtencion = atencionService.saveAtencion(Atencion);
      return ResponseEntity.ok(savedAtencion);
    }

    @PutMapping()
    public ResponseEntity<?>  updateAtencion(@RequestBody Atencion Atencion){
      // Optional<Atencion> existingAtencion = atencionService.findByDni(Atencion.getDni());
      // if(existingAtencion.isPresent() && (existingAtencion.get().getID()!=Atencion.getID() )){
      //   return ResponseEntity.badRequest().body("Ya existe un Atencion con la misma matrícula");
      // }
      Atencion updatedAtencion=this.atencionService.updateById(Atencion,(long) Atencion.getID());

       return ResponseEntity.ok(updatedAtencion);
    }

    @DeleteMapping()
    @Transactional
    public ResponseEntity<?> eliminarAtencion(@RequestBody Atencion Atencion) throws Exception {
      Long id= (long) Atencion.getID();
      Optional<Atencion> atencion = atencionService.getById(id);
      if(atencion.isEmpty()){
        return ResponseEntity.notFound().build();
      }
      atencionService.eliminarLogico(id);
      return ResponseEntity.ok().build();
    }
    

}

