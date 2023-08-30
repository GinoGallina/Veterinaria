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
import com.veterinaria.vet.DTO.RazaDTO;
import com.veterinaria.vet.Models.Especie;
import com.veterinaria.vet.Models.Raza;
import com.veterinaria.vet.Models.Response;
import com.veterinaria.vet.Services.EspecieService;
import com.veterinaria.vet.Services.RazaService;
import com.veterinaria.vet.annotations.CheckAdmin;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/Razas")
public class RazaController {
	@Autowired
	private RazaService razaService;
	@Autowired
	private EspecieService especieService;

	@CheckAdmin
	@GetMapping(path = "/Index")
	public ModelAndView getRazas(HttpSession session) {
		ArrayList<Raza> razas = this.razaService.getAllRazas();
		ArrayList<Especie> especies = this.especieService.getAllEspecies();
		ModelAndView modelAndView = new ModelAndView("Razas/Index");
		modelAndView.addObject("razas", razas);
		modelAndView.addObject("especies", especies);
		modelAndView.addObject("user_role", session.getAttribute("user_role"));
		return modelAndView;
	}

	@CheckAdmin
	@PostMapping(produces = "application/json", consumes = "application/json")
	public ResponseEntity<Object> save(@Validated(RazaDTO.PostAndPut.class) @RequestBody RazaDTO razaDTO)
			throws JsonProcessingException {
		Optional<Raza> existingRaza = razaService.findByDescripcion(razaDTO.getDescripcion());
		Optional<Especie> existingEspecie = especieService.getById(razaDTO.getEspecieID());
		Response json = new Response();
		if (!existingEspecie.isPresent()) {
			json.setMessage("No existe dicha especie");
			json.setTitle("ERROR");
			return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST);
		}
		if (existingRaza.isPresent()) {
			if (!razaService.getById(existingRaza.get().getID()).isPresent()) {
				razaService.saveLogico(existingRaza.get().getID());
				json.setMessage("La raza se encontraba eliminada y se ha recuperado");
				json.setData(existingRaza.get().toJson());
				return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
			} else {
				json.setMessage("La raza ingresada ya existe");
				json.setTitle("ERROR");
				return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST);
			}
		}
		Raza raza = new Raza();
		raza.setDescripcion(razaDTO.getDescripcion());
		raza.setEspecie(existingEspecie.get());
		Raza savedRaza = razaService.saveRaza(raza);
		json.setMessage("Se ha guardado la raza");
		json.setData(savedRaza.toJson());
		return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
	}

	@CheckAdmin
	@PutMapping(produces = "application/json", consumes = "application/json")
	public ResponseEntity<Object> updateRaza(
			@Validated({ RazaDTO.PutAndDelete.class, RazaDTO.PostAndPut.class }) @RequestBody RazaDTO razaDTO)
			throws JsonProcessingException {
		Optional<Raza> existingRaza = razaService.findByDescripcion(razaDTO.getDescripcion());
		Optional<Especie> existingEspecie = especieService.getById(razaDTO.getEspecieID());
		Response json = new Response();
		if (!existingEspecie.isPresent()) {
			json.setMessage("No existe dicha especie");
			json.setTitle("ERROR");
			return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST);
		}
		if (existingRaza.isPresent()) {
			json.setMessage("La raza ingresada ya existe");
			json.setTitle("ERROR");
			return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST);
		}
		Optional<Raza> raza = razaService.getById(razaDTO.getID());
		if (raza.isEmpty()) {
			json.setMessage("La raza no existe");
			json.setTitle("ERROR");
			return new ResponseEntity<Object>(json.toJson(), HttpStatus.NOT_FOUND);
		}

		raza.get().setDescripcion(razaDTO.getDescripcion());
		raza.get().setEspecie(existingEspecie.get());
		Raza updatedRaza = this.razaService.updateById(raza.get(), (long) raza.get().getID());
		json.setMessage("Se ha actualizado la raza");
		json.setData(updatedRaza.toJson());
		return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
	}

	@CheckAdmin
	@DeleteMapping(produces = "application/json", consumes = "application/json")
	@Transactional
	public ResponseEntity<Object> eliminarRaza(@Validated(RazaDTO.PutAndDelete.class) @RequestBody RazaDTO razaDTO)
			throws JsonProcessingException {
		Optional<Raza> existingRaza = razaService.getById(razaDTO.getID());
		Response json = new Response();
		if (existingRaza.isEmpty()) {
			json.setMessage("La raza no existe");
			json.setTitle("ERROR");
			return new ResponseEntity<Object>(json.toJson(), HttpStatus.NOT_FOUND);
		}
		razaService.eliminarLogico(razaDTO.getID());
		json.setMessage("Se ha eliminado la raza");
		json.setData(existingRaza.get().toJson());
		return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
	}

}
