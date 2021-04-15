package com.example.comisariaapp.entity.service;

import java.util.Date;

public final class Agraviado extends Persona{
    private boolean medidaProteccion;
    private String detalleProteccion;
    private String Juzgado;
    private Date fechaEmision;
    private String RHD;
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
        return Juzgado;
    }

    public void setJuzgado(String juzgado) {
        Juzgado = juzgado;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getRHD() {
        return RHD;
    }

    public void setRHD(String RHD) {
        this.RHD = RHD;
    }

    public InformacionAdicional getInformacionAdicional() {
        return informacionAdicional;
    }

    public void setInformacionAdicional(InformacionAdicional informacionAdicional) {
        this.informacionAdicional = informacionAdicional;
    }
}
