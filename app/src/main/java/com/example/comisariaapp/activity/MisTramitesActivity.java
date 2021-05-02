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
import com.example.comisariaapp.adapter.MisTramitesAdapter;
import com.example.comisariaapp.entity.service.Tramite;
import com.example.comisariaapp.entity.service.Usuario;
import com.example.comisariaapp.utils.DateSerializer;
import com.example.comisariaapp.viewmodel.TramiteViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.TimeTypeAdapter;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class MisTramitesActivity extends AppCompatActivity {
    private TramiteViewModel tramiteViewModel;
    private ListView lsvMisTramites;
    private MisTramitesAdapter adapter;
    private List<Tramite> tramites = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_tramites);
        tramiteViewModel = new ViewModelProvider(this).get(TramiteViewModel.class);
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
        lsvMisTramites = findViewById(R.id.lsvmisTramites);
    }

    private void initAdapter() {
        adapter = new MisTramitesAdapter(this, R.layout.item_mis_tramites_activity, tramites);
        lsvMisTramites.setAdapter(adapter);
    }

    private void loadData() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);//getPreferences(Context.MODE_PRIVATE);
        Gson g = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .registerTypeAdapter(Time.class, new TimeTypeAdapter())
                .create();
        String user = preferences.getString("UsuarioJson", null);
        if (user != null) {
            Usuario usuario = g.fromJson(user, Usuario.class);
            tramiteViewModel.devolverMisTramites(usuario.getId()).observe(this, response -> {
                if (response.getRpta() == 1) {
                    tramites.clear();
                    tramites.addAll(response.getBody());
                    adapter.notifyDataSetChanged();
                }
            });
        }

    }
}