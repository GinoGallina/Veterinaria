package com.veterinaria.vet.Controllers;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.veterinaria.vet.DTO.ReservaDTO;
import com.veterinaria.vet.Models.Cliente;
import com.veterinaria.vet.Models.Producto;
import com.veterinaria.vet.Models.Reserva;
import com.veterinaria.vet.Models.Response;
import com.veterinaria.vet.Services.ClienteService;
import com.veterinaria.vet.Services.ProductosAdminService;
import com.veterinaria.vet.Services.ReservaService;

import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/Reservas")
public class ReservaController {
        @Autowired
        private ReservaService reservaService;
        @Autowired
        private ProductosAdminService productosAdminService;
        @Autowired
        private ClienteService clienteService;


        @GetMapping(path = "/Index")
        public ModelAndView getReservas(){
            ArrayList<Reserva> reservas =  this.reservaService.getAllReservas();
            ArrayList<Cliente> clientes =  this.clienteService.getAllClientes();
            ArrayList<Producto> productos =  this.productosAdminService.getAllProductos();
            ArrayList<String> header = new ArrayList<>();
            header.add("Cliente");
            header.add("Producto");
            header.add("Precio pactado");
            header.add("Cantidad");
            header.add("Subtotal");
            header.add("Total");
            ModelAndView modelAndView = new ModelAndView("Reservas/Index");
            modelAndView.addObject("clientes", clientes);
            modelAndView.addObject("reservas", reservas);
            modelAndView.addObject("productos", productos);
            modelAndView.addObject("header", header);
            return modelAndView;
        }



       @PostMapping(produces = "application/json", consumes = "application/json")
        public ResponseEntity<Object> save(@Validated(ReservaDTO.PutAndPost.class) @RequestBody ReservaDTO reservaDTO) throws JsonProcessingException {
          Response json = new Response();
            /*Optional<Reserva> existingReserva = reservaService.findByDescripcion(ReservaDTO.getDescripcion());
            if (existingReserva.isPresent()) {
                if (!reservaService.getById(existingReserva.get().getID()).isPresent()) {
                    reservaService.saveLogico(existingReserva.get().getID());
                    json.setMessage("La Reserva se encontraba eliminada y se ha recuperado");
                    json.setData(existingReserva.get().toJson());
                    return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
                } else {
                    json.setMessage("La Reserva ingresada ya existe");
                    json.setTitle("ERROR");
                    return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST);
                }
            } */

            //VER QUE SE VALIDA ANTES

            Reserva Reserva = new Reserva();
            Optional<Cliente> existingCliente= clienteService.getById(reservaDTO.getClienteID());
            if (!existingCliente.isPresent()) {
                json.setMessage("El cliente no existe");
                json.setTitle("ERROR");
                return new ResponseEntity<Object>(json.toJson(), HttpStatus.NOT_FOUND); 
            }
            Reserva.setCliente(existingCliente.get());
            Reserva savedReserva = reservaService.saveReserva(Reserva);
            json.setMessage("Se ha guardado la Reserva");
            json.setData(savedReserva.toJson());
            return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
        }

        /*@PutMapping(produces = "application/json", consumes = "application/json")
        public ResponseEntity<Object> updateReserva(@Validated({ReservaDTO.PutAndDelete.class,ReservaDTO.PutAndPost.class}) @RequestBody ReservaDTO ReservaDTO) throws JsonProcessingException{
            Optional<Reserva> existingReserva = reservaService.findByDescripcion(ReservaDTO.getDescripcion());
            Response json = new Response();
            if(existingReserva.isPresent()){
                json.setMessage("La Reserva ingresada ya existe");
                json.setTitle("ERROR");
                return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST); 
            }
            Reserva Reserva = new Reserva();
            Reserva.setID(ReservaDTO.getID());
            Reserva.setDescripcion(ReservaDTO.getDescripcion());
            Reserva updatedReserva=this.reservaService.updateById(Reserva,(long) Reserva.getID());
            json.setMessage("Se ha actualizado la Reserva");
            json.setData(updatedReserva.toJson());
            return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
        }

        @DeleteMapping(produces = "application/json", consumes = "application/json")
        @Transactional
        public ResponseEntity<Object> eliminarReserva(@Validated(ReservaDTO.PutAndDelete.class) @RequestBody ReservaDTO ReservaDTO) throws JsonProcessingException {
            Optional<Reserva> existingReserva = reservaService.getById(ReservaDTO.getID());
            Response json = new Response();
            if(existingReserva.isEmpty()){
                json.setMessage("La Reserva no existe");
                json.setTitle("ERROR");
                return new ResponseEntity<Object>(json.toJson(), HttpStatus.NOT_FOUND); 
            }
            reservaService.eliminarLogico(ReservaDTO.getID());
            json.setMessage("Se ha eliminado la Reserva");
            json.setData(existingReserva.get().toJson());
            return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
        } */ 
}
