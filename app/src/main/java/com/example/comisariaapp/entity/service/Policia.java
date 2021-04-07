package com.example.comisariaapp.entity.service;

public class Policia {
    private int id;
    /*-------------------------------------------------*/
    private String nombres;
    /*-------------------------------------------------*/
    private String apellidos;
    /*---------------------------------------------------*/
    private String sexo;
    /*----------------------------------------------------*/
    private String telefono;
    /*-----------------------------------------------------*/
    private boolean estado;
    /*------------------------------------------------------*/
    private Distrito distrito;
    /*-------------------------------------------------------*/
    private LoginPNP loginPNP;
    /*-------------------------------------------------------*/
    private GradoPNP gradoPNP;
    /*--------------------------------------------------------*/
    private TipoIdentificacion tipoIdentificacion;
    /*---------------------------------------------------------*/
    private String numeroIdentificacion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Distrito getDistrito() {
        return distrito;
    }

    public void setDistrito(Distrito distrito) {
        this.distrito = distrito;
    }

    public LoginPNP getLoginPNP() {
        return loginPNP;
    }

    public void setLoginPNP(LoginPNP loginPNP) {
        this.loginPNP = loginPNP;
    }

    public GradoPNP getGradoPNP() {
        return gradoPNP;
    }

    public void setGradoPNP(GradoPNP gradoPNP) {
        this.gradoPNP = gradoPNP;
    }

    public TipoIdentificacion getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(TipoIdentificacion tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }
}
