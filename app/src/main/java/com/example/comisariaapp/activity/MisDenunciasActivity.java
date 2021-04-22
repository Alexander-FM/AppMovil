package com.example.comisariaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;

import com.example.comisariaapp.R;
import com.example.comisariaapp.adapter.MisDenunciasAdapter;
import com.example.comisariaapp.entity.service.Denuncia;
import com.example.comisariaapp.entity.service.Usuario;
import com.example.comisariaapp.utils.DateDeserializer;
import com.example.comisariaapp.viewmodel.DenunciaViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MisDenunciasActivity extends AppCompatActivity {
    private DenunciaViewModel denunciaViewModel;
    private ListView lsvMisDenuncias;
    private MisDenunciasAdapter adapter;
    private List<Denuncia> denuncias = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_denuncias);
        denunciaViewModel = new ViewModelProvider(this).get(DenunciaViewModel.class);
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

        lsvMisDenuncias = findViewById(R.id.lsvMisDenuncias);
    }

    private void initAdapter() {
        adapter = new MisDenunciasAdapter(this, R.layout.item_mis_denuncias_activity, denuncias);
        lsvMisDenuncias.setAdapter(adapter);
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
            denunciaViewModel.devolverMisDenuncias(usuario.getId()).observe(this, response -> {
                if (response.getRpta() == 1){
                    denuncias.clear();
                    denuncias.addAll(response.getBody());
                    adapter.notifyDataSetChanged();
                }
            });
        }

    }


}