package com.example.wow_guau.model;

public class Usuario {
    private String nombre;
    private  int edad;
    private String direccionFoto;
    private Direccion direccion;

    public Usuario(String nombre, int edad, String direccionFoto, Direccion direccion) {
        this.nombre = nombre;
        this.edad = edad;
        this.direccionFoto = direccionFoto;
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getDireccionFoto() {
        return direccionFoto;
    }

    public void setDireccionFoto(String direccionFoto) {
        this.direccionFoto = direccionFoto;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }
}
