package com.example.comisariaapp.entity;

import com.example.comisariaapp.entity.service.Denuncia;
import com.example.comisariaapp.entity.service.DetalleDenuncia;

import java.util.List;

public class DenunciaWitchDetallesDTO {
    private Denuncia denuncia;
    private List<DetalleDenuncia> detalles;

    public Denuncia getDenuncia() {
        return denuncia;
    }

    public void setDenuncia(Denuncia denuncia) {
        this.denuncia = denuncia;
    }

    public List<DetalleDenuncia> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleDenuncia> detalles) {
        this.detalles = detalles;
    }
}
