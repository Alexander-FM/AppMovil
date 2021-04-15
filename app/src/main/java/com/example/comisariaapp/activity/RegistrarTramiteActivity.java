package com.example.comisariaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.example.comisariaapp.R;
import com.example.comisariaapp.entity.service.Policia;
import com.example.comisariaapp.entity.service.TipoTramite;
import com.example.comisariaapp.entity.service.Tramites;
import com.example.comisariaapp.entity.service.Usuario;
import com.example.comisariaapp.viewmodel.TipoTramiteViewModel;
import com.example.comisariaapp.viewmodel.TramiteViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class RegistrarTramiteActivity extends AppCompatActivity {
    private MaterialSpinner sp_tipoTramite;
    //private Spinner sp_tipoTramite;
    private List<TipoTramite> tiposTramite = new ArrayList<>();
    private List<String> displayTiposTramite = new ArrayList<>();
    private TipoTramiteViewModel tipoTramiteViewModel;
    private TramiteViewModel viewModel;
    private ArrayAdapter<String> adapterTramite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_tramite);
        ViewModelProvider vmp = new ViewModelProvider(this);
        viewModel = vmp.get(TramiteViewModel.class);
        tipoTramiteViewModel = vmp.get(TipoTramiteViewModel.class);
        this.init();
        this.loadData();
    }

    private void init() {
        Button btnRegistrarTramite = findViewById(R.id.btnRegistrarTramite), btnCancelar = findViewById(R.id.btnCancelTramite);
        btnCancelar.setOnClickListener(v -> finish());
        btnRegistrarTramite.setOnClickListener(v -> save());
        sp_tipoTramite = findViewById(R.id.sp_tipoTramite);

        adapterTramite = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, displayTiposTramite);
        adapterTramite.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_tipoTramite.setAdapter(adapterTramite);

    }

    private void loadData() {
        tipoTramiteViewModel.list().observe(this, response -> {
            if (response.getRpta() == 1) {
                tiposTramite.clear();
                tiposTramite.addAll(response.getBody());

                displayTiposTramite.clear();
                for (TipoTramite t : tiposTramite) {
                    displayTiposTramite.add(t.getTipoTramite());
                }
                adapterTramite.notifyDataSetChanged();
            }
        });
    }

    private void save() {
        Tramites t = new Tramites();
        t.setEstadoTramite(false);
        t.setFechaDenuncia(new Date());
        t.setTipoTramite(tiposTramite.get(sp_tipoTramite.getSelectedItemPosition()));
        t.setPolicia(new Policia());
        t.getPolicia().setId(1);
        t.setCodTramite("---");
        final Gson g = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                //.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()))
                .create();
        t.setUsuario(g.fromJson(PreferenceManager.getDefaultSharedPreferences(this).getString("UsuarioJson", ""), Usuario.class));
        viewModel.save(t).observe(this, response -> {
            if (response.getRpta() == 1) {
                Toast.makeText(this, "Tramite registrado con Éxito 📃!!!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Tramite no registrado 😥", Toast.LENGTH_SHORT).show();
            }
        });
    }
}