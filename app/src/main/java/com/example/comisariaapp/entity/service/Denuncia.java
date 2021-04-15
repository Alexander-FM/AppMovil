package com.example.comisariaapp.entity.service;

import java.util.Date;
public final class Denuncia {
    private int id;
    private Date fechaDenuncia;
    private String cod_denuncia;
    private String direccion;
    private String referenciaDireccion;
    private Date fechaHechos;
    private boolean estadoDenuncia;
    private TipoDenuncia tipoDenuncia;
    private Distrito distrito;
    private VinculoParteDenunciada vinculoParteDenunciada;
    private Policia policia;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Policia getPolicia() {
        return policia;
    }

    public void setPolicia(Policia policia) {
        this.policia = policia;
    }

    public Date getFechaDenuncia() {
        return fechaDenuncia;
    }

    public void setFechaDenuncia(Date fechaDenuncia) {
        this.fechaDenuncia = fechaDenuncia;
    }


    public boolean isEstadoDenuncia() {
        return estadoDenuncia;
    }

    public void setEstadoDenuncia(boolean estadoDenuncia) {
        this.estadoDenuncia = estadoDenuncia;
    }

    public TipoDenuncia getTipoDenuncia() {
        return tipoDenuncia;
    }

    public void setTipoDenuncia(TipoDenuncia tipoDenuncia) {
        this.tipoDenuncia = tipoDenuncia;
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

    public String getReferenciaDireccion() {
        return referenciaDireccion;
    }

    public void setReferenciaDireccion(String referenciaDireccion) {
        this.referenciaDireccion = referenciaDireccion;
    }

    public Date getFechaHechos() {
        return fechaHechos;
    }

    public void setFechaHechos(Date fechaHechos) {
        this.fechaHechos = fechaHechos;
    }

    public VinculoParteDenunciada getVinculoParteDenunciada() {
        return vinculoParteDenunciada;
    }

    public void setVinculoParteDenunciada(VinculoParteDenunciada vinculoParteDenunciada) {
        this.vinculoParteDenunciada = vinculoParteDenunciada;
    }

    public String getCod_denuncia() {
        return cod_denuncia;
    }

    public void setCod_denuncia(String cod_denuncia) {
        this.cod_denuncia = cod_denuncia;
    }

    public String getNombreCompletoPolicia() {
        /*this.nombreCompletoPolicia = policia != null ? this.policia.getNombres() + " " + this.policia.getApellidos() : " - - - ";
        return nombreCompletoPolicia;*/
        return this.policia != null ? this.policia.getNombres() + " " + this.policia.getApellidoPaterno()+" "+this.policia.getApellidoMaterno() : " - - - ";
    }

    public String getNombreDistrito() {
        return this.distrito!=null?this.distrito.getDistrito():"- - - ";
    }
    public String getNombreTipoDenuncia(){
        return this.tipoDenuncia!=null?this.tipoDenuncia.getTipoDenuncia():"- - - ";
    }
    public String getNombreVPD(){
        return this.vinculoParteDenunciada!=null?this.vinculoParteDenunciada.getNombre():"- - - ";
    }
}
