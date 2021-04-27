package com.example.comisariaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;

import com.example.comisariaapp.R;
import com.example.comisariaapp.adapter.MisDenunciasAdapter;
import com.example.comisariaapp.communication.MisDenunciasCommunication;
import com.example.comisariaapp.entity.service.Agraviado;
import com.example.comisariaapp.entity.service.Denuncia;
import com.example.comisariaapp.entity.service.Denunciado;
import com.example.comisariaapp.entity.service.Usuario;
import com.example.comisariaapp.entity.service.dto.DenunciaConDetallesDTO;
import com.example.comisariaapp.utils.DateDeserializer;
import com.example.comisariaapp.viewmodel.DenunciaViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MisDenunciasActivity extends AppCompatActivity implements MisDenunciasCommunication {
    private DenunciaViewModel denunciaViewModel;
    private ListView lsvMisDenuncias;
    private MisDenunciasAdapter adapter;
    private final List<Denuncia> denuncias = new ArrayList<>();
    private final List<DenunciaConDetallesDTO> dtos = new ArrayList<>();

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
        adapter = new MisDenunciasAdapter(this, R.layout.item_mis_denuncias_activity, denuncias, this);
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
                if (response.getRpta() == 1) {
                    denuncias.clear();
                    dtos.addAll(response.getBody());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        response.getBody().forEach(dto -> denuncias.add(dto.getDenuncia()));
                    }else{
                        for (DenunciaConDetallesDTO dto:response.getBody()){
                            denuncias.add(dto.getDenuncia());
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        }

    }


    @Override
    public void showDetalles(int index) {
        final Gson g = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();
        final List<Agraviado> agraviados = dtos.get(index).getAgraviados();
        final List<Denunciado> denunciados = dtos.get(index).getDenunciados();
        final Intent i = new Intent(this, DetalleMisDenunciasActivity.class);
        i.putExtra("agraviados", g.toJson(agraviados));
        i.putExtra("denunciados", g.toJson(denunciados));
        this.startActivity(i);
    }
}