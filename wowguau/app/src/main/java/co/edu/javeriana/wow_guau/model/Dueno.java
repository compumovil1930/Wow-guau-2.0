package co.edu.javeriana.wow_guau.model;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Dueno extends Usuario {

    List<Perro> Mascotas;
    GeoPoint ubicacion;

    public Dueno(){}

    public Dueno(String correo, String nombre, long cedula, Date fechaNacimiento, long telefono,
                 String genero, String direccion) {
        super(correo, nombre, cedula, fechaNacimiento, telefono, genero, direccion);
        Mascotas = new ArrayList<>();
    }

    public List<Perro> getMascotas() {
        return Mascotas;
    }

    public void setMascotas(List<Perro> mascotas) {
        Mascotas = mascotas;
    }

    public GeoPoint getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(GeoPoint ubicacion) {
        this.ubicacion = ubicacion;
    }
}
