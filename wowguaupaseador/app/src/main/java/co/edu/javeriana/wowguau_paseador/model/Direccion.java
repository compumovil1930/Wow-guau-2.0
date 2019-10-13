package co.edu.javeriana.wowguau_paseador.model;

import android.location.Address;

import java.io.Serializable;

public class Direccion implements Serializable {
    private String direccion;
    private double latitud;
    private double longitud;

    public Direccion(String direccion, Address address) {
        this.direccion = direccion;
        this.latitud = address.getLatitude();
        this.longitud = address.getLongitude();
    }

    public Direccion() {
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}
