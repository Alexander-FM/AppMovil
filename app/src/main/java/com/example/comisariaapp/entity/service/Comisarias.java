package com.example.comisariaapp.entity.service;

public class Comisarias {
    private int id;
    private String nombreComisaria;
    private String direccionComisaria;
    private String telefonoComisaria;
    private Distrito distrito;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreComisaria() {
        return nombreComisaria;
    }

    public void setNombreComisaria(String nombreComisaria) {
        this.nombreComisaria = nombreComisaria;
    }

    public String getDireccionComisaria() {
        return direccionComisaria;
    }

    public void setDireccionComisaria(String direccionComisaria) {
        this.direccionComisaria = direccionComisaria;
    }

    public String getTelefonoComisaria() {
        return telefonoComisaria;
    }

    public void setTelefonoComisaria(String telefonoComisaria) {
        this.telefonoComisaria = telefonoComisaria;
    }

    public Distrito getDistrito() {
        return distrito;
    }

    public void setDistrito(Distrito distrito) {
        this.distrito = distrito;
    }
}
