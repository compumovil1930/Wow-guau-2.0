package co.edu.javeriana.wowguau_paseador.model;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

public class Paseo implements Serializable {
    String uidPerro;
    String uidPaseador;
    String uidDueno;
    long duracionMinutos;
    long costo;
    String direccion;
    Double latitude;
    Double longitude;
    Boolean estado;
    String nomPerro;
    String nomPaseador;
    String uriPerrito;
    double dist;
    LatLng paseadorLoc;
    File myImage;

    public Paseo() {
    }

    public Paseo(String uidPerro, String uidPaseador, String uidDueno, long duracionMinutos, long costo, String direccion,
                 Double latitude, Double longitude, Boolean estado, String nomPerro, String uriPerrito){
        this.uriPerrito = uriPerrito;
        this.nomPerro = nomPerro;
        this.uidPerro = uidPerro;
        this.uidPaseador = uidPaseador;
        this.uidDueno = uidDueno;
        this.duracionMinutos = duracionMinutos;
        this.costo = costo;
        this.direccion = direccion;
        this.latitude = latitude;
        this.longitude = longitude;
        this.estado = estado;
        this.dist = -1;
    }

    public double getDist() {

        return dist;
    }

    public String getUidPerro() {
        return uidPerro;
    }

    public void setUidPerro(String uidPerro) {
        this.uidPerro = uidPerro;
    }

    public String getUidPaseador() {
        return uidPaseador;
    }

    public void setUidPaseador(String uidPaseador) {
        this.uidPaseador = uidPaseador;
    }

    public String getUidDueno() {
        return uidDueno;
    }

    public void setUidDueno(String uidDueno) {
        this.uidDueno = uidDueno;
    }

    public long getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(long duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public long getCosto() {
        return costo;
    }

    public void setCosto(long costo) {
        this.costo = costo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getNomPerro() {
        return nomPerro;
    }

    public void setNomPerro(String nomPerro) {
        this.nomPerro = nomPerro;
    }

    public String getNomPaseador() {
        return nomPaseador;
    }

    public void setNomPaseador(String nomPaseador) {
        this.nomPaseador = nomPaseador;
    }

    public String getUriPerrito() {
        return uriPerrito;
    }

    public void setUriPerrito(String uriPerrito) {
        this.uriPerrito = uriPerrito;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }

    public LatLng getPaseadorLoc() {
        return paseadorLoc;
    }

    public void setPaseadorLoc(LatLng paseadorLoc) {
        this.paseadorLoc = paseadorLoc;
    }

    public File getMyImage() {
        return myImage;
    }

    public void setMyImage(File myImage) {
        this.myImage = myImage;
    }

    public void calcDist(){
        if(this.getPaseadorLoc() == null)
            return;
        double mydist = distance(this.getLatitude(),this.getLongitude(),this.getPaseadorLoc().latitude, this.getPaseadorLoc().longitude);
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

    /*
    @Override
    public int compareTo(Paseo o) {
        return (int)(this.dist - o.dist);
    }

     */
}
