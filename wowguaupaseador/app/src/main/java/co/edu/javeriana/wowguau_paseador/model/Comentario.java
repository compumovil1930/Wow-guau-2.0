package co.edu.javeriana.wowguau_paseador.model;

public class Comentario {

    private String comentario;
    private float calificacion;
    private String nomDueno;

    public Comentario(String comentario, float calificacion, String nomDueno) {
        this.comentario = comentario;
        this.calificacion = calificacion;
        this.nomDueno = nomDueno;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public float getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(float calificacion) {
        this.calificacion = calificacion;
    }

    public String getNomDueno() {
        return nomDueno;
    }

    public void setNomDueno(String nomDueno) {
        this.nomDueno = nomDueno;
    }
}
