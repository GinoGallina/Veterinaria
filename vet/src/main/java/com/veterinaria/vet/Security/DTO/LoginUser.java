package com.veterinaria.vet.Security.DTO;

import jakarta.validation.constraints.NotBlank;

public class LoginUser {
    @NotBlank
    private String Email;
    @NotBlank
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