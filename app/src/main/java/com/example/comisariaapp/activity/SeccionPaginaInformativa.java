package com.example.comisariaapp.activity;

public class SeccionPaginaInformativa {
    private int id;
    private String titulo;
    private int idImagen;
    private String color;
    private String background;

    public SeccionPaginaInformativa() {
    }

    public SeccionPaginaInformativa(int id, String titulo, int idImagen, String color, String background) {
        this.id = id;
        this.titulo = titulo;
        this.idImagen = idImagen;
        this.color = color;
        this.background = background;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(int idImagen) {
        this.idImagen = idImagen;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }
}
