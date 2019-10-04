package com.example.wow_guau.model;

public class Perro {
    private String nombre;
    private String raza;
    private String sexo;
    private String direccionCarnetVacunas;
    private String observaciones;

    public Perro() {
    }

    public Perro(String nombre, String raza, String sexo, String direccionCarnetVacunas, String observaciones) {
        this.nombre = nombre;
        this.raza = raza;
        this.sexo = sexo;
        this.direccionCarnetVacunas = direccionCarnetVacunas;
        this.observaciones = observaciones;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getDireccionCarnetVacunas() {
        return direccionCarnetVacunas;
    }

    public void setDireccionCarnetVacunas(String direccionCarnetVacunas) {
        this.direccionCarnetVacunas = direccionCarnetVacunas;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
