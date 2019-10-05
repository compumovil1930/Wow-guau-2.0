package com.example.wow_guau.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Dueno extends Usuario {
    List<Perro> Mascotas;

    public Dueno(String correo, String nombre, int cedula, Date fechaNacimiento, int telefono, String genero, Direccion direccion) {
        super(correo, nombre, cedula, fechaNacimiento, telefono, genero, direccion);
        Mascotas = new ArrayList<>();
    }

    public List<Perro> getMascotas() {
        return Mascotas;
    }

    public void setMascotas(List<Perro> mascotas) {
        Mascotas = mascotas;
    }
}
