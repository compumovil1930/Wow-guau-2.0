package co.edu.javeriana.wowguau_paseador.model;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.Date;

@IgnoreExtraProperties
public abstract class Usuario implements Serializable {
    private String correo;
    private String nombre;
    private long cedula;
    private Date fechaNacimiento;
    private long telefono;
    private String genero;
    private Direccion direccion;
    private float saldo;
    private String direccionFoto;


    public Usuario(String correo, String nombre, long cedula, Date fechaNacimiento, long telefono, String genero, Direccion direccion) {
        this.correo = correo;
        this.nombre = nombre;
        this.cedula = cedula;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.genero = genero;
        this.direccion = direccion;
        this.saldo = 0;
    }

    public Usuario() {
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getCedula() {
        return cedula;
    }

    public void setCedula(long cedula) {
        this.cedula = cedula;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public long getTelefono() {
        return telefono;
    }

    public void setTelefono(long telefono) {
        this.telefono = telefono;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
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

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }
}
/*
        // Old:
        java.util.Date date = snapshot.getDate("created_at");
        // New:
        Timestamp timestamp = snapshot.getTimestamp("created_at");
        java.util.Date date = timestamp.toDate();
         */