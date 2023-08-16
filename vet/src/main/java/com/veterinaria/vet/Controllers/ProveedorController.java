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
import com.veterinaria.vet.DTO.ProveedorDTO;
import com.veterinaria.vet.Models.Proveedor;
import com.veterinaria.vet.Models.Response;
import com.veterinaria.vet.Services.ProveedorService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/Proveedores")
public class ProveedorController {
  
    @Autowired
    private ProveedorService proveedorService;


      @GetMapping
      public ModelAndView getProveedores(){
          ArrayList<Proveedor> proveedores =  this.proveedorService.getAllProveedores();
          ArrayList<String> header = new ArrayList<>();
          header.add("CUIL");
          header.add("Dirección");
          header.add("Email");
          header.add("Teléfono");
          header.add("Razón Social");
          ModelAndView modelAndView = new ModelAndView("Proveedor");
          modelAndView.addObject("proveedores", proveedores);
          modelAndView.addObject("header", header);
          return modelAndView;
      }



      @PostMapping(produces = "application/json", consumes = "application/json")
      public ResponseEntity<Object> save(@Validated(ProveedorDTO.PutAndPost.class) @RequestBody ProveedorDTO proveedorDTO) throws JsonProcessingException {
          Optional<Proveedor> existingProveedorEmail = proveedorService.findByEmail(proveedorDTO.getEmail());
          Optional<Proveedor> existingProveedorCuil = proveedorService.findByCuil(proveedorDTO.getCuil());
          Optional<Proveedor> existingProveedor = existingProveedorEmail.or(() -> existingProveedorCuil);
          Response json = new Response();
          if (existingProveedor.isPresent()) {
              if (!proveedorService.getById(existingProveedor.get().getID()).isPresent()) {
                  proveedorService.saveLogico(existingProveedor.get().getID());
                  json.setMessage("El Proveedor se encontraba eliminado y se ha recuperado");
                  json.setData(existingProveedor.get().toJson());
                  return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
              } else {
                  json.setMessage("El Proveedor ingresado ya existe");
                  json.setTitle("ERROR");
                  return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST);
              }
          }
          Proveedor proveedor = new Proveedor();
          proveedor.setCuil(proveedorDTO.getCuil());
          proveedor.setDireccion(proveedorDTO.getDireccion());
          proveedor.setTelefono(proveedorDTO.getTelefono());
          proveedor.setEmail(proveedorDTO.getEmail());
          proveedor.setRazonSocial(proveedorDTO.getRazonSocial());
          Proveedor savedProveedor = proveedorService.saveProveedor(proveedor);
          json.setMessage("Se ha guardado el Proveedor");
          json.setData(savedProveedor.toJson());
          return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
    }




      @PutMapping(produces = "application/json", consumes = "application/json")
      public ResponseEntity<Object> updateProveedor(@Validated({ProveedorDTO.PutAndDelete.class,ProveedorDTO.PutAndPost.class}) @RequestBody ProveedorDTO proveedorDTO) throws JsonProcessingException{
        Optional<Proveedor> existingProveedorEmail = proveedorService.findByEmail(proveedorDTO.getEmail());
        Optional<Proveedor> existingProveedorCuil = proveedorService.findByCuil(proveedorDTO.getCuil());
        Optional<Proveedor> existingProveedor = existingProveedorEmail.or(() -> existingProveedorCuil);
        Response json = new Response();
        if(existingProveedor.isPresent()){
            json.setMessage("El Proveedor ingresado ya existe (MAIL O CUIL)");
            json.setTitle("ERROR");
            return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST); 
        }
          Proveedor proveedor = new Proveedor();
          proveedor.setID(proveedorDTO.getID());
          proveedor.setCuil(proveedorDTO.getCuil());
          proveedor.setDireccion(proveedorDTO.getDireccion());
          proveedor.setTelefono(proveedorDTO.getTelefono());
          proveedor.setEmail(proveedorDTO.getEmail());
          proveedor.setRazonSocial(proveedorDTO.getRazonSocial());
        Proveedor updatedProveedor=this.proveedorService.updateById(proveedor,(long) proveedor.getID());
        json.setMessage("Se ha actualizado la Proveedor");
        json.setData(updatedProveedor.toJson());
        return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
      }

      @DeleteMapping(produces = "application/json", consumes = "application/json")
      @Transactional
      public ResponseEntity<Object> eliminarProveedor(@Validated(ProveedorDTO.PutAndDelete.class) @RequestBody ProveedorDTO proveedorDTO) throws JsonProcessingException {
        Optional<Proveedor> existingProveedor = proveedorService.getById(proveedorDTO.getID());
        Response json = new Response();
        if(existingProveedor.isEmpty()){
            json.setMessage("La Proveedor no existe");
            json.setTitle("ERROR");
            return new ResponseEntity<Object>(json.toJson(), HttpStatus.NOT_FOUND); 
        }
        Proveedor proveedor = new Proveedor();
        proveedor.setID(proveedorDTO.getID());
        proveedor.setCuil(proveedorDTO.getCuil());
        proveedor.setDireccion(proveedorDTO.getDireccion());
        proveedor.setTelefono(proveedorDTO.getTelefono());
        proveedor.setEmail(proveedorDTO.getEmail());
        proveedor.setRazonSocial(proveedorDTO.getRazonSocial());
        proveedorService.eliminarLogico(proveedor.getID());
        json.setMessage("Se ha eliminado el Proveedor");
        json.setData(existingProveedor.get().toJson());
        return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
      }  




}