package com.example.comisariaapp.dialog;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comisariaapp.R;
import com.example.comisariaapp.adapter.AgraviadoAdapter;
import com.example.comisariaapp.adapter.DenunciadoAdapter;
import com.example.comisariaapp.adapter.DetalleMiDenunciaAdapter;
import com.example.comisariaapp.entity.service.Agraviado;
import com.example.comisariaapp.entity.service.Denunciado;
import com.example.comisariaapp.entity.service.Persona;
import com.example.comisariaapp.utils.DateSerializer;
import com.example.comisariaapp.utils.TimeSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class DetalleDenunciaDialog extends DialogFragment {
    private final Gson g = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateSerializer())
            .registerTypeAdapter(Time.class, new TimeSerializer())
            .create();
    private List<Agraviado> agraviados = new ArrayList<>();
    private List<Denunciado> denunciados = new ArrayList<>();
    private List<Persona> pAgraviado = new ArrayList<>(), pDenunciado = new ArrayList<>();
    private RecyclerView rcvAgraviados, rcvDenunciados;
    private DetalleMiDenunciaAdapter aDmdAdapter, dDmdAdapter;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle data) {
        super.onCreate(data);
        String strA, strD;
        strA = this.getArguments().getString("agraviados", null);
        strD = this.getArguments().getString("denunciados", null);
        if (strA != null && strD != null) {
            this.agraviados = this.g.fromJson(strA, new TypeToken<List<Agraviado>>() {
            }.getType());
            for (Agraviado a : this.agraviados) {
                this.pAgraviado.add(a);
            }
            this.denunciados = this.g.fromJson(strD, new TypeToken<List<Denunciado>>() {
            }.getType());
            for (Denunciado d : this.denunciados) {
                this.pDenunciado.add(d);
            }
        }

    }

    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_detalle_denuncia, container, false);
        this.init(v);
        this.initAdapters();
        this.loadData();
        getDialog().getWindow().setLayout(800, 800);
        getDialog().getWindow().setGravity(Gravity.CENTER);
        return v;
    }

    private void init(View v) {
        this.rcvAgraviados = v.findViewById(R.id.rcvAgraviados);
        this.rcvDenunciados = v.findViewById(R.id.rcvDenunciados);
        final Button btnAceptar = v.findViewById(R.id.btnAceptar);
        btnAceptar.setOnClickListener(v1 -> this.dismiss());
    }

    private void initAdapters() {
        this.aDmdAdapter = new DetalleMiDenunciaAdapter(new ArrayList<>());
        this.rcvAgraviados.setAdapter(aDmdAdapter);
        this.dDmdAdapter = new DetalleMiDenunciaAdapter(new ArrayList<>());
        this.rcvDenunciados.setAdapter(dDmdAdapter);
    }

    private void loadData() {
        List<Persona> pAgraviados = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            agraviados.forEach(a -> pAgraviados.add(a));
        } else {
            for (Agraviado a : agraviados) {
                pAgraviados.add(a);
            }
        }
        List<Persona> pDenunciados = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            denunciados.forEach(d -> pDenunciados.add(d));
        } else {
            for (Denunciado d : denunciados) {
                pDenunciados.add(d);
            }
        }
        aDmdAdapter.updateItems(this.pAgraviado);
        dDmdAdapter.updateItems(this.pDenunciado);
        String str;
    }
}
