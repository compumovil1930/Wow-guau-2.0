package co.edu.javeriana.wowguau_paseador.model;

public class Paseo {
    String uidPerro;
    String uidPaseador;
    long duracionMinutos;
    long costo;
    String direccion;
    Double latitude;
    Double longitude;
    Boolean estado;
    String nomPerro;
    String nomPaseador;
    String uriPerrito;

    public Paseo(String uidPerro, String uidPaseador, long duracionMinutos, long costo, String direccion,
                 Double latitude, Double longitude, Boolean estado, String nomPerro, String uriPerrito){
        this.uriPerrito = uriPerrito;
        this.nomPerro = nomPerro;
        this.uidPerro = uidPerro;
        this.uidPaseador = uidPaseador;
        this.duracionMinutos = duracionMinutos;
        this.costo = costo;
        this.direccion = direccion;
        this.latitude = latitude;
        this.longitude = longitude;
        this.estado = estado;
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

    public String getUriPerrito() {
        return uriPerrito;
    }

    public void setUriPerrito(String uriPerrito) {
        this.uriPerrito = uriPerrito;
    }

    public void setUidPaseador(String uidPaseador) {
        this.uidPaseador = uidPaseador;
    }

    public long getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public long getCosto() {
        return costo;
    }

    public void setCosto(long costo) {
        this.costo = costo;
    }

    public String getNomPaseador() {
        return nomPaseador;
    }

    public void setNomPaseador(String nomPaseador) {
        this.nomPaseador = nomPaseador;
    }

    public String getNomPerro() {
        return nomPerro;
    }

    public void setNomPerro(String nomPerro) {
        this.nomPerro = nomPerro;
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
}
