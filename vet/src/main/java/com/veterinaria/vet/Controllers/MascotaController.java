package com.veterinaria.vet.Controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.veterinaria.vet.DTO.MascotaDTO;
import com.veterinaria.vet.Models.Cliente;
import com.veterinaria.vet.Models.Mascota;
import com.veterinaria.vet.Models.Raza;
import com.veterinaria.vet.Models.Response;
import com.veterinaria.vet.Services.ClienteService;
import com.veterinaria.vet.Services.MascotaService;
import com.veterinaria.vet.Services.RazaService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/Mascotas")
public class MascotaController {
    @Autowired
    private MascotaService mascotaService;
    @Autowired
    private RazaService razaService;
    @Autowired
    private ClienteService clienteService;

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<Object> save(@Validated(MascotaDTO.PutAndPost.class) @RequestBody MascotaDTO mascotaDTO)
            throws JsonProcessingException {
        Response json = new Response();
        Optional<Raza> exsisteingRaza = razaService.getById(mascotaDTO.getRazaID());
        Optional<Cliente> existingCliente = clienteService.getById(mascotaDTO.getClienteID());
        if (!exsisteingRaza.isPresent()) {
            json.setMessage("No existe dicha raza");
            json.setTitle("ERROR");
            return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST);
        } else if (!existingCliente.isPresent()) {
            json.setMessage("No existe dicho cliente");
            json.setTitle("ERROR");
            return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST);
        }
        Mascota mascota = new Mascota();
        mascota.setNacimiento(mascotaDTO.getNacimiento());
        mascota.setNombre(mascotaDTO.getNombre());
        mascota.setSexo(mascotaDTO.getSexo().charAt(0));
        mascota.setCliente(existingCliente.get());
        mascota.setRaza(exsisteingRaza.get());
        Mascota savedMascota = mascotaService.saveMascota(mascota);
        json.setMessage("Se ha guardado la mascota");
        json.setData(savedMascota.toJson());
        return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<Object> updateMascota(@Validated({ MascotaDTO.PutAndDelete.class,
            MascotaDTO.PutAndPost.class }) @RequestBody MascotaDTO mascotaDTO) throws JsonProcessingException {
        Response json = new Response();
        Optional<Raza> exsisteingRaza = razaService.getById(mascotaDTO.getRazaID());
        Optional<Cliente> existingCliente = clienteService.getById(mascotaDTO.getClienteID());
        if (!exsisteingRaza.isPresent()) {
            json.setMessage("No existe dicha raza");
            json.setTitle("ERROR");
            return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST);
        } else if (!existingCliente.isPresent()) {
            json.setMessage("No existe dicho cliente");
            json.setTitle("ERROR");
            return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST);
        }
        Optional<Mascota> mascota = mascotaService.getById(mascotaDTO.getID());
        if (mascota.isEmpty()) {
            json.setMessage("La mascota no existe");
            json.setTitle("ERROR");
            return new ResponseEntity<Object>(json.toJson(), HttpStatus.NOT_FOUND);
        }
        mascota.get().setID(mascotaDTO.getID());
        mascota.get().setNacimiento(mascotaDTO.getNacimiento());
        mascota.get().setNombre(mascotaDTO.getNombre());
        mascota.get().setSexo(mascotaDTO.getSexo().charAt(0));
        mascota.get().setCliente(existingCliente.get());
        mascota.get().setRaza(exsisteingRaza.get());
        Mascota updatedMascota = mascotaService.updateById(mascota.get(), (long) mascota.get().getID());
        json.setMessage("Se ha actualizado la mascota");
        json.setData(updatedMascota.toJson());
        return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
    }

    @DeleteMapping(produces = "application/json", consumes = "application/json")
    @Transactional
    public ResponseEntity<Object> eliminarMascota(
            @Validated(MascotaDTO.PutAndDelete.class) @RequestBody MascotaDTO mascotaDTO)
            throws JsonProcessingException {
        Optional<Mascota> existingMascota = mascotaService.getById(mascotaDTO.getID());
        Response json = new Response();
        if (existingMascota.isEmpty()) {
            json.setMessage("La mascota no existe");
            json.setTitle("ERROR");
            return new ResponseEntity<Object>(json.toJson(), HttpStatus.NOT_FOUND);
        }
        mascotaService.eliminarLogico(existingMascota.get().getID());
        json.setMessage("Se ha eliminado la mascota");
        json.setData(existingMascota.get().toJson());
        return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
    }

}
