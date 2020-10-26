package com.introverted;

public class SwipeCardInfo {
   private int id;
   private String nombre;
   private String apellido;
   private String username;
   private String bday;
   private Double longitude;
   private Double latitude;
   private int generoId;
   private String sexo;

    public SwipeCardInfo(int id, String nombre, String apellido, String username,
                         String bday, Double longitude, Double latitude, int generoId, String sexo) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.username = username;
        this.bday = bday;
        this.longitude = longitude;
        this.latitude = latitude;
        this.generoId = generoId;
        this.sexo = sexo;
    }

    public String getSexo() {
        return sexo;
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

    public String getBday() {
        return bday;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }
}
