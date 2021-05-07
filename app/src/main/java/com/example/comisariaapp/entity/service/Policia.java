package com.example.comisariaapp.entity.service;

import java.sql.Date;

public final class Policia extends Persona{
    private GradoPNP gradoPNP;
    private boolean estado;
    private Date fechaNacimiento;
    private String telefono;
    private EstadoCivil estadoCivil;
    private Distrito distrito;
    private String direccion;

    public GradoPNP getGradoPNP() {
        return gradoPNP;
    }

    public void setGradoPNP(GradoPNP gradoPNP) {
        this.gradoPNP = gradoPNP;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }


    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public EstadoCivil getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(EstadoCivil estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public Distrito getDistrito() {
        return distrito;
    }

    public void setDistrito(Distrito distrito) {
        this.distrito = distrito;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
