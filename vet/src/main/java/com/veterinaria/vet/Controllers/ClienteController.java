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

import com.veterinaria.vet.Models.Cliente;
import com.veterinaria.vet.Services.ClienteService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/Clientes")
public class ClienteController {
  
    @Autowired
    private ClienteService clienteService;



    /*@GetMapping(path = "/{todos}")
    public ArrayList<Cliente> getTodosClientes(){
      return this.ClienteService.getTodosClientes();
    }*/

    @GetMapping
    public ArrayList<Cliente> getClientes(){
      return this.clienteService.getAllClientes();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getClienteById(@RequestParam("id") Long id){
      Optional<Cliente> existingCliente = clienteService.getById(id);
      if(!existingCliente.isPresent()){
        // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario con ID " + id + " no fue encontrado.");
        return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok(existingCliente);
    }

    @PostMapping()
    public ResponseEntity<?> save(@RequestBody Cliente Cliente){

      Optional<Cliente> existingCliente = clienteService.findByDni(Cliente.getDni());
      if(existingCliente.isPresent()){
        if (!clienteService.getById(existingCliente.get().getID()).isPresent()) {
          clienteService.saveLogico(existingCliente.get().getID());
          return ResponseEntity.ok(existingCliente.get());
        } else {
        return ResponseEntity.badRequest().body("Ya existe un Cliente con el mismo dni");
      }}
      Cliente savedCliente = clienteService.saveCliente(Cliente);
      return ResponseEntity.ok(savedCliente);
    }

    @PutMapping()
    public ResponseEntity<?>  updateCliente(@RequestBody Cliente Cliente){
      Optional<Cliente> existingCliente = clienteService.findByDni(Cliente.getDni());
      if(existingCliente.isPresent() && (existingCliente.get().getID()!=Cliente.getID() )){
        return ResponseEntity.badRequest().body("Ya existe un Cliente con la misma matrícula");
      }
      Cliente updatedCliente=this.clienteService.updateById(Cliente,(long) Cliente.getID());

       return ResponseEntity.ok(updatedCliente);
    }

    @DeleteMapping()
    @Transactional
    public ResponseEntity<?> eliminarCliente(@RequestBody Cliente Cliente) throws Exception {
      Long id= (long) Cliente.getID();
      Optional<Cliente> cliente = clienteService.getById(id);
      if(cliente.isEmpty()){
        return ResponseEntity.notFound().build();
      }
      clienteService.eliminarLogico(id);
      return ResponseEntity.ok().build();
    }
    

}
