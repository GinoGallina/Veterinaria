package com.veterinaria.vet.Security.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "Email",nullable = false)
    private String email;

    @Column(name = "Password",nullable = false)
    private String password;

    @Column(name = "UserToken")
    private String userToken;

    @ManyToOne
    @JoinColumn(name = "RolID", nullable = false)
    private Rol rol;

//     @ManyToMany(fetch = FetchType.EAGER)
//     @JoinTable(
//         name = "users_roles",
//         joinColumns = @JoinColumn(name = "user_id"),
//         inverseJoinColumns = @JoinColumn(name = "rol_id")
//     )
//    private Set<Rol> roles = new HashSet<>();


    public User(){

    }

    public Long getID() {
        return ID;
    }

    public void setID(Long iD) {
        ID = iD;
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

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

   



}