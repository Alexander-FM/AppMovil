package com.example.comisariaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.comisariaapp.R;
import com.example.comisariaapp.adapter.AgraviadoAdapter;
import com.example.comisariaapp.adapter.DenunciadoAdapter;
import com.example.comisariaapp.entity.service.Agraviado;
import com.example.comisariaapp.entity.service.Denunciado;
import com.example.comisariaapp.utils.DateDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetalleMisDenunciasActivity extends AppCompatActivity {
    private AgraviadoAdapter aAdapter;
    private DenunciadoAdapter dAdapter;
    private RecyclerView rcvAgraviados, rcvDenunciados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_mis_denuncias);
        this.init();
        this.initAdpaters();
        this.loadData();
    }

    private void init() {
        this.rcvAgraviados = this.findViewById(R.id.rcvAgraviados);
        this.rcvDenunciados = this.findViewById(R.id.rcvDenunciados);
        this.rcvAgraviados.setLayoutManager(new GridLayoutManager(this, 1));
        this.rcvDenunciados.setLayoutManager(new GridLayoutManager(this, 1));
    }

    private void initAdpaters() {
        this.aAdapter = new AgraviadoAdapter(null,new ArrayList<Agraviado>());
        this.rcvAgraviados.setAdapter(aAdapter);
        this.dAdapter = new DenunciadoAdapter(null,new ArrayList<Denunciado>());
        this.rcvDenunciados.setAdapter(dAdapter);
    }

    private void loadData() {
        final Gson g = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();
        Bundle data = getIntent().getExtras();
        List<Agraviado> agraviados = g.fromJson(data.getString("agraviados"), new TypeToken<List<Agraviado>>() {
        }.getType());
        List<Denunciado> denunciados = g.fromJson(data.getString("denunciados"), new TypeToken<List<Denunciado>>() {
        }.getType());
        this.aAdapter.updateItems(agraviados);
        this.dAdapter.updateItems(denunciados);
        //Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
    }
}