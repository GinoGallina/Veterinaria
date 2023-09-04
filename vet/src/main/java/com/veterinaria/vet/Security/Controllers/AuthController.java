package com.veterinaria.vet.Security.Controllers;

import javax.security.auth.login.LoginException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.veterinaria.vet.Models.Response;
import com.veterinaria.vet.Security.DTO.LoginUser;
import com.veterinaria.vet.Security.DTO.NewUser;
import com.veterinaria.vet.Security.Services.UserService;
import com.veterinaria.vet.Security.jwt.JwtProvider;
import com.veterinaria.vet.annotations.CheckLogin;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {
    @Autowired
    UserService userService;

    @PostMapping(path = "/login")
    public RedirectView login(HttpSession session, @Validated @ModelAttribute LoginUser login) throws LoginException {
        boolean is_loggued = userService.login(login, session);
        if (is_loggued) {
            return new RedirectView("/inicio");
        }
        return new RedirectView("/Auth/Login");
    }

    @PostMapping(path = "/register")
    public Object register(HttpSession session, @Validated @ModelAttribute NewUser nuevoUsuario) throws JsonProcessingException, LoginException {
        ResponseEntity<?> response = userService.save(nuevoUsuario);
        if (response.getStatusCode() == HttpStatus.OK) {
            NewUser newUser = new NewUser();
            newUser = (NewUser) response.getBody();
            LoginUser loginUser = new LoginUser();
            loginUser.setEmail(newUser.getEmail());
            loginUser.setPassword(newUser.getPassword());

            boolean is_loggued= userService.login(loginUser, session);
            if (is_loggued) {
                return new RedirectView("/inicio");
            }
            return new RedirectView("/Auth/Login");
  
        } else{
            Response json = new Response();
            json = (Response) response.getBody();
            return new ResponseEntity<Object>(json, HttpStatus.BAD_REQUEST);
        }
    }
    
    @CheckLogin
    @PostMapping("/logout")
    public RedirectView logout(HttpSession session){
        userService.logout(session);
        return new RedirectView("/login");
    }
}




