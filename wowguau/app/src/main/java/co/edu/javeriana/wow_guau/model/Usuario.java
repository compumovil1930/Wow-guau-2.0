package co.edu.javeriana.wow_guau.model;

import java.util.Date;

public abstract class Usuario
{
    private String correo;
    private String nombre;
    private int cedula;
    private Date fechaNacimiento;
    private int telefono;
    private String genero;
    private String direccionFoto;
    private Direccion direccion;

    public Usuario(String correo, String nombre, int cedula, Date fechaNacimiento, int telefono, String genero, Direccion direccion) {
        this.correo = correo;
        this.nombre = nombre;
        this.cedula = cedula;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.genero = genero;
        this.direccion = direccion;
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

    public int getCedula() {
        return cedula;
    }

    public void setCedula(int cedula) {
        this.cedula = cedula;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
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
}
