package com.veterinaria.vet.Controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.veterinaria.vet.Models.Practica;
import com.veterinaria.vet.Models.Precio;
import com.veterinaria.vet.Services.PracticaService;
import com.veterinaria.vet.Services.PrecioService;


@RestController
@RequestMapping("/Practicas")
public class PracticaController {
    @Autowired
    private PracticaService practicaService;
    @Autowired
    private PrecioService precioService;



    @GetMapping
    public ArrayList<Practica> getPracticas(){
      return this.practicaService.getAllPracticas();
    }

    @GetMapping(path = "/elegidas")
    public ArrayList<Practica> getPracticasElegidas(@RequestParam("ids") List<Long> ids){
      return this.practicaService.getPracticasElegidas(ids);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getPracticaById(@RequestParam("id") Long id){
      Optional<Practica> existingPractica = practicaService.getById(id);
      if(!existingPractica.isPresent()){
        // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario con ID " + id + " no fue encontrado.");
        return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok(existingPractica);
    }

    @PostMapping()
    public ResponseEntity<?> save(@RequestBody Practica Practica,@RequestParam("precio") BigDecimal precio){
      Optional<Practica> existingPractica = practicaService.findByDescripcion(Practica.getDescripcion());
      if(existingPractica.isPresent()){
        if (!practicaService.getById(existingPractica.get().getID()).isPresent()) {
          practicaService.saveLogico(existingPractica.get().getID());
          return ResponseEntity.ok(existingPractica.get());
        } else {
        return ResponseEntity.badRequest().body("Ya existe un Practica con la misma descripcion");
      }}
      Practica savedPractica = practicaService.savePractica(Practica);
      Precio savedPrecio= new Precio(savedPractica,precio);
      precioService.savePrecio(savedPrecio);
      return ResponseEntity.ok(savedPractica);
    }

    @PutMapping()
    public ResponseEntity<?>  updatePractica(@RequestBody Practica Practica){
      Optional<Practica> existingPractica = practicaService.findByDescripcion(Practica.getDescripcion());
      if(existingPractica.isPresent() && (existingPractica.get().getID()!=Practica.getID() )){
        return ResponseEntity.badRequest().body("Ya existe un Practica con la misma descripcion");
      }
      Practica updatedPractica=this.practicaService.updateById(Practica,(long) Practica.getID());
       return ResponseEntity.ok(updatedPractica);
    }

    @DeleteMapping()
    @Transactional
    public ResponseEntity<?> eliminarPractica(@RequestBody Practica Practica) throws Exception {
      Long id= (long) Practica.getID();
      Optional<Practica> practica = practicaService.getById(id);
      if(practica.isEmpty()){
        return ResponseEntity.notFound().build();
      }
      practicaService.eliminarLogico(id);
      return ResponseEntity.ok().build();
    }
    

}
