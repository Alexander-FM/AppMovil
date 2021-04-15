package com.example.comisariaapp.utils;

import com.example.comisariaapp.entity.service.Agraviado;
import com.example.comisariaapp.entity.service.Denuncia;
import com.example.comisariaapp.entity.service.Denunciado;
import com.example.comisariaapp.entity.service.dto.DenunciaConDetallesDTO;

import java.util.List;

public class DenunciaManager {
    private static final DenunciaConDetallesDTO dto = new DenunciaConDetallesDTO();

    public static void setDenuncia(Denuncia d) {
        dto.setDenuncia(d);
    }

    public static String addAgraviado(final Agraviado a) {
        String response = "";
        final List<Agraviado> agraviados = dto.getAgraviados();
        boolean act = false;
        if (!agraviados.isEmpty()) {
            int i = 0;
            for (Agraviado ag : agraviados) {
                if (ag.getNumeroIdentificacion().equals(a.getNumeroIdentificacion())) {
                    i = agraviados.indexOf(ag);
                    act = true;
                    break;
                }
                i++;
            }
            if (act) {
                agraviados.set(i, a);
                response = "Se ha encontrado un agraviado con el mismo documento de identidad,los datos se sobrescribirán ⚠⚠⚠";
            } else {
                agraviados.add(a);
                response = "Agraviado guardado Correctamente ✔✔✔";
            }
        } else {
            agraviados.add(a);
            response = "Agraviado guardado Correctamente ✔✔✔";
        }
        dto.setAgraviados(agraviados);
        return response;
    }
    public static String addDenunciado(final Denunciado d) {
        String response = "";
        final List<Denunciado> denunciados = dto.getDenunciados();
        boolean act = false;
        if (!denunciados.isEmpty()) {
            int i = 0;
            for (Denunciado de : denunciados) {
                if (de.getNumeroIdentificacion().equals(d.getNumeroIdentificacion())) {
                    i = denunciados.indexOf(de);
                    act = true;
                    break;
                }
                i++;
            }
            if (act) {
                denunciados.set(i, d);
                response = "Se ha encontrado un denunciado con el mismo documento de identidad,los datos se sobrescribirán ⚠⚠⚠";
            } else {
                denunciados.add(d);
                response = "Denunciado guardado Correctamente ✔✔✔";
            }
        } else {
            denunciados.add(d);
            response = "Denunciado guardado Correctamente ✔✔✔";
        }
        dto.setDenunciados(denunciados);
        return response;
    }
    public static DenunciaConDetallesDTO getDto(){
        return dto;
    }
}
