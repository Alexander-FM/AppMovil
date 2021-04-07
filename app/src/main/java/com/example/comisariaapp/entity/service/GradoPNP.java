package com.example.comisariaapp.entity.service;

public class GradoPNP {
    private int id;
    /*---------------------------------*/
    private String nombreGrado;
    /*---------------------------------*/
    private boolean vigencia;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreGrado() {
        return nombreGrado;
    }

    public void setNombreGrado(String nombreGrado) {
        this.nombreGrado = nombreGrado;
    }

    public boolean isVigencia() {
        return vigencia;
    }

    public void setVigencia(boolean vigencia) {
        this.vigencia = vigencia;
    }
}
