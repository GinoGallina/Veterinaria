package com.veterinaria.vet.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.veterinaria.vet.annotations.CheckLogin;

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

    @CheckLogin
    @GetMapping("/inicio")
    public ModelAndView inicio(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("Home/Index");
        modelAndView.addObject("user_email", session.getAttribute("user_email"));
        modelAndView.addObject("user_role", session.getAttribute("user_role"));
        return modelAndView; // Nombre de la vista Thymeleaf (index.html)
    }
}
