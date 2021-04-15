package com.example.comisariaapp.entity.service.dto;

import com.example.comisariaapp.entity.service.Agraviado;
import com.example.comisariaapp.entity.service.Denuncia;
import com.example.comisariaapp.entity.service.Denunciado;

import java.util.ArrayList;
import java.util.List;

public class DenunciaConDetallesDTO {
    private Denuncia denuncia;
    private List<Agraviado> agraviados;
    private List<Denunciado>denunciados;

    public DenunciaConDetallesDTO() {
        this.denuncia=new Denuncia();
        this.agraviados=new ArrayList<>();
        this.denunciados=new ArrayList<>();
    }

    public Denuncia getDenuncia() {
        return denuncia;
    }

    public void setDenuncia(Denuncia denuncia) {
        this.denuncia = denuncia;
    }

    public List<Agraviado> getAgraviados() {
        return agraviados;
    }

    public void setAgraviados(List<Agraviado> agraviados) {
        this.agraviados = agraviados;
    }

    public List<Denunciado> getDenunciados() {
        return denunciados;
    }

    public void setDenunciados(List<Denunciado> denunciados) {
        this.denunciados = denunciados;
    }
}
