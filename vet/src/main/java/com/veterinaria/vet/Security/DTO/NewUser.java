package com.veterinaria.vet.Security.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class NewUser {
    @Email(message = "Dirección de email no válida")
    @NotBlank(message = "Email obligatorio")
    private String Email;
    @NotBlank(message = "Password obligatoria")
    private String Password;
    private String rol;
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
    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }



   
}