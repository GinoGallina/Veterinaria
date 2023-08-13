package com.veterinaria.vet.Security.Controllers;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.veterinaria.vet.Security.DTO.JwtDTO;
import com.veterinaria.vet.Security.DTO.LoginUser;
import com.veterinaria.vet.Security.DTO.NewUser;
import com.veterinaria.vet.Security.Services.UserService;
import com.veterinaria.vet.Security.jwt.JwtProvider;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin
@Validated
public class AuthController {
    @Autowired
    UserService usuarioService;
    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/nuevo")
    public ResponseEntity<?> nuevo(@Valid @RequestBody NewUser nuevoUsuario){
        return usuarioService.save(nuevoUsuario);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDTO> login(@Valid @RequestBody LoginUser loginUsuario){
        return ResponseEntity.ok(usuarioService.login(loginUsuario));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtDTO> refresh(@RequestBody JwtDTO jwtDto) throws ParseException {
        return ResponseEntity.ok(usuarioService.refresh(jwtDto));
    }
    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String tokenHeader) {
        // Extraer el token de la cabecera (ten en cuenta que el token incluye la palabra "Bearer ")
        String token = tokenHeader.substring(7);

        // Validar el token
        boolean isValidToken = jwtProvider.validateToken(token);

        if (isValidToken) {
            return ResponseEntity.ok().build(); // Token válido, responder con OK (200)
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Token inválido, responder con UNAUTHORIZED (401)
        }
    }
}




