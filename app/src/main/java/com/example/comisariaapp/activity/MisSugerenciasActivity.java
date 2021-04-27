package com.example.comisariaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;

import com.example.comisariaapp.R;
import com.example.comisariaapp.adapter.MisSugerenciasAdapter;
import com.example.comisariaapp.adapter.MisTramitesAdapter;
import com.example.comisariaapp.entity.service.Sugerencia;
import com.example.comisariaapp.entity.service.Tramites;
import com.example.comisariaapp.entity.service.Usuario;
import com.example.comisariaapp.utils.DateDeserializer;
import com.example.comisariaapp.viewmodel.SugerenciaViewModel;
import com.example.comisariaapp.viewmodel.TramiteViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MisSugerenciasActivity extends AppCompatActivity {
    private SugerenciaViewModel sugerenciaViewModel;
    private ListView lsvMisSugerencias;
    private MisSugerenciasAdapter adapter;
    private List<Sugerencia> sugerencias = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_sugerencias);
        sugerenciaViewModel = new ViewModelProvider(this).get(SugerenciaViewModel.class);
        init();
        initAdapter();
        loadData();
    }

    private void init() {
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.atras);
        toolbar.setNavigationOnClickListener(v -> {//Reemplazo con lamba
            this.startActivity(new Intent(getApplicationContext(), MenuActivity.class));
            this.overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
        });
        lsvMisSugerencias = findViewById(R.id.lsvSugerencias);
    }

    private void initAdapter() {
        adapter = new MisSugerenciasAdapter(this, R.layout.item_mis_sugerencias, sugerencias);
        lsvMisSugerencias.setAdapter(adapter);
    }

    private void loadData() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);//getPreferences(Context.MODE_PRIVATE);
        Gson g = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();
        String user = preferences.getString("UsuarioJson", null);
        if (user != null) {
            Usuario usuario = g.fromJson(user, Usuario.class);
            sugerenciaViewModel.devolverMisComentarioByUsu(usuario.getId()).observe(this, response -> {
                if (response.getRpta() == 1) {
                    sugerencias.clear();
                    sugerencias.addAll(response.getBody());
                    adapter.notifyDataSetChanged();
                }
            });
        }

    }
}