package com.veterinaria.vet.Controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.veterinaria.vet.Models.Practica;
import com.veterinaria.vet.Models.Precio;
import com.veterinaria.vet.Services.PracticaService;

@Controller
@RequestMapping("/Practicas")
public class PracticaController {
    @Autowired
    private PracticaService practicaService;

    @GetMapping(path = "/elegidas")
    public ArrayList<Practica> getPracticasElegidas(@RequestParam("ids") List<Long> ids) {
        return this.practicaService.getPracticasElegidas(ids);
    }

    @GetMapping(path = "/Index")
    public ModelAndView getPracticas() {
        ArrayList<Practica> practicas = this.practicaService.getAllPracticas();

        for (Practica practica : practicas) {
            // Obtener todos los precios de la practica y setear el ultimo ordenado por el campo CreatedAt descendiente
            List<Precio> precios = new LinkedList<Precio>(practica.getPrecios());
            Collections.sort(precios, Comparator.comparing(Precio::getCreatedAt).reversed());
            if (precios.size() > 0) {
                practica.setUltimoPrecio(precios.get(0));
            }
        }


        ModelAndView modelAndView = new ModelAndView("Practicas/Index");
        modelAndView.addObject("practicas", practicas);
        return modelAndView;
    }

    /*
     * @PostMapping(produces = "application/json", consumes = "application/json")
     * public ResponseEntity<Object>
     * save(@Validated(PracticaDTO.PutAndPost.class) @RequestBody PracticaDTO
     * practicaDTO) throws JsonProcessingException {
     * Optional<Practica> existingPractica =
     * practicaService.findByDescripcion(practicaDTO.getDescripcion());
     * Response json = new Response();
     * if (existingPractica.isPresent()) {
     * if (!practicaDTO.getById(existingPractica.get().getID()).isPresent()) {
     * practicaDTO.saveLogico(existingPractica.get().getID());
     * json.setMessage("La especie se encontraba eliminada y se ha recuperado");
     * json.setData(existingPractica.get().toJson());
     * return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
     * } else {
     * json.setMessage("La especie ingresada ya existe");
     * json.setTitle("ERROR");
     * return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST);
     * }
     * }
     * Especie especie = new Especie();
     * especie.setDescripcion(practicaDTO.getDescripcion());
     * Especie savedEspecie = practicaService.saveEspecie(especie);
     * json.setMessage("Se ha guardado la especie");
     * json.setData(savedEspecie.toJson());
     * return new ResponseEntity<Object>(json.toJson(), HttpStatus.OK);
     * }
     * 
     * @PostMapping()
     * public ResponseEntity<?> save(@RequestBody Practica
     * Practica,@RequestParam("precio") BigDecimal precio){
     * Optional<Practica> existingPractica =
     * practicaService.findByDescripcion(Practica.getDescripcion());
     * if(existingPractica.isPresent()){
     * if (!practicaService.getById(existingPractica.get().getID()).isPresent()) {
     * practicaService.saveLogico(existingPractica.get().getID());
     * return ResponseEntity.ok(existingPractica.get());
     * } else {
     * return ResponseEntity.badRequest().
     * body("Ya existe un Practica con la misma descripcion");
     * }}
     * Practica savedPractica = practicaService.savePractica(Practica);
     * Precio savedPrecio= new Precio(savedPractica,precio);
     * precioService.savePrecio(savedPrecio);
     * return ResponseEntity.ok(savedPractica);
     * }
     */

    @PutMapping()
    public ResponseEntity<?> updatePractica(@RequestBody Practica Practica) {
        Optional<Practica> existingPractica = practicaService.findByDescripcion(Practica.getDescripcion());
        if (existingPractica.isPresent() && (existingPractica.get().getID() != Practica.getID())) {
            return ResponseEntity.badRequest().body("Ya existe un Practica con la misma descripcion");
        }
        Practica updatedPractica = this.practicaService.updateById(Practica, (long) Practica.getID());
        return ResponseEntity.ok(updatedPractica);
    }

    @DeleteMapping()
    @Transactional
    public ResponseEntity<?> eliminarPractica(@RequestBody Practica Practica) throws Exception {
        Long id = (long) Practica.getID();
        Optional<Practica> practica = practicaService.getById(id);
        if (practica.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        practicaService.eliminarLogico(id);
        return ResponseEntity.ok().build();
    }

}
