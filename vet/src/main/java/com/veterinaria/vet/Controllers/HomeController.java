package com.veterinaria.vet.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;


@Controller
public class HomeController {

    @RequestMapping(value = { "/", "/index.html","/home","/index"})
    public String redirectToPage(HttpSession session) {
        
        if (session.getAttribute("user_id") != null) {
            return "redirect:/inicio";  
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        if (session.getAttribute("user_id") != null) {
            return "redirect:/inicio";
        }
        return "Auth/Login"; // Nombre de la vista Thymeleaf (index.html)
    }
    @GetMapping("/inicio")
    public String inicio() {
        return "Home/Index"; // Nombre de la vista Thymeleaf (index.html)
    }
    @GetMapping("/prueba")
    public String prueba() {
        return "Shared/prueba"; // Nombre de la vista Thymeleaf (index.html)
    }

    @RequestMapping("/**")
        public void unmappedRequest() throws HttpRequestMethodNotSupportedException {
            throw new HttpRequestMethodNotSupportedException(null);
    }
}
