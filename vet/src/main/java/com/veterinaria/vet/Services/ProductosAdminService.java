package com.veterinaria.vet.Services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veterinaria.vet.Models.Producto;
import com.veterinaria.vet.Repositories.ProductosRepository;

@Service
public class ProductosAdminService {
  @Autowired
  ProductosRepository productosAdminRepository;


  public ArrayList<Producto> getTodosProductos(){
    return (ArrayList<Producto>) productosAdminRepository.findAll();
  }

    public ArrayList<Producto> getAllProductos(){
    return (ArrayList<Producto>) productosAdminRepository.findByDeletedAtIsNull();
  }

 /*public Optional<Producto> getById(Long id){
    return productosAdminRepository.findById(id);
  }*/
  public Optional<Producto> getById(Long id){
    return productosAdminRepository.findByIDAndDeletedAtIsNull(id);
  }

  public Producto updateById(Producto prod, Long id){
    Producto producto= productosAdminRepository.findById(id).get();
    producto.setDescripcion(prod.getDescripcion());
    producto.setPrecio(prod.getPrecio());
    producto.setStock(prod.getStock());
    producto.setUpdatedAt(LocalDateTime.now());
    productosAdminRepository.save(producto);
    return producto;
  }

  public Producto saveProducto(Producto Producto){
     return productosAdminRepository.save(Producto);
  }

   public Optional<Producto> findByDescripcion(String desc){
     return productosAdminRepository.findByDescripcion(desc);
   }
   

    public void eliminarLogico(Long id){
      productosAdminRepository.eliminarLogico(id);
    }

    public void saveLogico(Long id){
      productosAdminRepository.saveLogico(id);
    }

    

}
