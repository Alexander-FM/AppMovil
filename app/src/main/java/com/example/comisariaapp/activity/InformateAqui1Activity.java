package com.example.comisariaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import com.example.comisariaapp.R;
import com.example.comisariaapp.adapter.SeccionPaginaInformativaAdapter;

import java.util.ArrayList;

public class InformateAqui1Activity extends AppCompatActivity {
    private ArrayList<SeccionPaginaInformativa> seccions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informate_aqui1);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.atras);
        toolbar.setNavigationOnClickListener(v -> {//Reemplazo con lamba
            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
            overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
        });
        this.seccions = new ArrayList<>();
        seccions.add(new SeccionPaginaInformativa(1, "Violencia Física", R.drawable.violencia_mujer, "#66A5AD", "#07575B"));
        seccions.add(new SeccionPaginaInformativa(2, "Violencia Sexual", R.drawable.violencia_sexual, "#68829e", "#505160"));
        seccions.add(new SeccionPaginaInformativa(3, "Violencia Psicológica", R.drawable.violencia_psicologic, "#90afc5", "#003b46"));
        seccions.add(new SeccionPaginaInformativa(4, "Violencia Economica", R.drawable.violencia_economica, "#336B87", "#2a3132"));

        GridView grwSecciones = findViewById(R.id.grwSeccionesPagInf);
        SeccionPaginaInformativaAdapter adapter = new SeccionPaginaInformativaAdapter(this, R.layout.item_seccion_pagina_informativa, seccions);
        grwSecciones.setAdapter(adapter);
    }
}