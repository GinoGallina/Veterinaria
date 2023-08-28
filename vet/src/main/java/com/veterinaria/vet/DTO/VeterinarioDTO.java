package com.veterinaria.vet.DTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;

public class VeterinarioDTO {

    @NotNull(message = "El campo id no puede estar vacío", groups = { PutAndDelete.class })
    private Long ID;

    @NotBlank(message = "El campo matricula no puede estar vacío", groups = { PutAndPost.class })
    @NotNull(message = "El campo matricula no puede estar vacío", groups = { PutAndPost.class })
    private String matricula;

    @NotBlank(message = "El campo nombre no puede estar vacío", groups = { PutAndPost.class })
    @NotNull(message = "El campo nombre no puede estar vacío", groups = { PutAndPost.class })
    @Pattern(regexp = "^[a-zA-Z\\s\\p{Punct}]*$", message = "El nombre solo debe contener letras y espacios", groups = {
            PutAndPost.class })
    private String nombre;

    @NotBlank(message = "El campo apellido no puede estar vacío", groups = { PutAndPost.class })
    @NotNull(message = "El campo apellido no puede estar vacío", groups = { PutAndPost.class })
    @Pattern(regexp = "^[a-zA-Z\\s\\p{Punct}]*$", message = "El apellido solo debe contener letras y espacios", groups = {
            PutAndPost.class })
    private String apellido;

    @NotBlank(message = "El campo direccion no puede estar vacío", groups = { PutAndPost.class })
    @NotNull(message = "El campo direccion no puede estar vacío", groups = { PutAndPost.class })
    @Pattern(regexp = "^[a-zA-Z0-9\\u00C0-\\u017F\\s]*$", message = "La dirección debe contener letras, números o espacios", groups = {
            PutAndPost.class })
    private String direccion;

    @NotBlank(message = "El campo telefono no puede estar vacío", groups = { PutAndPost.class })
    @NotNull(message = "El campo telefono no puede estar vacío", groups = { PutAndPost.class })
    @Pattern(regexp = "^[0-9]+$", message = "El telefono debe contener sólo números", groups = {
            PutAndPost.class })
    private String telefono;

    @NotBlank(message = "El campo telefono no puede estar vacío", groups = { PutAndPost.class })
    @Email(message = "El campo email debe ser una dirección de correo electrónico válida")
    @NotNull(message = "El campo email no puede estar vacío", groups = { PutAndPost.class })
    private String email;

    @NotBlank(message = "El campo contrase\u00F1a no puede estar vacío", groups = { Post.class })
    @NotNull(message = "El campo contraseña no puede estar vacío", groups = { Post.class })
    private String password;

    public Long getID() {
        return ID;
    }

    public void setID(Long iD) {
        ID = iD;
    }

    public String toJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(this);
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public interface PutAndDelete extends Default {
    }

    public interface PutAndPost extends Default {
    }

    public interface Post extends Default {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}