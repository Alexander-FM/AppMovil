package com.example.comisariaapp.entity;

public class GridSeccion {

    private int id;
    private int id_imagen;
    private String txtBoton;

    public GridSeccion() {
    }

    public GridSeccion(int id, int id_imagen, String txtBoton) {
        this.id = id;
        this.id_imagen = id_imagen;
        this.txtBoton = txtBoton;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_imagen() {
        return id_imagen;
    }

    public void setId_imagen(int id_imagen) {
        this.id_imagen = id_imagen;
    }

    public String getTxtBoton() {
        return txtBoton;
    }

    public void setTxtBoton(String txtBoton) {
        this.txtBoton = txtBoton;
    }
}
