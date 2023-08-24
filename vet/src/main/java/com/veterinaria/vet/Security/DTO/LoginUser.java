package com.veterinaria.vet.Security.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginUser {
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El  email debe ser una dirección de correo electrónico válida")
    private String Email;
    @NotBlank(message = "La contraseña no puede estar vacía")
    private String Password;
    public String getEmail() {
        return Email;
    }
    public void setEmail(String email) {
        Email = email;
    }
    public String getPassword() {
        return Password;
    }
    public void setPassword(String password) {
        Password = password;
    }

    

}