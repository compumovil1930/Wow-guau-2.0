package co.edu.javeriana.wow_guau.adapters;

import java.io.File;
import java.io.Serializable;

public class PaseoClassAdapter implements Serializable {

    private String nomPerro;
    private boolean aceptado;
    private long costo;
    private long duracion;
    private boolean estado; // aceptado o finalizado
    private String uidDueno;
    private String uidPaseador;
    private String uidPerro;
    private String uriPerro; // foto del perrito
    private boolean calificado;
    private double calificacion;
    private String comentarioCalificacion;
    private String uidPaseo;

    File mImage;

    public PaseoClassAdapter(String nomPerro, boolean aceptado, long costo, long duracion, boolean estado, String uidDueno, String uidPaseador, String uidPerro, String uriPerro, boolean calificado, double calificacion, String comentarioCalificacion, String uidPaseo) {
        this.nomPerro = nomPerro;
        this.aceptado = aceptado;
        this.costo = costo;
        this.duracion = duracion;
        this.estado = estado;
        this.uidDueno = uidDueno;
        this.uidPaseador = uidPaseador;
        this.uidPerro = uidPerro;
        this.uriPerro = uriPerro;
        this.calificado = calificado;
        this.calificacion = calificacion;
        this.comentarioCalificacion = comentarioCalificacion;
        this.uidPaseo = uidPaseo;
    }

    public File getmImage() {
        return mImage;
    }

    public void setmImage(File mImage) {
        this.mImage = mImage;
    }

    public String getUidPaseo() {
        return uidPaseo;
    }

    public void setUidPaseo(String uidPaseo) {
        this.uidPaseo = uidPaseo;
    }

    public String getNomPerro() {
        return nomPerro;
    }

    public void setNomPerro(String nomPerro) {
        this.nomPerro = nomPerro;
    }

    public boolean isAceptado() {
        return aceptado;
    }

    public void setAceptado(boolean aceptado) {
        this.aceptado = aceptado;
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

    public String getUidDueno() {
        return uidDueno;
    }

    public void setUidDueno(String uidDueno) {
        this.uidDueno = uidDueno;
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

    public boolean isCalificado() {
        return calificado;
    }

    public void setCalificado(boolean calificado) {
        this.calificado = calificado;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentarioCalificacion() {
        return comentarioCalificacion;
    }

    public void setComentarioCalificacion(String comentarioCalificacion) {
        this.comentarioCalificacion = comentarioCalificacion;
    }
}
