package com.veterinaria.vet.Services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veterinaria.vet.Models.Proveedor;
import com.veterinaria.vet.Repositories.ProveedorRepository;



@Service
public class ProveedorService {
  @Autowired
  ProveedorRepository proveedorRepository;

  public ArrayList<Proveedor> getTodasProveedores(){
    return (ArrayList<Proveedor>) proveedorRepository.findAll();
  }

  public ArrayList<Proveedor> getAllProveedores(){
     return (ArrayList<Proveedor>) proveedorRepository.findByDeletedAtIsNull();
   }


  public Optional<Proveedor> getById(Long id){
    return proveedorRepository.findByIDAndDeletedAtIsNull(id);
  }

  public Proveedor updateById(Proveedor pr, Long id){
    Proveedor Proveedor= proveedorRepository.findById(id).get();
    Proveedor.setCuil(pr.getCuil());
    Proveedor.setDireccion(pr.getDireccion());
    Proveedor.setEmail(pr.getEmail());
    Proveedor.setRazonSocial(pr.getRazonSocial());
    Proveedor.setTelefono(pr.getTelefono());
    Proveedor.setUpdatedAt(LocalDateTime.now());
    proveedorRepository.save(Proveedor);
    return Proveedor;
  }

  public Proveedor saveProveedor(Proveedor Proveedor){
     return proveedorRepository.save(Proveedor);
  }

   public Optional<Proveedor> findByEmail(String email){
     return proveedorRepository.findByEmail(email);
   }
   public Optional<Proveedor> findByCuil(String cuil){
     return proveedorRepository.findByCuil(cuil);
   }
   


    public void eliminarLogico(Long id){
      proveedorRepository.eliminarLogico(id);
    }
    public void saveLogico(Long id){
      proveedorRepository.saveLogico(id);
    }

}



