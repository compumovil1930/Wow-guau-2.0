package com.example.wow_guau.model;

import java.util.List;

public class Paseador extends Usuario {
    private String descripcion;
    private int experiencia;
    private List<String> direccionesCertificados;

    public Paseador(String nombre, int edad, String direccionFoto, Direccion direccion, String descripcion, int experiencia, List<String> direccionesCertificados) {
        super(nombre, edad, direccionFoto, direccion);
        this.descripcion = descripcion;
        this.experiencia = experiencia;
        this.direccionesCertificados = direccionesCertificados;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(int experiencia) {
        this.experiencia = experiencia;
    }

    public List<String> getDireccionesCertificados() {
        return direccionesCertificados;
    }

    public void setDireccionesCertificados(List<String> direccionesCertificados) {
        this.direccionesCertificados = direccionesCertificados;
    }
}
