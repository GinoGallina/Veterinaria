package com.veterinaria.vet.Controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.veterinaria.vet.DTO.DeudaDTO;
import com.veterinaria.vet.DTO.ProveedorDTO;
import com.veterinaria.vet.Models.Deuda;
import com.veterinaria.vet.Models.PagosDeuda;
import com.veterinaria.vet.Models.Proveedor;
import com.veterinaria.vet.Models.Response;
import com.veterinaria.vet.Services.DeudaService;
import com.veterinaria.vet.Services.PagosDeudaService;
import com.veterinaria.vet.Services.ProveedorService;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/Proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;
    @Autowired
    private PagosDeudaService pagosDeudaService;
    @Autowired
    private DeudaService deudaService;

    @GetMapping(path = "/Index")
    public ModelAndView getProveedores(HttpSession session) {
        ArrayList<Proveedor> proveedores = this.proveedorService.getAllProveedores();
        ModelAndView modelAndView = new ModelAndView("Proveedores/Index");
        modelAndView.addObject("proveedores", proveedores);
        modelAndView.addObject("user_role", session.getAttribute("user_role"));
        return modelAndView;
    }

    @GetMapping(path = "/Details/{proveedorID}")
    public ModelAndView details(@PathVariable Long proveedorID, HttpSession session) {
        Deuda deuda = new Deuda();
        ArrayList<Deuda> deudas = deuda.orderByCreatedAt(deudaService.getDeudasByProveedorId(proveedorID));
        Proveedor proveedor = proveedorService.getById(proveedorID).get();
        ModelAndView modelAndView = new ModelAndView("Proveedores/Details");
        modelAndView.addObject("deudas", deudas);
        modelAndView.addObject("proveedor", proveedor);
        modelAndView.addObject("user_role", session.getAttribute("user_role"));
        return modelAndView;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<Object> save(@Validated(ProveedorDTO.PutAndPost.class) @RequestBody ProveedorDTO proveedorDTO)
            throws JsonProcessingException {
        Optional<Proveedor> existingProveedorEmail = proveedorService.findByEmail(proveedorDTO.getEmail());
        Optional<Proveedor> existingProveedorCuil = proveedorService.findByCuil(proveedorDTO.getCuil());
        Optional<Proveedor> existingProveedor = existingProveedorEmail.or(() -> existingProveedorCuil);
        Response json = new Response();
        if (existingProveedor.isPresent()) {
            if (!proveedorService.getById(existingProveedor.get().getID()).isPresent()) {
                proveedorService.saveLogico(existingProveedor.get().getID());
                json.setMessage("El proveedor se encontraba eliminado y se ha recuperado");
                json.setData(existingProveedor.get().toJson());
                return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
            } else {
                json.setMessage("El proveedor ingresado ya existe");
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
        json.setMessage("Se ha guardado el proveedor");
        json.setData(savedProveedor.toJson());
        return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
    }

    @PostMapping(path = "/Details/New",produces = "application/json", consumes = "application/json")
    public ResponseEntity<Object> newDeuda(@Validated(DeudaDTO.PutAndPost.class) @RequestBody DeudaDTO deudaDTO) throws JsonProcessingException {
        //Optional<Proveedor> existingProveedorEmail = proveedorService.findByEmail(proveedorDTO.getEmail());
        System.out.println("aAAA");
        Response json = new Response();
        /*if (existingProveedor.isPresent()) {
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
        }*/
        Optional<Proveedor> existingProveedor = proveedorService.getById(deudaDTO.getProveedorID());
        if(!existingProveedor.isPresent()){
            json.setMessage("El Proveedor no existe");
            json.setTitle("ERROR");
            return new ResponseEntity<Object>(json.toJson(), HttpStatus.NOT_FOUND);
        }
        
        
        
        Deuda deuda = new Deuda();
        deuda.setPrecio(deudaDTO.getPrecio());
        deuda.setDescripcion(deudaDTO.getDescripcion());
        deuda.setProveedor(existingProveedor.get());
        Deuda savedDeuda = deudaService.saveDeuda(deuda);


       /*  PagosDeuda pagosDeuda = new PagosDeuda();
        pagosDeuda.setDeuda(deuda);
        pagosDeuda.setPago(BigDecimal.ZERO);
        PagosDeuda savedPagosDeuda = pagosDeudaService.savePagosDeuda(pagosDeuda);

        deuda.addPagosDeuda(savedPagosDeuda);*/

        json.setMessage("Se ha guardado la deuda");
        json.setData(savedDeuda.toJson());
        return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<Object> updateProveedor(
            @Validated({ ProveedorDTO.PutAndDelete.class,
                    ProveedorDTO.PutAndPost.class }) @RequestBody ProveedorDTO proveedorDTO)
            throws JsonProcessingException {
        Optional<Proveedor> existingProveedorEmail = proveedorService.findByEmail(proveedorDTO.getEmail());
        Optional<Proveedor> existingProveedorCuil = proveedorService.findByCuil(proveedorDTO.getCuil());
        Optional<Proveedor> existingProveedor = existingProveedorEmail.or(() -> existingProveedorCuil);
        Response json = new Response();
        if (existingProveedor.isPresent()) {
            json.setMessage("Ya existe un proveedor con ese mail o cuil");
            json.setTitle("ERROR");
            return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST);
        }
        Optional<Proveedor> proveedor = proveedorService.getById(proveedorDTO.getID());
        if (proveedor.isEmpty()) {
            json.setMessage("El proveedor no existe");
            json.setTitle("ERROR");
            return new ResponseEntity<Object>(json.toJson(), HttpStatus.NOT_FOUND);
        }
        proveedor.get().setCuil(proveedorDTO.getCuil());
        proveedor.get().setDireccion(proveedorDTO.getDireccion());
        proveedor.get().setTelefono(proveedorDTO.getTelefono());
        proveedor.get().setEmail(proveedorDTO.getEmail());
        proveedor.get().setRazonSocial(proveedorDTO.getRazonSocial());
        Proveedor updatedProveedor = this.proveedorService.updateById(proveedor.get(), (long) proveedor.get().getID());
        json.setMessage("Se ha actualizado el proveedor");
        json.setData(updatedProveedor.toJson());
        return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
    }

    @PostMapping(path = "/Details/PagarDeuda", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Object> pagarDeuda( @Validated( DeudaDTO.Put.class ) @RequestBody DeudaDTO deudaDTO) throws JsonProcessingException {
        Response json = new Response();
        /*Optional<Proveedor> existingProveedorEmail = proveedorService.findByEmail(proveedorDTO.getEmail());
        if (existingProveedor.isPresent()) {
            json.setMessage("Ya existe un proveedor con ese mail o cuil");
            json.setTitle("ERROR");
            return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST);
        }*/
        Optional<Deuda> deuda =deudaService.getById(deudaDTO.getID());
        if (deuda.isEmpty()) {
            json.setMessage("La deuda no existe");
            json.setTitle("ERROR");
            return new ResponseEntity<Object>(json.toJson(), HttpStatus.NOT_FOUND);
        }

        BigDecimal total_pagado = deuda.get().getTotalPagos();
        BigDecimal total = deuda.get().getPrecio();
        if(total.compareTo(total_pagado.add(deudaDTO.getPago())) < 0 ){
            json.setMessage("Esta pagando mas de la cuenta");
            json.setTitle("ERROR");
            return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST);
        }
        PagosDeuda pagosDeuda = new PagosDeuda();
        pagosDeuda.setDeuda(deuda.get());
        pagosDeuda.setPago(deudaDTO.getPago());

        PagosDeuda savedPagosDeuda = pagosDeudaService.savePagosDeuda(pagosDeuda);

        json.setMessage("Se ha realizado el pago con exito");
        deuda.get().addPagosDeuda(savedPagosDeuda);
        json.setData(deuda.get().toJson());
        return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
    }

    @DeleteMapping(produces = "application/json", consumes = "application/json")
    @Transactional
    public ResponseEntity<Object> eliminarProveedor(
            @Validated(ProveedorDTO.PutAndDelete.class) @RequestBody ProveedorDTO proveedorDTO)
            throws JsonProcessingException {
        Optional<Proveedor> existingProveedor = proveedorService.getById(proveedorDTO.getID());
        Response json = new Response();
        if (existingProveedor.isEmpty()) {
            json.setMessage("El proveedor no existe");
            json.setTitle("ERROR");
            return new ResponseEntity<Object>(json.toJson(), HttpStatus.NOT_FOUND);
        }
        proveedorService.eliminarLogico(proveedorDTO.getID());
        json.setMessage("Se ha eliminado el proveedor");
        json.setData(existingProveedor.get().toJson());
        return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/Details", produces = "application/json", consumes = "application/json")
    @Transactional
    public ResponseEntity<Object> eliminarDeuda(
            @Validated(DeudaDTO.PutAndDelete.class) @RequestBody DeudaDTO deudaDTO)
            throws JsonProcessingException {
        Optional<Deuda> existingDeuda = deudaService.getById(deudaDTO.getID());
        Response json = new Response();
        if (existingDeuda.isEmpty()) {
            json.setMessage("La deuda no existe");
            json.setTitle("ERROR");
            return new ResponseEntity<Object>(json.toJson(), HttpStatus.NOT_FOUND);
        }
        deudaService.eliminarLogico(deudaDTO.getID());
        json.setMessage("Se ha eliminado la deuda");
        json.setData(existingDeuda.get().toJson());
        return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
    }

}