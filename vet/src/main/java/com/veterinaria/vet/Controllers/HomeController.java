package com.veterinaria.vet.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class HomeController {
    @RequestMapping(value = { "/", "/index.html","/home","/index"})
    public String redirectToPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // El usuario está autenticado, redirigir a la página de inicio
            return "redirect:/loginN";
        } else {
            // El usuario no está autenticado, redirigir a la página de inicio de sesión
            return "index";  
        }
    }

    @GetMapping("/loginN")
    public String login() {
        return "login"; // Nombre de la vista Thymeleaf (index.html)
    }
    @GetMapping("/inicio")
    public String inicio() {
        return "index"; // Nombre de la vista Thymeleaf (index.html)
    }
    @GetMapping("/veterinario")
    public String veterinarios() {
        return "veterinarios"; // Nombre de la vista Thymeleaf (index.html)
    }
    @GetMapping("/raza")
    public String razas() {
        return "razas"; // Nombre de la vista Thymeleaf (index.html)
    }
    @GetMapping("/especie")
    public String especies() {
        return "especies"; // Nombre de la vista Thymeleaf (index.html)
    }
    @GetMapping("/cliente")
    public String clientes() {
        return "clientes"; // Nombre de la vista Thymeleaf (index.html)
    }
    @GetMapping("/datosCliente")
    public String datosClientes(@RequestParam("id") Long id) {
        return "datosCliente"; // Nombre de la vista Thymeleaf (index.html)
    }
    @GetMapping("/proveedor")
    public String proveedores() {
        return "proveedores"; // Nombre de la vista Thymeleaf (index.html)
    }
    @GetMapping("/productosAdmin")
    public String productosAdmin() {
        return "productosAdmin"; // Nombre de la vista Thymeleaf (index.html)
    }
    @GetMapping("/practica")
    public String practicas() {
        return "practicas"; // Nombre de la vista Thymeleaf (index.html)
    }
}
