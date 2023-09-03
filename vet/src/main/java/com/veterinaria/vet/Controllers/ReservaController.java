package com.veterinaria.vet.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.veterinaria.vet.Models.Cliente;
import com.veterinaria.vet.Models.Producto;
import com.veterinaria.vet.Models.ReservaProducto;
import com.veterinaria.vet.Models.ReservaProductoId;
import com.veterinaria.vet.Models.Reserva;
import com.veterinaria.vet.Models.Response;
import com.veterinaria.vet.Services.ClienteService;
import com.veterinaria.vet.Services.ProductosAdminService;
import com.veterinaria.vet.Services.ReservaService;
import com.veterinaria.vet.Services.ReservaProductoService;
import com.veterinaria.vet.annotations.CheckAdmin;
import com.veterinaria.vet.annotations.CheckAdminUser;

import com.veterinaria.vet.DTO.ReservaDTO;

import jakarta.servlet.http.HttpSession;
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
        @Autowired
        private ReservaProductoService reservaProductoService;

        @CheckAdminUser
        @GetMapping(path = "/Index")
        public ModelAndView getReservas(HttpSession session) {
            String user_role = session.getAttribute("user_role").toString();
            ArrayList<Reserva> reservas = new ArrayList<Reserva>();
            if (user_role.equals("[ADMIN]")) {
                reservas = this.reservaService.getAllReservas();
            } else {
                Long user_id = (Long)session.getAttribute("user_id");
                Cliente cliente = this.clienteService.getByUserId(user_id).get();
                reservas = this.reservaService.getReservasCliente(cliente.getID());
            }
            ArrayList<Cliente> clientes =  this.clienteService.getAllClientes();
            ArrayList<Producto> productos =  this.productosAdminService.getAllProductos();
            ModelAndView modelAndView = new ModelAndView("Reservas/Index");
            modelAndView.addObject("clientes", clientes);
            modelAndView.addObject("reservas", reservas);
            modelAndView.addObject("productos", productos);
            modelAndView.addObject("user_role", user_role);
            modelAndView.addObject("user_email", session.getAttribute("user_email"));
            return modelAndView;
        }

        @CheckAdminUser
        @GetMapping(path = "/New")
        public ModelAndView New(){
            ModelAndView modelAndView = new ModelAndView("Reservas/New");
            ArrayList<Producto> productos =  this.productosAdminService.getAllProductos();                        
            modelAndView.addObject("productos", productos);
            return modelAndView;
        }

        @CheckAdminUser
        @PostMapping(path = "/New", produces = "application/json", consumes = "application/json")
        public ResponseEntity<Object> save(@RequestBody List<ReservaProducto> prodQuantity, HttpSession session) throws JsonProcessingException {
            try {
                Long user_id = (Long) session.getAttribute("user_id");
                Response json = new Response();
                Reserva reserva = new Reserva();
                
                List<ReservaProducto> productos = new ArrayList<ReservaProducto>();
                reserva.setCliente(clienteService.getByUserId(user_id).get());
                Reserva savedReserva = reservaService.saveReserva(reserva);

                for (ReservaProducto product : prodQuantity) {
                    product.setId(new ReservaProductoId(savedReserva.getID(), product.getProducto().getID()));
                    product.setReserva(savedReserva);
                    product.setPrecio(productosAdminService.getById(product.getProducto().getID()).get().getPrecio());
                    productos.add(product);
                    Producto producto = productosAdminService.getById(product.getProducto().getID()).get();
                    producto.setStock(producto.getStock() - product.getCantidad());
                }

                reservaProductoService.saveProductos(productos);


                json.setMessage("Se ha guardado la reserva");
                json.setData(savedReserva.toJson());
                return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
            } catch (Exception e) {
                Response json = new Response();
                json.setMessage("No se ha podido guardar la reserva");
                json.setTitle("ERROR");
                json.setData(e.getMessage());
                return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST);
            }
        }

        @CheckAdmin
        @DeleteMapping(produces = "application/json", consumes = "application/json")
        @Transactional
        public ResponseEntity<Object> eliminarReserva(@Validated(ReservaDTO.PutAndDelete.class) @RequestBody ReservaDTO ReservaDTO) throws JsonProcessingException {
            try {
                Optional<Reserva> existingReserva = reservaService.getById(ReservaDTO.getID());
                Response json = new Response();
                if (existingReserva.isEmpty()){
                    json.setMessage("La reserva no existe");
                    json.setTitle("ERROR");
                    return new ResponseEntity<Object>(json.toJson(), HttpStatus.NOT_FOUND); 
                }
                reservaService.eliminarLogico(ReservaDTO.getID());
                json.setMessage("Se ha eliminado la reserva");
                json.setData(existingReserva.get().toJson());
                return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
            } catch (Exception e) {
                Response json = new Response();
                json.setMessage("No se ha podido guardar la reserva");
                json.setTitle("ERROR");
                json.setData(e.getMessage());
                return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST);
            }
        }
}
