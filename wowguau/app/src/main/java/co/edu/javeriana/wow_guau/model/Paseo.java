package co.edu.javeriana.wow_guau.model;

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

    public Paseo() {
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
