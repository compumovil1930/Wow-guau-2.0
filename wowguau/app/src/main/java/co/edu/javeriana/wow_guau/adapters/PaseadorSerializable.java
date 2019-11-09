package co.edu.javeriana.wow_guau.adapters;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.Serializable;

public class PaseadorSerializable implements Serializable {

    String nombre;
    String uidPaseador;
    Double dist;
    String uriPhoto;

    File mImage;

    public PaseadorSerializable(String nombre, String uidPaseador, LatLng localizacion, String uriPhoto) {
        this.nombre = nombre;
        this.uidPaseador = uidPaseador;
        this.uriPhoto = uriPhoto;
        dist = -1.0;
    }

    public PaseadorSerializable(PaseadorClassAdapter p){
        this.nombre = p.getNombre();
        this.uidPaseador = p.getUidPaseador();
        this.dist = p.getDist();
        this.uriPhoto = p.getUriPhoto();
        this.mImage = p.getmImage();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUidPaseador() {
        return uidPaseador;
    }

    public void setUidPaseador(String uidPaseador) {
        this.uidPaseador = uidPaseador;
    }

    public Double getDist() {
        return dist;
    }

    public void setDist(Double dist) {
        this.dist = dist;
    }

    public String getUriPhoto() {
        return uriPhoto;
    }

    public void setUriPhoto(String uriPhoto) {
        this.uriPhoto = uriPhoto;
    }

    public File getmImage() {
        return mImage;
    }

    public void setmImage(File mImage) {
        this.mImage = mImage;
    }

}
