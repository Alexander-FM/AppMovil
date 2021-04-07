package com.example.comisariaapp.entity.service;

public class DetalleDenuncia {
    private int id;
    private Denuncia denuncia;
    private Agraviado agraviado;/*el agraviado puede ser la misma persona que denuncia.*/
    private Usuario usuario;/*Con la sesión del usuario podemos definir que el es el denunciante.*/
    private Denunciado denunciado;/*Toda persona que ingresa a realizar una denuncia debe conocer al denunciado.*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Denuncia getDenuncia() {
        return denuncia;
    }

    public void setDenuncia(Denuncia denuncia) {
        this.denuncia = denuncia;
    }

    public Agraviado getAgraviado() {
        return agraviado;
    }

    public void setAgraviado(Agraviado agraviado) {
        this.agraviado = agraviado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Denunciado getDenunciado() {
        return denunciado;
    }

    public void setDenunciado(Denunciado denunciado) {
        this.denunciado = denunciado;
    }
}
