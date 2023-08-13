package com.veterinaria.vet.Security.Models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;

@Entity
@Table(name="roles")
public class Rol {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    @Column(name = "Descripcion",nullable = false)
    private String descripcion;



    public Rol(){

    }

    public Rol(int rolId, String desc) {
        this.ID = rolId;
        this.descripcion = desc;
    }

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descipcion) {
        this.descripcion = descipcion;
    }



}
