package co.edu.javeriana.wow_guau.adapters;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.Serializable;

public class PaseadorClassAdapter implements Serializable {

    String nombre;
    String uidPaseador;
    LatLng localizacion;
    LatLng usuarioLocalizacion;
    Double dist;
    String uriPhoto;

    File mImage;

    public PaseadorClassAdapter(String nombre, String uidPaseador, LatLng localizacion, String uriPhoto) {
        this.nombre = nombre;
        this.uidPaseador = uidPaseador;
        this.localizacion = localizacion;
        this.uriPhoto = uriPhoto;
        dist = -1.0;
    }

    public LatLng getUsuarioLocalizacion() {
        return usuarioLocalizacion;
    }

    public void setUsuarioLocalizacion(LatLng usuarioLocalizacion) {
        this.usuarioLocalizacion = usuarioLocalizacion;
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

    public LatLng getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(LatLng localizacion) {
        this.localizacion = localizacion;
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

    public void calcDist(){
        if(this.getUsuarioLocalizacion() == null || this.localizacion==null )
            return;
        double mydist = distance(this.localizacion.latitude,this.localizacion.longitude,this.usuarioLocalizacion.latitude, this.usuarioLocalizacion.longitude);
        this.dist = mydist;
    }

    public double distance(double lat1, double long1, double lat2, double long2) {
        final int RADIUS_OF_EARTH_KM = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat1 - lat2);
        double lngDistance = Math.toRadians(long1 - long2);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double result = RADIUS_OF_EARTH_KM * c;
        return Math.round(result*100.0)/100.0;
    }

    @Override
    public String toString() {
        return "PaseadorClassAdapter{" +
                "nombre='" + nombre + '\'' +
                ", uidPaseador='" + uidPaseador + '\'' +
                ", localizacion=" + localizacion +
                ", usuarioLocalizacion=" + usuarioLocalizacion +
                ", dist=" + dist +
                ", uriPhoto='" + uriPhoto + '\'' +
                '}';
    }
}
