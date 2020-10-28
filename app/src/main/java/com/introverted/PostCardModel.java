package com.introverted;

import android.graphics.Bitmap;

public class PostCardModel {
    private int postId, id, likes, dislikes, comments;
    private Bitmap image, userImage;
    private String username, titulo, publicacion, subject, time;

    public PostCardModel(int postId, int id, int likes, int dislikes, String username,
                         Bitmap image, String onlyText, String titulo, String publicacion,
                         String subject,String time, int comments, Bitmap userImage) {
        this.postId = postId;
        this.id = id;
        this.likes = likes;
        this.dislikes = dislikes;
        this.username = username;
        if(onlyText.equals("no"))
            this.image = image;
        else
            this.image = null;
        this.titulo = titulo;
        this.publicacion = publicacion;
        this.subject = subject;
        this.time = time;
        this.comments = comments;
        this.userImage = userImage;
    }

    public Bitmap getUserImage() {
        return userImage;
    }

    public void setUserImage(Bitmap userImage) {
        this.userImage = userImage;
    }

    public int getComments() {
        return comments;
    }

    public String getUsername() {
        return username;
    }

    public String getTime() {
        return time;
    }

    public int getPostId() {
        return postId;
    }

    public int getId() {
        return id;
    }

    public int getLikes() {
        return likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getPublicacion() {
        return publicacion;
    }

    public String getSubject() {
        return subject;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}