package com.example.comisariaapp.entity.service;

import java.sql.Date;

public final class Agraviado extends Persona {
    private boolean medidaProteccion;
    private String detalleProteccion;
    private String juzgado;
    private Date fechaEmision;
    private String rhd;
    private InformacionAdicional informacionAdicional;

    public boolean isMedidaProteccion() {
        return medidaProteccion;
    }

    public void setMedidaProteccion(boolean medidaProteccion) {
        this.medidaProteccion = medidaProteccion;
    }

    public String getDetalleProteccion() {
        return detalleProteccion;
    }

    public void setDetalleProteccion(String detalleProteccion) {
        this.detalleProteccion = detalleProteccion;
    }

    public String getJuzgado() {
        return juzgado;
    }

    public void setJuzgado(String juzgado) {
        this.juzgado = juzgado;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getRhd() {
        return rhd;
    }

    public void setRhd(String rhd) {
        this.rhd = rhd;
    }

    public InformacionAdicional getInformacionAdicional() {
        return informacionAdicional;
    }

    public void setInformacionAdicional(InformacionAdicional informacionAdicional) {
        this.informacionAdicional = informacionAdicional;
    }
}
