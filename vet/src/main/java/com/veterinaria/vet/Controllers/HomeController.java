package com.veterinaria.vet.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;


@Controller
public class HomeController {

    @RequestMapping(value = { "/", "/index.html","/home","/index"})
    public String redirectToPage(HttpSession session) {
        
        if (session.getAttribute("user_id") != null) {
            return "inicio";  
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/login")
    public String login() {
        return "Auth/Login"; // Nombre de la vista Thymeleaf (index.html)
    }
    @GetMapping("/")
    public String inicio() {
        return "Home/Index"; // Nombre de la vista Thymeleaf (index.html)
    }
}
