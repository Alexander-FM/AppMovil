package com.example.comisariaapp.entity;

import com.example.comisariaapp.entity.service.Agraviado;
import com.example.comisariaapp.entity.service.Denuncia;
import com.example.comisariaapp.entity.service.Denunciado;
import com.example.comisariaapp.entity.service.DetalleDenuncia;

import java.util.List;

public class DenunciaManager {
    private static Denuncia den;
    private static Agraviado a = null;
    private static Denunciado d = null;
    private static List<DetalleDenuncia> detalles;

    public static void set(Denuncia d) {
        String rpta = "datos de la denuncia " + d == null ? "registrados" : "actualizados";
        if (den == null) {

        }
        den = d;
    }

    public static String addAgraviado(Agraviado ag) {
        String rpta = "agraviado " + a == null ? "registrado" : "actualizado";
        a = ag;
        return rpta;
    }

    public static String addDenunciado(Denunciado de) {
        String rpta = "denunciado " + d == null ? "registrado" : "actualizado";
        d = de;
        return rpta;
    }

    public static void deleteAgraviado() {
        a = null;
    }

    public static void deleteDenunciado() {
        d = null;
    }
}
