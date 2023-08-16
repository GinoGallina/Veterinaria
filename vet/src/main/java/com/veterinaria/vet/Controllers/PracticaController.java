package com.veterinaria.vet.Controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.veterinaria.vet.DTO.EspecieDTO;
import com.veterinaria.vet.DTO.PracticaDTO;
import com.veterinaria.vet.Models.Especie;
import com.veterinaria.vet.Models.Practica;
import com.veterinaria.vet.Models.Precio;
import com.veterinaria.vet.Models.Practica;
import com.veterinaria.vet.Models.Response;
import com.veterinaria.vet.Services.PracticaService;
import com.veterinaria.vet.Services.PrecioService;


@RestController
@RequestMapping("/Practicas")
public class PracticaController {
      @Autowired
      private PracticaService practicaService;
      @Autowired
      private PrecioService precioService;




      @GetMapping(path = "/elegidas")
      public ArrayList<Practica> getPracticasElegidas(@RequestParam("ids") List<Long> ids){
        return this.practicaService.getPracticasElegidas(ids);
      }

      @GetMapping
      public ModelAndView getPracticas(){
          ArrayList<Practica> practicas =  this.practicaService.getAllPracticas();

          ArrayList<String> header = new ArrayList<>();
          header.add("Descripcion");
          header.add("Precio");
          ModelAndView modelAndView = new ModelAndView("Practica");
          modelAndView.addObject("practicas", practicas);
          modelAndView.addObject("header", header);
          return modelAndView;
      }

      /*   @PostMapping(produces = "application/json", consumes = "application/json")
        public ResponseEntity<Object> save(@Validated(PracticaDTO.PutAndPost.class) @RequestBody PracticaDTO practicaDTO) throws JsonProcessingException {
            Optional<Practica> existingPractica = practicaService.findByDescripcion(practicaDTO.getDescripcion());
            Response json = new Response();
            if (existingPractica.isPresent()) {
                if (!practicaDTO.getById(existingPractica.get().getID()).isPresent()) {
                    practicaDTO.saveLogico(existingPractica.get().getID());
                    json.setMessage("La especie se encontraba eliminada y se ha recuperado");
                    json.setData(existingPractica.get().toJson());
                    return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
                } else {
                    json.setMessage("La especie ingresada ya existe");
                    json.setTitle("ERROR");
                    return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST);
                }
            }
            Especie especie = new Especie();
            especie.setDescripcion(practicaDTO.getDescripcion());
            Especie savedEspecie = practicaService.saveEspecie(especie);
            json.setMessage("Se ha guardado la especie");
            json.setData(savedEspecie.toJson());
            return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
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
    }*/

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
