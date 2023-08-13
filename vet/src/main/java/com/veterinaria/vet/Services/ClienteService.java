package com.veterinaria.vet.Services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veterinaria.vet.Models.Cliente;
import com.veterinaria.vet.Repositories.ClienteReposioty;

@Service
public class ClienteService {
  @Autowired
  ClienteReposioty clienteReposioty;

  public ArrayList<Cliente> getTodosClientes(){
    return (ArrayList<Cliente>) clienteReposioty.findAll();
  }

    public ArrayList<Cliente> getAllClientes(){
    return (ArrayList<Cliente>) clienteReposioty.findByDeletedAtIsNull();
  }

 /*public Optional<Veterinario> getById(Long id){
    return clienteReposioty.findById(id);
  }*/
  public Optional<Cliente> getById(Long id){
    return clienteReposioty.findByIDAndDeletedAtIsNull(id);
  }

  public Cliente updateById(Cliente cli, Long id){
    Cliente cliente= clienteReposioty.findById(id).get();
    cliente.setNombre(cli.getNombre());
    cliente.setApellido(cli.getApellido());
    cliente.setDireccion(cli.getDireccion());
    cliente.setDni(cli.getDni());
    cliente.setTelefono(cli.getTelefono());
    cliente.setUser(cli.getUser());
    cliente.setUpdatedAt(LocalDateTime.now());
    clienteReposioty.save(cliente);
    return cliente;
  }

  public Cliente saveCliente(Cliente cli){
     return clienteReposioty.save(cli);
  }

   public Optional<Cliente> findByDni(String dni){
     return clienteReposioty.findByDni(dni);
   }
   

  /*public Boolean deleteVeterinario(Long id){
    try{
      clienteReposioty.deleteById(id);
      return true; 

    }catch(Exception e){
      return false;
    }*/
    public void eliminarLogico(Long id){
      clienteReposioty.eliminarLogico(id);
    }
    
    public void saveLogico(Long id) {
      clienteReposioty.saveLogico(id);
    }
  }