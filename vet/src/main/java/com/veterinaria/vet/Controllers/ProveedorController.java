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

import com.veterinaria.vet.Models.Proveedor;
import com.veterinaria.vet.Services.ProveedorService;


import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/Proveedores")
public class ProveedorController {
  
    @Autowired
    private ProveedorService proveedorService;



    @GetMapping
    public ArrayList<Proveedor> getProveedores(){
      return this.proveedorService.getAllProveedores();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getProveedorById(@RequestParam("id") Long id){
      Optional<Proveedor> existingProveedor = proveedorService.getById(id);
      if(!existingProveedor.isPresent()){
        // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario con ID " + id + " no fue encontrado.");
        return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok(existingProveedor);
    }

    @PostMapping()
    public ResponseEntity<?> save(@RequestBody Proveedor Proveedor){

      Optional<Proveedor> existingProveedorEmail = proveedorService.findByEmail(Proveedor.getEmail());
      Optional<Proveedor> existingProveedorCuil = proveedorService.findByCuil(Proveedor.getCuil());
      System.out.println(Proveedor.getCuil());
      if(existingProveedorEmail.isPresent() ||  existingProveedorCuil.isPresent()){
        return ResponseEntity.badRequest().body("Ya existe un Proveedor con el mismo mail o cuil");
      }
      Proveedor savedProveedor = proveedorService.saveProveedor(Proveedor);
      return ResponseEntity.ok(savedProveedor);
    }

    @PutMapping()
    public ResponseEntity<?>  updateProveedor(@RequestBody Proveedor Proveedor){
      Optional<Proveedor> existingProveedorEmail = proveedorService.findByEmail(Proveedor.getEmail());
      Optional<Proveedor> existingProveedorCuil = proveedorService.findByCuil(Proveedor.getCuil());
      if((existingProveedorEmail.isPresent() && existingProveedorEmail.get().getID()!=Proveedor.getID()) 
        || (existingProveedorCuil.isPresent() && existingProveedorCuil.get().getID()!=Proveedor.getID()) ){
        return ResponseEntity.badRequest().body("Ya existe un Proveedor con el mismo mail o cuil");
      }
      Proveedor updatedProveedor=this.proveedorService.updateById(Proveedor,(long) Proveedor.getID());
       return ResponseEntity.ok(updatedProveedor);
    }

    @DeleteMapping()
    @Transactional
    public ResponseEntity<?> eliminarProveedor(@RequestBody Proveedor Proveedor) throws Exception {
      Long id= (long) Proveedor.getID();
      Optional<Proveedor> proveedor = proveedorService.getById(id);
      if(proveedor.isEmpty()){
        return ResponseEntity.notFound().build();
      }
      proveedorService.eliminarLogico(id);
      return ResponseEntity.ok().build();
    }
    

}