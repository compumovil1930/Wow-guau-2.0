package co.edu.javeriana.wow_guau.model;

import java.util.Date;

public class Mensaje {
    private String id;
    private String text;
    private String imageUrl;
    private String senderId;
    private Date time;

    public Mensaje() {
    }

    public Mensaje(String text, String imageUrl, String senderId) {
        this.text = text;
        this.imageUrl = imageUrl;
        this.senderId = senderId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

}
