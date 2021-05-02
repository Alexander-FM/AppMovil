package com.example.comisariaapp.entity.service;

import java.sql.Date;
import java.sql.Time;

public final class Tramite {
    private int id;
    private String codTramite;
    private Date fechaTramite;
    private Time horaTramite;
    private TipoTramite tipoTramite;
    private boolean estadoTramite;
    private String correo;
    private Usuario usuario;
    private Policia policia; /*Muchos Trámites puede atender un solo policía*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodTramite() {
        return codTramite;
    }

    public void setCodTramite(String codTramite) {
        this.codTramite = codTramite;
    }

    public Date getFechaTramite() {
        return fechaTramite;
    }

    public void setFechaTramite(Date fechaTramite) {
        this.fechaTramite = fechaTramite;
    }

    public Time getHoraTramite() {
        return horaTramite;
    }

    public void setHoraTramite(Time horaTramite) {
        this.horaTramite = horaTramite;
    }

    public TipoTramite getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public boolean isEstadoTramite() {
        return estadoTramite;
    }

    public void setEstadoTramite(boolean estadoTramite) {
        this.estadoTramite = estadoTramite;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Policia getPolicia() {
        return policia;
    }

    public void setPolicia(Policia policia) {
        this.policia = policia;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
