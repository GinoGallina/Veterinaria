package com.veterinaria.vet.Controllers;

import java.util.ArrayList;
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

import com.veterinaria.vet.Models.Producto;
import com.veterinaria.vet.Services.ProductosAdminService;


@RestController
@RequestMapping("/ProductosAdmin")
public class ProductosAdminController{
    @Autowired
    private ProductosAdminService productosAdminService;



    /*@GetMapping(path = "/{todos}")
    public ArrayList<Producto> getTodosProductos(){
      return this.productosAdminService.getTodosProductos();
    }*/

    @GetMapping
    public ArrayList<Producto> getProductos(){
      return this.productosAdminService.getAllProductos();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getProductoById(@RequestParam("id") Long id){
      Optional<Producto> existingProducto = productosAdminService.getById(id);
      if(!existingProducto.isPresent()){
        // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario con ID " + id + " no fue encontrado.");
        return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok(existingProducto);
    }

    @PostMapping()
    public ResponseEntity<?> save(@RequestBody Producto Producto){

      Optional<Producto> existingProducto = productosAdminService.findByDescripcion(Producto.getDescripcion());
      if(existingProducto.isPresent()){
        return ResponseEntity.badRequest().body("Ya existe un Producto con la misma descripcion");
      }
      Producto savedProducto = productosAdminService.saveProducto(Producto);
      return ResponseEntity.ok(savedProducto);
    }

    @PutMapping()
    public ResponseEntity<?>  updateProducto(@RequestBody Producto Producto){
      Optional<Producto> existingProducto = productosAdminService.findByDescripcion(Producto.getDescripcion());
      if(existingProducto.isPresent() && (existingProducto.get().getID()!=Producto.getID() )){
        return ResponseEntity.badRequest().body("Ya existe un Producto con la misma descripcion");
      }
      Producto updatedProducto=this.productosAdminService.updateById(Producto,(long) Producto.getID());
       return ResponseEntity.ok(updatedProducto);
    }

    @DeleteMapping()
    @Transactional
    public ResponseEntity<?> eliminarProducto(@RequestBody Producto Producto) throws Exception {
      Long id= (long) Producto.getID();
      Optional<Producto> producto = productosAdminService.getById(id);
      if(producto.isEmpty()){
        return ResponseEntity.notFound().build();
      }
      productosAdminService.eliminarLogico(id);
      return ResponseEntity.ok().build();
    }
    

}