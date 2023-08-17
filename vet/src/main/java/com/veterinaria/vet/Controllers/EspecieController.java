package com.veterinaria.vet.Controllers;

import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.veterinaria.vet.DTO.EspecieDTO;
import com.veterinaria.vet.Models.Especie;
import com.veterinaria.vet.Models.Response;
import com.veterinaria.vet.Services.EspecieService;

import jakarta.transaction.Transactional;


//@RestController
@Controller
@RequestMapping("/Especies")
public class EspecieController {
        @Autowired
        private EspecieService especieService;


        @GetMapping(path = "/Index")
        public ModelAndView getEspecies(){
            ArrayList<Especie> especies =  this.especieService.getAllEspecies();
            ArrayList<String> header = new ArrayList<>();
            header.add("Descripcion");
            ModelAndView modelAndView = new ModelAndView("Especies/Index");
            modelAndView.addObject("especies", especies);
            modelAndView.addObject("header", header);
            return modelAndView;
        }



        @PostMapping(produces = "application/json", consumes = "application/json")
        public ResponseEntity<Object> save(@Validated(EspecieDTO.PutAndPost.class) @RequestBody EspecieDTO especieDTO) throws JsonProcessingException {
            Optional<Especie> existingEspecie = especieService.findByDescripcion(especieDTO.getDescripcion());
            Response json = new Response();
            if (existingEspecie.isPresent()) {
                if (!especieService.getById(existingEspecie.get().getID()).isPresent()) {
                    especieService.saveLogico(existingEspecie.get().getID());
                    json.setMessage("La especie se encontraba eliminada y se ha recuperado");
                    json.setData(existingEspecie.get().toJson());
                    return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
                } else {
                    json.setMessage("La especie ingresada ya existe");
                    json.setTitle("ERROR");
                    return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST);
                }
            }
            Especie especie = new Especie();
            especie.setDescripcion(especieDTO.getDescripcion());
            Especie savedEspecie = especieService.saveEspecie(especie);
            json.setMessage("Se ha guardado la especie");
            json.setData(savedEspecie.toJson());
            return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
        }

        @PutMapping(produces = "application/json", consumes = "application/json")
        public ResponseEntity<Object> updateEspecie(@Validated({EspecieDTO.PutAndDelete.class,EspecieDTO.PutAndPost.class}) @RequestBody EspecieDTO especieDTO) throws JsonProcessingException{
            Optional<Especie> existingEspecie = especieService.findByDescripcion(especieDTO.getDescripcion());
            Response json = new Response();
            if(existingEspecie.isPresent()){
                json.setMessage("La especie ingresada ya existe");
                json.setTitle("ERROR");
                return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST); 
            }
            Especie especie = new Especie();
            especie.setID(especieDTO.getID());
            especie.setDescripcion(especieDTO.getDescripcion());
            Especie updatedEspecie=this.especieService.updateById(especie,(long) especie.getID());
            json.setMessage("Se ha actualizado la especie");
            json.setData(updatedEspecie.toJson());
            return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
        }

        @DeleteMapping(produces = "application/json", consumes = "application/json")
        @Transactional
        public ResponseEntity<Object> eliminarEspecie(@Validated(EspecieDTO.PutAndDelete.class) @RequestBody EspecieDTO especieDTO) throws JsonProcessingException {
            Optional<Especie> existingEspecie = especieService.getById(especieDTO.getID());
            Response json = new Response();
            if(existingEspecie.isEmpty()){
                json.setMessage("La especie no existe");
                json.setTitle("ERROR");
                return new ResponseEntity<Object>(json.toJson(), HttpStatus.NOT_FOUND); 
            }
            especieService.eliminarLogico(especieDTO.getID());
            json.setMessage("Se ha eliminado la especie");
            json.setData(existingEspecie.get().toJson());
            return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
        }  
}
