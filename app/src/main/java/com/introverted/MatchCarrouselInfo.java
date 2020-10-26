package com.introverted;

public class MatchCarrouselInfo {
    private int id;
    private String username;
    private String nombre;
    private String apellido;
    private String saveLocation;

    public MatchCarrouselInfo(int id, String username, String nombre, String apellido, String saveLocation) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.username = username;
        this.saveLocation = saveLocation;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getUsername() {
        return username;
    }

    public String getSaveLocation() {
        return saveLocation;
    }
}
