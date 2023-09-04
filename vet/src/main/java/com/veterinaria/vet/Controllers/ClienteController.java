package com.veterinaria.vet.Controllers;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.veterinaria.vet.DTO.ClienteDTO;
import com.veterinaria.vet.Models.Atencion;
import com.veterinaria.vet.Models.Cliente;
import com.veterinaria.vet.Models.Mascota;
import com.veterinaria.vet.Models.Practica;
import com.veterinaria.vet.Models.Raza;
import com.veterinaria.vet.Models.Response;
import com.veterinaria.vet.Security.DTO.NewUser;
import com.veterinaria.vet.Security.Models.User;
import com.veterinaria.vet.Security.Services.UserService;
import com.veterinaria.vet.Services.AtencionService;
import com.veterinaria.vet.Services.ClienteService;
import com.veterinaria.vet.Services.MascotaService;
import com.veterinaria.vet.Services.PracticaService;
import com.veterinaria.vet.Services.RazaService;
import com.veterinaria.vet.annotations.CheckAdmin;
import com.veterinaria.vet.annotations.CheckAdminVet;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/Clientes")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;
	@Autowired
	private UserService userService;
	@Autowired
	private MascotaService mascotaService;
	@Autowired
	private AtencionService atencionService;
	@Autowired
	private RazaService razaService;
	@Autowired
	private PracticaService practicaService;

	@CheckAdminVet
	@GetMapping(path = "/Index")
	public ModelAndView getClientes(HttpSession session) {
		ArrayList<Cliente> clientes = clienteService.getAllClientes();
		ModelAndView modelAndView = new ModelAndView("Clientes/Index");
		modelAndView.addObject("clientes", clientes);
		modelAndView.addObject("user_role", session.getAttribute("user_role"));
		modelAndView.addObject("user_email", session.getAttribute("user_email"));
		return modelAndView;
	}

	@CheckAdminVet
	@GetMapping(path = "/Details")
	public String getClienteSeleccionado(@RequestParam Long id, Model model, HttpSession session) {
		Optional<Cliente> cliente = clienteService.getById(id);
		// Response json = new Response();
		if (!cliente.isPresent()) {
			// return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
			// VER Q DEVUELVO
		}
		Mascota mascota = new Mascota();
		ArrayList<Mascota> mascotas = mascota.orderByCreatedAt(mascotaService.getAllMascotasCliente(id));
		ArrayList<Atencion> atenciones = atencionService.getAllAtencionesCliente(id);
		ArrayList<Raza> razas = razaService.getAllRazas();
		ArrayList<Practica> practicas = practicaService.getAllPracticas();
		model.addAttribute("mascotas", mascotas);
		model.addAttribute("atenciones", atenciones);
		model.addAttribute("razas", razas);
		model.addAttribute("practicas", practicas);
		model.addAttribute("cliente", cliente.get());
		model.addAttribute("user_role", session.getAttribute("user_role"));
		model.addAttribute("user_email", session.getAttribute("user_email"));
		return "Clientes/Details";
	}

	@CheckAdmin
	@PostMapping(produces = "application/json", consumes = "application/json")
	public ResponseEntity<Object> save(@Validated(ClienteDTO.PutAndPost.class) @RequestBody ClienteDTO clienteDTO)
			throws JsonProcessingException {
		Optional<Cliente> existingCliente = clienteService.findByDni(clienteDTO.getDni());
		Response json = new Response();
		if (existingCliente.isPresent()) {
			if (!clienteService.getById(existingCliente.get().getID()).isPresent()) {
				clienteService.saveLogico(existingCliente.get().getID());
				json.setMessage("El cliente se encontraba eliminado y se ha recuperado");
				json.setData(existingCliente.get().toJson());

				// REESTABLECER USUARIO
				userService.saveLogico(existingCliente.get().getUser().getID());

				return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
			} else {
				json.setMessage("El cliente ingresado ya existe");
				json.setTitle("ERROR");
				return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST);
			}
		}
		NewUser newUser = new NewUser();
		newUser.setEmail(clienteDTO.getEmail());
		newUser.setPassword(clienteDTO.getPassword());
		newUser.setRol("USER");
		User user = new User();
		ResponseEntity<?> response = userService.save(newUser);
		if (response.getStatusCode() == HttpStatus.OK) {
			user = (User) response.getBody();
		} else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
			json.setMessage((String) response.getBody());
			json.setTitle("ERROR");
			return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST);
		}
		Cliente Cliente = new Cliente();
		Cliente.setUser(user);
		Cliente.setDni(clienteDTO.getDni());
		Cliente.setNombre(clienteDTO.getNombre());
		Cliente.setApellido(clienteDTO.getApellido());
		Cliente.setTelefono(clienteDTO.getTelefono());
		Cliente.setDireccion(clienteDTO.getDireccion());
		Cliente savedCliente = clienteService.saveCliente(Cliente);
		json.setMessage("Se ha guardado el cliente");
		json.setData(savedCliente.toJson());
		return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
	}

	@CheckAdmin
	@PutMapping(produces = "application/json", consumes = "application/json")
	public ResponseEntity<Object> updateCliente(@Validated({ ClienteDTO.PutAndDelete.class,
			ClienteDTO.PutAndPost.class }) @RequestBody ClienteDTO clienteDTO) throws JsonProcessingException {
		Optional<Cliente> existingCliente = clienteService.findByDni(clienteDTO.getDni());
		Response json = new Response();
		if (existingCliente.isPresent()) {
			json.setMessage("El cliente ingresado ya existe");
			json.setTitle("ERROR");
			return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST);
		}
		Optional<Cliente> cliente = clienteService.getById(clienteDTO.getID());
		if (cliente.isEmpty()) {
			json.setMessage("El cliente no existe");
			json.setTitle("ERROR");
			return new ResponseEntity<Object>(json.toJson(), HttpStatus.NOT_FOUND);
		}
		cliente.get().setID(clienteDTO.getID());
		cliente.get().setDni(clienteDTO.getDni());
		cliente.get().setNombre(clienteDTO.getNombre());
		cliente.get().setApellido(clienteDTO.getApellido());
		cliente.get().setTelefono(clienteDTO.getTelefono());
		cliente.get().setDireccion(clienteDTO.getDireccion());
		Cliente updatedCliente = this.clienteService.updateById(cliente.get(), (long) cliente.get().getID());
		json.setMessage("Se ha actualizado el cliente");
		json.setData(updatedCliente.toJson());
		return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
	}

	@CheckAdmin
	@DeleteMapping(produces = "application/json", consumes = "application/json")
	@Transactional
	public ResponseEntity<Object> eliminarCliente(
			@Validated(ClienteDTO.PutAndDelete.class) @RequestBody ClienteDTO clienteDTO)
			throws JsonProcessingException {
		Optional<Cliente> existingCliente = clienteService.getById(clienteDTO.getID());
		Response json = new Response();
		if (existingCliente.isEmpty()) {
			json.setMessage("El cliente no existe");
			json.setTitle("ERROR");
			return new ResponseEntity<Object>(json.toJson(), HttpStatus.NOT_FOUND);
		}
		clienteService.eliminarLogico(clienteDTO.getID());
		json.setMessage("Se ha eliminado el cliente");
		json.setData(existingCliente.get().toJson());

		// BORRAR USUARIO
		userService.eliminarLogico(existingCliente.get().getUser().getID());

		return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
	}
}
