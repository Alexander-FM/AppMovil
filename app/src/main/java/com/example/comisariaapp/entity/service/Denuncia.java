package com.example.comisariaapp.entity.service;

import java.sql.Date;
import java.sql.Time;

public final class Denuncia {

    private int id;
    private Date fechaDenuncia;
    private Time horaDenuncia;
    private String cod_denuncia;
    private String direccion;
    private String referenciaDireccion;
    private Date fechaHechos;
    private Time horaHechos;
    private boolean estadoDenuncia;
    private TipoDenuncia tipoDenuncia;
    private Distrito distrito;
    private VinculoParteDenunciada vinculoParteDenunciada;
    private Policia policia;
    private Usuario usuario;
    private Comisarias comisarias;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFechaDenuncia() {
        return fechaDenuncia;
    }

    public void setFechaDenuncia(Date fechaDenuncia) {
        this.fechaDenuncia = fechaDenuncia;
    }

    public Time getHoraDenuncia() {
        return horaDenuncia;
    }

    public void setHoraDenuncia(Time horaDenuncia) {
        this.horaDenuncia = horaDenuncia;
    }

    public String getCod_denuncia() {
        return cod_denuncia;
    }

    public void setCod_denuncia(String cod_denuncia) {
        this.cod_denuncia = cod_denuncia;
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

    public Time getHoraHechos() {
        return horaHechos;
    }

    public void setHoraHechos(Time horaHechos) {
        this.horaHechos = horaHechos;
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

    public VinculoParteDenunciada getVinculoParteDenunciada() {
        return vinculoParteDenunciada;
    }

    public void setVinculoParteDenunciada(VinculoParteDenunciada vinculoParteDenunciada) {
        this.vinculoParteDenunciada = vinculoParteDenunciada;
    }

    public Policia getPolicia() {
        return policia;
    }

    public void setPolicia(Policia policia) {
        this.policia = policia;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Comisarias getComisarias() {
        return comisarias;
    }

    public void setComisarias(Comisarias comisarias) {
        this.comisarias = comisarias;
    }
}
