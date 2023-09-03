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
import com.veterinaria.vet.Models.Mascota;
import com.veterinaria.vet.Models.Practica;
import com.veterinaria.vet.Models.PracticaAtencion;
import com.veterinaria.vet.Models.PracticaAtencionId;
import com.veterinaria.vet.Models.Response;
import com.veterinaria.vet.Services.AtencionService;
import com.veterinaria.vet.Services.MascotaService;
import com.veterinaria.vet.Services.PracticaAtencionService;
import com.veterinaria.vet.Services.PracticaService;
import com.veterinaria.vet.Services.VeterinarioService;
import com.veterinaria.vet.annotations.CheckVet;

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
    @Autowired
    private PracticaAtencionService practicaAtencionService;

    @CheckVet
    @GetMapping(path = "/cliente")
    public ArrayList<Atencion> getAtencionesCliente(@RequestParam("id") Long id) {
        return this.atencionService.getAllAtencionesCliente(id);
    }

    @CheckVet
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getAtencionById(@RequestParam("id") Long id) {
        Optional<Atencion> existingAtencion = atencionService.getById(id);
        if (!existingAtencion.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(existingAtencion);
    }

    @CheckVet
    @Transactional
    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<Object> save(@Validated(AtencionDTO.PutAndPost.class) @RequestBody AtencionDTO atencionDTO, HttpSession session) throws JsonProcessingException {

        Response json = new Response();
        LocalDate hoy = LocalDate.now();
        if ((atencionDTO.getFechaPago()!=null) &&( atencionDTO.getFechaPago().isBefore(hoy))) {
            json.setMessage("La fecha de pago no puede ser anterior a hoy");
            json.setTitle("ERROR");
            return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST);
        }
        Long user_id = (Long) session.getAttribute("user_id");
        Atencion atencion = new Atencion();
        List<PracticaAtencion> practicasAtenciones = new ArrayList<PracticaAtencion>();
        
        atencion.setMascota(mascotaService.getById(atencionDTO.getMascotaID()).get());

        atencion.setVeterinario(veterinarioService.getByUserId(user_id).get());

        if (atencionDTO.getFechaPago() != null) {
            atencion.setFechaPago(atencionDTO.getFechaPago());
        } else{
            atencion.setFechaPago(null);
        }

        atencion.setFechaAtencion(atencionDTO.getFechaAtencion());
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

    @CheckVet
    @Transactional
    @PutMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<Object> updateAtencion(@Validated({AtencionDTO.PutAndPost.class,AtencionDTO.PutAndDelete.class}) @RequestBody AtencionDTO atencionDTO, HttpSession session) throws JsonProcessingException {

        Optional<Mascota> exsistingMascota = mascotaService.getById(atencionDTO.getMascotaID());
        Response json = new Response();

        if (!exsistingMascota.isPresent()) {
            json.setMessage("No existe dicha raza");
            json.setTitle("ERROR");
            return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST);
        }
        Optional<Atencion> atencion = atencionService.getById(atencionDTO.getID());
        if (atencion.isEmpty()) {
            json.setMessage("La atención no existe");
            json.setTitle("ERROR");
            return new ResponseEntity<Object>(json.toJson(), HttpStatus.NOT_FOUND);
        }
        if (atencionDTO.getFechaPago() != null) {
            atencion.get().setFechaPago(atencionDTO.getFechaPago());
        } else{
            atencion.get().setFechaPago(null);
        }
        atencion.get().setFechaAtencion(atencionDTO.getFechaAtencion());

        atencion.get().setMascota(mascotaService.getById(atencionDTO.getMascotaID()).get());
        Long user_id = (Long) session.getAttribute("user_id");
        atencion.get().setVeterinario(veterinarioService.getByUserId(user_id).get());
        
        Atencion updatedAtencion = atencionService.updateById(atencion.get(), (long) atencion.get().getID());
        
      /*  List<PracticaAtencion> practicasAtenciones = new ArrayList<PracticaAtencion>();
        for (Long id : atencionDTO.getPracticasID()) {
            PracticaAtencion practicaAtencion = new PracticaAtencion();
            practicaAtencion.setPractica(practicaService.getById(id).get());
            practicaAtencion.setAtencion(updatedAtencion);
            practicaAtencion.setPrecioPactado(practicaAtencion.getPractica().getLastPrice().getValor());
            practicaAtencion.setId(new PracticaAtencionId(updatedAtencion.getID(), id));
            practicasAtenciones.add(practicaAtencion);
        }*/



        // Obtener las prácticas actuales asociadas a la atención
        List<PracticaAtencion> practicasActuales = atencion.get().getPracticasAtenciones();

        // Crear una lista para las nuevas prácticas seleccionadas
        List<PracticaAtencion> nuevasPracticas = new ArrayList<>();

        for (Long id : atencionDTO.getPracticasID()) {
            Practica practica = practicaService.getById(id).orElse(null);

            if (practica != null) {
                // Verificar si la práctica actual ya existe
                PracticaAtencion practicaExistente = practicasActuales.stream()
                    .filter(pa -> pa.getPractica().getID() == id)
                    .findFirst()
                    .orElse(null);

                if (practicaExistente == null) {
                    // Si no existe, crear una nueva relación
                    PracticaAtencion nuevaPracticaAtencion = new PracticaAtencion();
                    nuevaPracticaAtencion.setPractica(practica);
                    nuevaPracticaAtencion.setAtencion(updatedAtencion);
                    nuevaPracticaAtencion.setPrecioPactado(practica.getLastPrice().getValor());
                    nuevaPracticaAtencion.setId(new PracticaAtencionId(updatedAtencion.getID(), id));

                    nuevasPracticas.add(nuevaPracticaAtencion);
                } else {
                    // Si ya existe, simplemente agregarla a la lista de nuevas prácticas
                    nuevasPracticas.add(practicaExistente);
                }
            }
        }

        // Eliminar las prácticas anteriores que no estén en la lista de nuevas prácticas
        for (PracticaAtencion practicaAEliminar : practicasActuales) {
            if (!nuevasPracticas.contains(practicaAEliminar)) {
                practicaAtencionService.delete(practicaAEliminar);
            }
        }


        
        updatedAtencion.setPracticasAtenciones(nuevasPracticas);

        json.setMessage("Se ha actualizado la atencion");
        json.setData(updatedAtencion.toJson());
        return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
    }

    @CheckVet
    @DeleteMapping()
    @Transactional
    public ResponseEntity<?> eliminarAtencion(@Validated(AtencionDTO.PutAndDelete.class) @RequestBody AtencionDTO atencionDTO) throws Exception {
        Optional<Atencion> existingAtencion = atencionService.getById(atencionDTO.getID());
        Response json = new Response();
        if (existingAtencion.isEmpty()) {
            json.setMessage("La atención no existe");
            json.setTitle("ERROR");
            return new ResponseEntity<Object>(json.toJson(), HttpStatus.NOT_FOUND);
        }
        atencionService.eliminarLogico(existingAtencion.get().getID());
        json.setMessage("Se ha eliminado la atención");
        json.setData(existingAtencion.get().toJson());
        return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
    }
}
