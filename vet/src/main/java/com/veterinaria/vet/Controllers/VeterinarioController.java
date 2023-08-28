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
import com.veterinaria.vet.DTO.VeterinarioDTO;
import com.veterinaria.vet.Models.Veterinario;
import com.veterinaria.vet.Security.DTO.NewUser;
import com.veterinaria.vet.Security.Models.User;
import com.veterinaria.vet.Security.Services.UserService;
import com.veterinaria.vet.Services.VeterinarioService;
import com.veterinaria.vet.Models.Response;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/Veterinarios")
public class VeterinarioController{
  
	@Autowired
	private VeterinarioService veterinarioService;
	@Autowired
	private UserService userService;



	@GetMapping(path = "/Index")
	public ModelAndView getVeterinarios(HttpSession session){
		ArrayList<Veterinario> veterinarios =  this.veterinarioService.getAllVeterinarios();
		ArrayList<String> header = new ArrayList<>();
		ModelAndView modelAndView = new ModelAndView("Veterinarios/Index");
		modelAndView.addObject("veterinarios", veterinarios);
		modelAndView.addObject("user_role", session.getAttribute("user_role"));
		return modelAndView;
	}

	@PostMapping(produces = "application/json", consumes = "application/json")
	public ResponseEntity<Object> save(@Validated({VeterinarioDTO.PutAndPost.class,VeterinarioDTO.Post.class}) @RequestBody VeterinarioDTO veterinarioDTO) throws JsonProcessingException {
		Optional<Veterinario> existingVeterinario = veterinarioService.findByMatricula(veterinarioDTO.getMatricula());
		Response json = new Response();
		if (existingVeterinario.isPresent()) {
			if (!veterinarioService.getById(existingVeterinario.get().getID()).isPresent()) {
				veterinarioService.saveLogico(existingVeterinario.get().getID());
				json.setMessage("El Veterinario se encontraba eliminado y se ha recuperado con sus datos antiguos");
				json.setData(existingVeterinario.get().toJson());

				//REESTABLECER USUARIO
				
				return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
			} else {
				json.setMessage("El Veterinario ingresado ya existe");
				json.setTitle("ERROR");
				return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST);
			}
		}
		NewUser newUser = new NewUser();
		newUser.setEmail(veterinarioDTO.getEmail());
		newUser.setPassword(veterinarioDTO.getPassword());
		User user = new User();
		ResponseEntity<?> response = userService.save(newUser);
		if (response.getStatusCode() == HttpStatus.OK) {
			user = (User) response.getBody();
		} else if(response.getStatusCode() == HttpStatus.BAD_REQUEST) {
			json.setMessage((String) response.getBody());
			json.setTitle("ERROR");
			return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST);
		}
		Veterinario veterinario = new Veterinario();
		veterinario.setUser(user);
		veterinario.setMatricula(veterinarioDTO.getMatricula());
		veterinario.setNombre(veterinarioDTO.getNombre());
		veterinario.setApellido(veterinarioDTO.getApellido());
		veterinario.setTelefono(veterinarioDTO.getTelefono());
		veterinario.setDireccion(veterinarioDTO.getDireccion());
		Veterinario savedVeterinario = veterinarioService.saveVeterinario(veterinario);
		json.setMessage("Se ha guardado el Veterinario");
		json.setData(savedVeterinario.toJson());
		return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
}



	@PutMapping(produces = "application/json", consumes = "application/json")
	public ResponseEntity<Object> updateVeterinario(@Validated({VeterinarioDTO.PutAndDelete.class,VeterinarioDTO.PutAndPost.class}) @RequestBody VeterinarioDTO veterinarioDTO) throws JsonProcessingException{
	Optional<Veterinario> existingVeterinario = veterinarioService.findByMatricula(veterinarioDTO.getMatricula());
	Response json = new Response();
	if(existingVeterinario.isPresent()){
		json.setMessage("El Veterinario ingresado ya existe");
		json.setTitle("ERROR");
		return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST); 
	}
	Veterinario veterinario = new Veterinario();
	veterinario.setID(veterinarioDTO.getID());
	veterinario.setMatricula(veterinarioDTO.getMatricula());
	veterinario.setNombre(veterinarioDTO.getNombre());
	veterinario.setApellido(veterinarioDTO.getApellido());
	veterinario.setTelefono(veterinarioDTO.getTelefono());
	veterinario.setDireccion(veterinarioDTO.getDireccion());
	Veterinario updatedVeterinario=this.veterinarioService.updateById(veterinario,(long) veterinario.getID());
	json.setMessage("Se ha actualizado el Veterinario");
	json.setData(updatedVeterinario.toJson());
	return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
	}

	@DeleteMapping(produces = "application/json", consumes = "application/json")
	@Transactional
	public ResponseEntity<Object> eliminarVeterinario(@Validated(VeterinarioDTO.PutAndDelete.class) @RequestBody VeterinarioDTO veterinarioDTO) throws JsonProcessingException {
	Optional<Veterinario> existingVeterinario = veterinarioService.getById(veterinarioDTO.getID());
	Response json = new Response();
	if(existingVeterinario.isEmpty()){
		json.setMessage("El Veterinario no existe");
		json.setTitle("ERROR");
		return new ResponseEntity<Object>(json.toJson(), HttpStatus.NOT_FOUND); 
	}
	veterinarioService.eliminarLogico(veterinarioDTO.getID());
	json.setMessage("Se ha eliminado el veterinario");
	json.setData(existingVeterinario.get().toJson());

	//BORRAR USUARIO


	return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
	}  





}