package co.edu.javeriana.wowguau_paseador.model;

import java.io.Serializable;
import java.util.Date;

public class Perro  implements Serializable {
    private String direccionFoto;
    private String nombre;
    private String raza;
    private String tamano;
    private Date fechaNacimiento;
    private String sexo;
    private String direccionCarnetVacunas;
    private String observaciones;

    public Perro(String nombre, String raza, String tamano, Date fechaNacimiento, String sexo, String observaciones) {
        this.nombre = nombre;
        this.raza = raza;
        this.tamano = tamano;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
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

    public String getTamano() {
        return tamano;
    }

    public void setTamano(String tamano) {
        this.tamano = tamano;
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

    public String getDireccionFoto() {
        return direccionFoto;
    }

    public void setDireccionFoto(String direccionFoto) {
        this.direccionFoto = direccionFoto;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
}
