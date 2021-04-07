package com.example.comisariaapp.entity.service;

import java.util.Date;

public class Agraviado {
    private int id;
    private String nombreAgraviado;
    private String apellidosAgraviado;
    private String sexo;
    private Date fechaNac;
    private String telefono;
    private String numeroDoc;
    private boolean medidaProteccion;
    private String detalleProteccion;
    private String juzgado;
    private Date fechaEmision;
    private String RHD;
    private TipoIdentificacion tipoIdentificacion;
    private Distrito distrito;
    private InformacionAdicional informacionAdicional;
    private String direccionAgraviado;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreAgraviado() {
        return nombreAgraviado;
    }

    public void setNombreAgraviado(String nombreAgraviado) {
        this.nombreAgraviado = nombreAgraviado;
    }

    public String getApellidosAgraviado() {
        return apellidosAgraviado;
    }

    public void setApellidosAgraviado(String apellidosAgraviado) {
        this.apellidosAgraviado = apellidosAgraviado;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Date getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNumeroDoc() {
        return numeroDoc;
    }

    public void setNumeroDoc(String numeroDoc) {
        this.numeroDoc = numeroDoc;
    }

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

    public String getRHD() {
        return RHD;
    }

    public void setRHD(String RHD) {
        this.RHD = RHD;
    }

    public TipoIdentificacion getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(TipoIdentificacion tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public Distrito getDistrito() {
        return distrito;
    }

    public void setDistrito(Distrito distrito) {
        this.distrito = distrito;
    }

    public InformacionAdicional getInformacionAdicional() {
        return informacionAdicional;
    }

    public void setInformacionAdicional(InformacionAdicional informacionAdicional) {
        this.informacionAdicional = informacionAdicional;
    }

    public String getDireccionAgraviado() {
        return direccionAgraviado;
    }

    public void setDireccionAgraviado(String direccionAgraviado) {
        this.direccionAgraviado = direccionAgraviado;
    }
}
