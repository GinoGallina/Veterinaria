package com.veterinaria.vet.Controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.veterinaria.vet.DTO.AtencionDTO;
import com.veterinaria.vet.Models.Atencion;
import com.veterinaria.vet.Models.PracticaAtencion;
import com.veterinaria.vet.Models.PracticaAtencionId;
import com.veterinaria.vet.Models.Response;
import com.veterinaria.vet.Services.AtencionService;
import com.veterinaria.vet.Services.MascotaService;
import com.veterinaria.vet.Services.PracticaService;
import com.veterinaria.vet.Services.VeterinarioService;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/Atenciones")
public class AtencionController {
    @Autowired
    private AtencionService atencionService;
    @Autowired
    private VeterinarioService veterinarioService;
    @Autowired
    private MascotaService mascotaService;
    @Autowired
    private PracticaService practicaService;

    @GetMapping(path = "/cliente")
    public ArrayList<Atencion> getAtencionesCliente(@RequestParam("id") Long id) {
        return this.atencionService.getAllAtencionesCliente(id);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getAtencionById(@RequestParam("id") Long id) {
        Optional<Atencion> existingAtencion = atencionService.getById(id);
        if (!existingAtencion.isPresent()) {
            // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario con ID "
            // + id + " no fue encontrado.");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(existingAtencion);
    }

    @Transactional
    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<Object> save(@Validated(AtencionDTO.PutAndPost.class) @RequestBody AtencionDTO atencionDTO, HttpSession session) throws JsonProcessingException {
        if (atencionDTO.getPracticasID().isEmpty()) {
            return ResponseEntity.badRequest().body("Debe seleccionar al menos una practica");
        }
        if (atencionDTO.getMascotaID() == null) {
            return ResponseEntity.badRequest().body("Debe seleccionar una mascota");
        }
        LocalDate hoy = LocalDate.now();
        if (atencionDTO.getFechaAtencion() == null) {
            return ResponseEntity.badRequest().body("Debe seleccionar una fecha de atencion");
        } else if (atencionDTO.getFechaAtencion().isAfter(hoy)) {
            return ResponseEntity.badRequest().body("La fecha de atencion no puede ser posterior a hoy");
        }
        Long user_id = (Long) session.getAttribute("user_id");
        Response json = new Response();
        Atencion atencion = new Atencion();
        List<PracticaAtencion> practicasAtenciones = new ArrayList<PracticaAtencion>();
        
        atencion.setMascota(mascotaService.getById(atencionDTO.getMascotaID()).get());
        atencion.setVeterinario(veterinarioService.getByUserId(user_id).get());
        if (atencionDTO.getFechaPago() != null) {
            atencion.setFechaPago(atencionDTO.getFechaPago());
        }
        atencion.setFechaAtencion(atencionDTO.getFechaAtencion());
        //System.out.println(atencion.toJson());
        Atencion savedAtencion = atencionService.saveAtencion(atencion);

        for (Long id : atencionDTO.getPracticasID()) {
            PracticaAtencion practicaAtencion = new PracticaAtencion();
            practicaAtencion.setPractica(practicaService.getById(id).get());
            practicaAtencion.setAtencion(savedAtencion);
            practicaAtencion.setPrecioPactado(practicaAtencion.getPractica().getLastPrice().getValor());
            practicaAtencion.setId(new PracticaAtencionId(savedAtencion.getID(), id));
            practicasAtenciones.add(practicaAtencion);
        }
        savedAtencion.setPracticasAtenciones(practicasAtenciones);

        json.setMessage("Se ha guardado la atención");
		json.setData(savedAtencion.toJson());
		return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<?> updateAtencion(@RequestBody Atencion Atencion) {
        // Optional<Atencion> existingAtencion =
        // atencionService.findByDni(Atencion.getDni());
        // if(existingAtencion.isPresent() &&
        // (existingAtencion.get().getID()!=Atencion.getID() )){
        // return ResponseEntity.badRequest().body("Ya existe un Atencion con la misma
        // matrícula");
        // }
        Atencion updatedAtencion = this.atencionService.updateById(Atencion, (long) Atencion.getID());

        return ResponseEntity.ok(updatedAtencion);
    }

    @DeleteMapping()
    @Transactional
    public ResponseEntity<?> eliminarAtencion(@RequestBody Atencion Atencion) throws Exception {
        Long id = (long) Atencion.getID();
        Optional<Atencion> atencion = atencionService.getById(id);
        if (atencion.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        atencionService.eliminarLogico(id);
        return ResponseEntity.ok().build();
    }

}
