package com.example.comisariaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;

import com.example.comisariaapp.R;
import com.example.comisariaapp.adapter.DetalleMiDenunciaAdapter;
import com.example.comisariaapp.entity.service.Agraviado;
import com.example.comisariaapp.entity.service.Denunciado;
import com.example.comisariaapp.entity.service.Persona;
import com.example.comisariaapp.utils.DateSerializer;
import com.example.comisariaapp.utils.TimeSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class DetalleMisDenunciasActivity extends AppCompatActivity {
    private DetalleMiDenunciaAdapter aDmdAdapter, dDmdAdapter;
    private RecyclerView rcvAgraviados, rcvDenunciados;
    private final Gson g = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateSerializer())
            .registerTypeAdapter(Time.class, new TimeSerializer())
            .create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_mis_denuncias);
        this.init();
        this.initAdpaters();
        this.loadData();
    }

    private void init() {
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.atras);
        toolbar.setNavigationOnClickListener(v -> {//Reemplazo con lamba
            this.finish();
            this.overridePendingTransition(R.anim.down_in, R.anim.down_out);
        });
        this.rcvAgraviados = this.findViewById(R.id.rcvAgraviados);
        this.rcvDenunciados = this.findViewById(R.id.rcvDenunciados);
        this.rcvAgraviados.setLayoutManager(new GridLayoutManager(this, 1));
        this.rcvDenunciados.setLayoutManager(new GridLayoutManager(this, 1));
    }

    private void initAdpaters() {
        this.aDmdAdapter = new DetalleMiDenunciaAdapter(new ArrayList<>());
        this.rcvAgraviados.setAdapter(aDmdAdapter);
        this.dDmdAdapter = new DetalleMiDenunciaAdapter(new ArrayList<>());
        this.rcvDenunciados.setAdapter(dDmdAdapter);
    }

    private void loadData() {
        Bundle data = getIntent().getExtras();

        List<Agraviado> agraviados = g.fromJson(data.getString("agraviados"), new TypeToken<List<Agraviado>>() {
        }.getType());
        List<Persona> pAgraviados = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            agraviados.forEach(a -> pAgraviados.add(a));
        } else {
            for (Agraviado a : agraviados) {
                pAgraviados.add(a);
            }
        }

        List<Denunciado> denunciados = g.fromJson(data.getString("denunciados"), new TypeToken<List<Denunciado>>() {
        }.getType());
        List<Persona> pDenunciados = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            denunciados.forEach(d -> pDenunciados.add(d));
        } else {
            for (Denunciado d : denunciados) {
                pDenunciados.add(d);
            }
        }
        this.aDmdAdapter.updateItems(pAgraviados);
        this.dDmdAdapter.updateItems(pDenunciados);
    }
}