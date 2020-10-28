package com.introverted;

import com.android.volley.toolbox.StringRequest;

import java.sql.Timestamp;

public class PostCardInfo {
    private int postId, id, likes, dislikes, commentCounter;
    private String tiempo, titulo, publicacion, subject, saveLocation, username, profilePic;

    public PostCardInfo(int postId, int id, int likes, int dislikes, int commentCounter,
                        String tiempo, String titulo, String publicacion, String subject,
                        String saveLocation, String username, String profilePic) {
        this.postId = postId;
        this.id = id;
        this.likes = likes;
        this.dislikes = dislikes;
        this.commentCounter = commentCounter;
        this.tiempo = tiempo;
        this.titulo = titulo;
        this.publicacion = publicacion;
        this.subject = subject;
        this.saveLocation = saveLocation;
        this.username = username;
        this.profilePic = profilePic;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getUsername() {
        return username;
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

    public int getCommentCounter() {
        return commentCounter;
    }

    public String getTiempo() {
        return tiempo;
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

    public String getSaveLocation() {
        return saveLocation;
    }
}
