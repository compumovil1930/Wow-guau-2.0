package co.edu.javeriana.wow_guau.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Paseo {

    private long costo;
    private long duracion;
    private boolean estado;
    private String nomPerro;
    private String uidPaseador;
    private String uidPerro;
    private String uriPerro;
    Map<String, Object> direccion;
    private String uidDueno;
    private Date fecha;
    private long distanciaRecorrida;

    private boolean aceptado;
    private double calificacion;
    private boolean calificado;
    private String comentarioCalificacion;

    public Paseo() {
    }

    public long getDistanciaRecorrida() {
        return distanciaRecorrida;
    }

    public void setDistanciaRecorrida(long distanciaRecorrida) {
        this.distanciaRecorrida = distanciaRecorrida;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean isAceptado() {
        return aceptado;
    }

    public void setAceptado(boolean aceptado) {
        this.aceptado = aceptado;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }

    public boolean isCalificado() {
        return calificado;
    }

    public void setCalificado(boolean calificado) {
        this.calificado = calificado;
    }

    public String getComentarioCalificacion() {
        return comentarioCalificacion;
    }

    public void setComentarioCalificacion(String comentarioCalificacion) {
        this.comentarioCalificacion = comentarioCalificacion;
    }

    public String getUidDueno() {
        return uidDueno;
    }

    public void setUidDueno(String uidDueno) {
        this.uidDueno = uidDueno;
    }

    public long getCosto() {
        return costo;
    }

    public void setCosto(long costo) {
        this.costo = costo;
    }

    public long getDuracion() {
        return duracion;
    }

    public void setDuracion(long duracion) {
        this.duracion = duracion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getNomPerro() {
        return nomPerro;
    }

    public void setNomPerro(String nomPerro) {
        this.nomPerro = nomPerro;
    }

    public String getUidPaseador() {
        return uidPaseador;
    }

    public void setUidPaseador(String uidPaseador) {
        this.uidPaseador = uidPaseador;
    }

    public String getUidPerro() {
        return uidPerro;
    }

    public void setUidPerro(String uidPerro) {
        this.uidPerro = uidPerro;
    }

    public String getUriPerro() {
        return uriPerro;
    }

    public void setUriPerro(String uriPerro) {
        this.uriPerro = uriPerro;
    }

    public Map<String, Object> getDireccion() {
        return direccion;
    }

    public void setDireccion(Map<String, Object> direccion) {
        this.direccion = direccion;
    }
}
