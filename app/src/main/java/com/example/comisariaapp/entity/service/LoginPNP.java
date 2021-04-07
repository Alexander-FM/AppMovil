package com.example.comisariaapp.entity.service;

public class LoginPNP {
    private int id;
    /*------------------------------------------------*/
    private String codigoPolicial;
    /*-------------------------------------------------*/
    private String clave;
    /*-------------------------------------------------*/
    private boolean estado;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigoPolicial() {
        return codigoPolicial;
    }

    public void setCodigoPolicial(String codigoPolicial) {
        this.codigoPolicial = codigoPolicial;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
