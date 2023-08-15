package com.veterinaria.vet.Controllers;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.veterinaria.vet.DTO.VeterinarioDTO;
import com.veterinaria.vet.Models.Veterinario;
import com.veterinaria.vet.Services.VeterinarioService;
import com.veterinaria.vet.Models.Response;


import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/Veterinarios")
public class VeterinarioController{
  
    @Autowired
    private VeterinarioService veterinarioService;



      @GetMapping
      public ModelAndView getVeterinarios(){
          ArrayList<Veterinario> veterinarios =  this.veterinarioService.getAllVeterinarios();
          ArrayList<String> header = new ArrayList<>();
          header.add("Matricula");
          header.add("Nombre");
          header.add("Apellido");
          header.add("Direccion");
          header.add("Telefono");
          header.add("User");
          ModelAndView modelAndView = new ModelAndView("Veterinario");
          modelAndView.addObject("veterinarios", veterinarios);
          modelAndView.addObject("header", header);
          return modelAndView;
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

      @PostMapping(produces = "application/json", consumes = "application/json")
      public ResponseEntity<Object> save(@Valid @RequestBody VeterinarioDTO veterinarioDTO) throws JsonProcessingException {
          Optional<Veterinario> existingVeterinario = veterinarioService.findByMatricula(veterinarioDTO.getMatricula());
          Response json = new Response();
          if (existingVeterinario.isPresent()) {
              if (!veterinarioService.getById(existingVeterinario.get().getID()).isPresent()) {
                  veterinarioService.saveLogico(existingVeterinario.get().getID());
                  json.setMessage("El Veterinario se encontraba eliminado y se ha recuperado");
                  json.setData(existingVeterinario.get().toJson());
                  return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
              } else {
                  json.setMessage("El Veterinario ingresado ya existe");
                  json.setTitle("ERROR");
                  return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST);
              }
          }
          Veterinario veterinario = new Veterinario();
          veterinario.setMatricula(veterinarioDTO.getMatricula());
          veterinario.setNombre(veterinarioDTO.getNombre());
          veterinario.setApellido(veterinarioDTO.getApellido());
          veterinario.setTelefono(veterinarioDTO.getTelefono());
          veterinario.setDireccion(veterinarioDTO.getDireccion());
          Veterinario savedVeterinario = veterinarioService.saveVeterinario(veterinario);
          json.setMessage("Se ha guardado el Veterinario");
          json.setData(savedVeterinario.toJson());
          return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
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


      @PutMapping(produces = "application/json", consumes = "application/json")
      public ResponseEntity<Object> updateVeterinario(@Validated(VeterinarioDTO.PutAndDelete.class) @RequestBody VeterinarioDTO veterinarioDTO) throws JsonProcessingException{
        Optional<Veterinario> existingVeterinario = veterinarioService.findByMatricula(veterinarioDTO.getMatricula());
        Response json = new Response();
        if(existingVeterinario.isPresent()){
            json.setMessage("El Veterinario ingresado ya existe");
            json.setTitle("ERROR");
            return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST); 
        }
        Veterinario veterinario = new Veterinario();
        veterinario.setID(veterinarioDTO.getID());
        veterinario.setMatricula(veterinarioDTO.getMatricula());
        veterinario.setNombre(veterinarioDTO.getNombre());
        veterinario.setApellido(veterinarioDTO.getApellido());
        veterinario.setTelefono(veterinarioDTO.getTelefono());
        veterinario.setDireccion(veterinarioDTO.getDireccion());
        Veterinario updatedVeterinario=this.veterinarioService.updateById(veterinario,(long) veterinario.getID());
        json.setMessage("Se ha actualizado el Veterinario");
        json.setData(updatedVeterinario.toJson());
        return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
      }

      @DeleteMapping(produces = "application/json", consumes = "application/json")
      @Transactional
      public ResponseEntity<Object> eliminarVeterinario(@Validated(VeterinarioDTO.PutAndDelete.class) @RequestBody VeterinarioDTO veterinarioDTO) throws JsonProcessingException {
        Optional<Veterinario> existingVeterinario = veterinarioService.getById(veterinarioDTO.getID());
        Response json = new Response();
        if(existingVeterinario.isEmpty()){
            json.setMessage("La Veterinario no existe");
            json.setTitle("ERROR");
            return new ResponseEntity<Object>(json.toJson(), HttpStatus.NOT_FOUND); 
        }
        Veterinario veterinario = new Veterinario();
        veterinario.setID(veterinarioDTO.getID());
        veterinario.setMatricula(veterinarioDTO.getMatricula());
        veterinario.setNombre(veterinarioDTO.getNombre());
        veterinario.setApellido(veterinarioDTO.getApellido());
        veterinario.setTelefono(veterinarioDTO.getTelefono());
        veterinario.setDireccion(veterinarioDTO.getDireccion());
        veterinarioService.eliminarLogico(veterinario.getID());
        json.setMessage("Se ha eliminado el veterinario");
        json.setData(existingVeterinario.get().toJson());
        return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
      }  





}