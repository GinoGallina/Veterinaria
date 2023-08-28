package com.veterinaria.vet.Controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
import com.veterinaria.vet.DTO.PracticaDTO;
import com.veterinaria.vet.Models.Practica;
import com.veterinaria.vet.Models.Precio;
import com.veterinaria.vet.Models.Response;
import com.veterinaria.vet.Services.PracticaService;
import com.veterinaria.vet.Services.PrecioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Practicas")
public class PracticaController {
    @Autowired
    private PracticaService practicaService;
    @Autowired
    private PrecioService precioService;

    @GetMapping(path = "/elegidas")
    public ArrayList<Practica> getPracticasElegidas(@RequestParam("ids") List<Long> ids) {
        return this.practicaService.getPracticasElegidas(ids);
    }

    @GetMapping(path = "/Index")
    public ModelAndView getPracticas(HttpSession session) {
        ArrayList<Practica> practicas = this.practicaService.getAllPracticas();

        for (Practica practica : practicas) {
            // Obtener todos los precios de la practica y setear el ultimo ordenado por el
            // campo CreatedAt descendiente
            List<Precio> precios = new LinkedList<Precio>(practica.getPrecios());
            Collections.sort(precios, Comparator.comparing(Precio::getCreatedAt).reversed());
            if (precios.size() > 0) {
                practica.setUltimoPrecio(precios.get(0));
            }
        }

        ModelAndView modelAndView = new ModelAndView("Practicas/Index");
        modelAndView.addObject("practicas", practicas);
        modelAndView.addObject("user_role", session.getAttribute("user_role"));
        return modelAndView;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<Object> save(@Validated(PracticaDTO.PutAndPost.class) @RequestBody PracticaDTO practicaDTO)
            throws JsonProcessingException {
        Optional<Practica> existingPractica = practicaService.findByDescripcion(practicaDTO.getDescripcion());
        Response json = new Response();
        Precio precio = new Precio();
        if (existingPractica.isPresent()) {
            if (!practicaService.getById(existingPractica.get().getID()).isPresent()) {
                practicaService.saveLogico(existingPractica.get().getID());
                existingPractica.get().setUltimoPrecio(existingPractica.get().getLastPrice());
                json.setMessage("La pracrica se encontraba eliminada y se ha recuperado");
                json.setData(existingPractica.get().toJson());
                precio.setPractica(existingPractica.get());
                precio.setValor(practicaDTO.getPrecio());
                precioService.savePrecio(precio);
                return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
            } else {
                json.setMessage("La práctica ingresada ya existe");
                json.setTitle("ERROR");
                return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST);
            }
        }
        Practica practica = new Practica();
        practica.setDescripcion(practicaDTO.getDescripcion());
        Practica savedPractica = practicaService.savePractica(practica);
        
        precio.setPractica(savedPractica);
        precio.setValor(practicaDTO.getPrecio());
        precioService.savePrecio(precio);
        savedPractica.setUltimoPrecio(precio);

        json.setMessage("Se ha guardado la práctica");
        json.setData(savedPractica.toJson());
        return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<Object> updateEspecie(
            @Validated({ PracticaDTO.PutAndDelete.class,
                    PracticaDTO.PutAndPost.class }) @RequestBody PracticaDTO practicaDTO)
            throws JsonProcessingException {
        Optional<Practica> existingPractica = practicaService.findByDescripcion(practicaDTO.getDescripcion());
        Response json = new Response();
        if (existingPractica.isPresent()) {
            json.setMessage("La práctica ingresada ya existe");
            json.setTitle("ERROR");
            return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST);
        }
        Optional<Practica> practica = practicaService.getById(practicaDTO.getID());
        if (practica.isEmpty()){
                json.setMessage("La práctica no existe");
                json.setTitle("ERROR");
                return new ResponseEntity<Object>(json.toJson(), HttpStatus.NOT_FOUND);
        }
        practica.get().setID(practicaDTO.getID());
        practica.get().setDescripcion(practicaDTO.getDescripcion());
        Practica updatedPractica = this.practicaService.updateById(practica.get(), (long) practica.get().getID());
        
        Precio precio = new Precio();
        precio.setPractica(updatedPractica);
        precio.setValor(practicaDTO.getPrecio());
        precioService.savePrecio(precio);
        
        updatedPractica.setUltimoPrecio(precio);
        
        json.setMessage("Se ha actualizado la practica");
        json.setData(updatedPractica.toJson());
        return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
    }

    @DeleteMapping(produces = "application/json", consumes = "application/json")
    @Transactional
    public ResponseEntity<?> eliminarPractica(
            @Validated(PracticaDTO.PutAndDelete.class) @RequestBody PracticaDTO practicaDTO)
            throws JsonProcessingException {
        Optional<Practica> existingPractica = practicaService.getById(practicaDTO.getID());
        Response json = new Response();
        if (existingPractica.isEmpty()){
                json.setMessage("La pr\u00E1ctica no existe");
                json.setTitle("ERROR");
                return new ResponseEntity<Object>(json.toJson(), HttpStatus.NOT_FOUND);
        }
        practicaService.eliminarLogico(practicaDTO.getID());
        json.setMessage("Se ha eliminado la práctica");
        json.setData(existingPractica.get().toJson());
        return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
    }


}
