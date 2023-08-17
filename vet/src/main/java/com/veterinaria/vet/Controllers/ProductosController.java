package com.veterinaria.vet.Controllers;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.veterinaria.vet.DTO.ProductoDTO;
import com.veterinaria.vet.Models.Producto;
import com.veterinaria.vet.Models.Response;
import com.veterinaria.vet.Services.ProductosAdminService;


@RestController
@RequestMapping("/Productos")
public class ProductosController{
      @Autowired
      private ProductosAdminService productosAdminService;



      @GetMapping(path = "/Index")
      public ModelAndView getProductos(){
          ArrayList<Producto> productos =  this.productosAdminService.getAllProductos();
          ArrayList<String> header = new ArrayList<>();
          header.add("Descripcion");
          header.add("Precio");
          header.add("Imagen");
          header.add("Stock");
          ModelAndView modelAndView = new ModelAndView("Productos/Index");
          modelAndView.addObject("productos", productos);
          modelAndView.addObject("header", header);
          return modelAndView;
      }



      @PostMapping(produces = "application/json", consumes = "application/json")
      public ResponseEntity<Object> save(@Validated(ProductoDTO.PutAndPost.class) @RequestBody ProductoDTO productoDTO) throws JsonProcessingException {
          Optional<Producto> existingProducto = productosAdminService.findByDescripcion(productoDTO.getDescripcion());
          Response json = new Response();
          if (existingProducto.isPresent()) {
              if (!productosAdminService.getById(existingProducto.get().getID()).isPresent()) {
                  productosAdminService.saveLogico(existingProducto.get().getID());
                  json.setMessage("El Producto se encontraba eliminado y se ha recuperado");
                  json.setData(existingProducto.get().toJson());
                  return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
              } else {
                  json.setMessage("La Producto ingresada ya existe");
                  json.setTitle("ERROR");
                  return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST);
              }
          }
          Producto Producto = new Producto();
          Producto.setDescripcion(productoDTO.getDescripcion());
          Producto.setPrecio(productoDTO.getPrecio());
          Producto.setImg(productoDTO.getImg());
          Producto.setStock(productoDTO.getStock());
          Producto savedProducto = productosAdminService.saveProducto(Producto);
          json.setMessage("Se ha guardado el Producto");
          json.setData(savedProducto.toJson());
          return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
      }



      @PutMapping(produces = "application/json", consumes = "application/json")
      public ResponseEntity<Object> updateProducto(@Validated({ProductoDTO.PutAndDelete.class,ProductoDTO.PutAndPost.class}) @RequestBody ProductoDTO productoDTO) throws JsonProcessingException{
          Optional<Producto> existingProducto = productosAdminService.findByDescripcion(productoDTO.getDescripcion());
          Response json = new Response();
          if(existingProducto.isPresent()){
              json.setMessage("El Producto ingresado ya existe");
              json.setTitle("ERROR");
              return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST); 
          }
          Producto Producto = new Producto();
          Producto.setID(productoDTO.getID());
          Producto.setDescripcion(productoDTO.getDescripcion());
          Producto.setPrecio(productoDTO.getPrecio());
          Producto.setImg(productoDTO.getImg());
          Producto.setStock(productoDTO.getStock());
          Producto updatedProducto=this.productosAdminService.updateById(Producto,(long) Producto.getID());
          json.setMessage("Se ha actualizado la Producto");
          json.setData(updatedProducto.toJson());
          return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
      }

      @DeleteMapping(produces = "application/json", consumes = "application/json")
      @Transactional
      public ResponseEntity<Object> eliminarProducto(@Validated(ProductoDTO.PutAndDelete.class) @RequestBody ProductoDTO productoDTO) throws JsonProcessingException {
          Optional<Producto> existingProducto = productosAdminService.findByDescripcion(productoDTO.getDescripcion());
          Response json = new Response();
          if(existingProducto.isEmpty()){
              json.setMessage("El Producto no existe");
              json.setTitle("ERROR");
              return new ResponseEntity<Object>(json.toJson(), HttpStatus.NOT_FOUND); 
          }
          productosAdminService.eliminarLogico(productoDTO.getID());
          json.setMessage("Se ha eliminado la Producto");
          json.setData(existingProducto.get().toJson());
          return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
      }  


    

}