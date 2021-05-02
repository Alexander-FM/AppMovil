package com.example.comisariaapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.example.comisariaapp.entity.service.Agraviado;
import com.example.comisariaapp.entity.service.Denuncia;
import com.example.comisariaapp.entity.service.Denunciado;
import com.example.comisariaapp.entity.service.dto.DenunciaConDetallesDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Date;
import java.sql.Time;
import java.util.Iterator;
import java.util.List;

public class DenunciaManager {
    private static final Gson g = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateSerializer())
            .registerTypeAdapter(Time.class, new TimeSerializer())
            .create();
    private static SharedPreferences sp;

    public static void setDenuncia(Denuncia d, Context c) {
        DenunciaConDetallesDTO dto;
        String data = getObject(c);
        dto = data == null ? new DenunciaConDetallesDTO() : g.fromJson(data, DenunciaConDetallesDTO.class);
        dto.setDenuncia(d);
        save(dto);
    }

    public static String addAgraviado(final Agraviado a, Context c) {
        DenunciaConDetallesDTO dto;
        String data = getObject(c);
        dto = (data == null ? new DenunciaConDetallesDTO() : g.fromJson(data, DenunciaConDetallesDTO.class));

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
        save(dto);
        return response;
    }

    public static String removeAgraviado(final String numeroIdentificacion, final Context c) {
        DenunciaConDetallesDTO dto;
        final String data = getObject(c);
        dto = (data == null ? new DenunciaConDetallesDTO() : g.fromJson(data, DenunciaConDetallesDTO.class));
        boolean delete = false;
        final List<Agraviado> agraviados = dto.getAgraviados();
        final Iterator<Agraviado> i = agraviados.iterator();
        while (i.hasNext()) {
            Agraviado a = i.next();
            if (a.getNumeroIdentificacion().equals(numeroIdentificacion)) {
                i.remove();
                delete = true;
                break;
            }

        }
        dto.setAgraviados(agraviados);
        save(dto);
        return delete ? "Agraviado Eliminado Correctamente" : "Parece que el agraviado que desea eliminar ya no existe,por favor refresque esta ventana";
    }

    public static String removeDenunciado(final String numeroIdentificacion, final Context c) {
        DenunciaConDetallesDTO dto;
        final String data = getObject(c);
        boolean delete = false;
        dto = (data == null ? new DenunciaConDetallesDTO() : g.fromJson(data, DenunciaConDetallesDTO.class));

        String response = "";
        final List<Denunciado> denunciados = dto.getDenunciados();
        final Iterator<Denunciado> i = denunciados.iterator();
        while (i.hasNext()) {
            Denunciado d = i.next();
            if (d.getNumeroIdentificacion().equals(numeroIdentificacion)) {
                i.remove();
                delete = true;
                break;
            }

        }
        dto.setDenunciados(denunciados);
        save(dto);
        return delete ? "Denunciado Eliminado Correctamente" : "Parece que el denunciado que desea eliminar ya no existe,por favor refresque esta ventana";
    }

    public static String addDenunciado(final Denunciado d, Context c) {
        DenunciaConDetallesDTO dto;
        String data = getObject(c);
        dto = (data == null ? new DenunciaConDetallesDTO() : g.fromJson(data, DenunciaConDetallesDTO.class));
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
        save(dto);
        return response;
    }

    public static DenunciaConDetallesDTO getDto(Context c) {
        DenunciaConDetallesDTO dto = getObject(c) != null ? g.fromJson(getObject(c), DenunciaConDetallesDTO.class) : new DenunciaConDetallesDTO();
        return dto;
    }


    private static String getObject(Context c) {
        sp = PreferenceManager.getDefaultSharedPreferences(c);
        return sp.getString("dto", null);
    }

    private static void save(DenunciaConDetallesDTO dto) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("dto", g.toJson(dto));
        editor.apply();
    }

    public static void clear(Context c) {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("dto");
        editor.apply();
    }
}

