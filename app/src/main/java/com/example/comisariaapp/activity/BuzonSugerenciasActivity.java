package com.example.comisariaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.comisariaapp.R;
import com.example.comisariaapp.entity.service.Sugerencia;
import com.example.comisariaapp.entity.service.Usuario;
import com.example.comisariaapp.utils.DateSerializer;
import com.example.comisariaapp.utils.TimeSerializer;
import com.example.comisariaapp.viewmodel.SugerenciaViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Time;
import java.sql.Date;

public class BuzonSugerenciasActivity extends AppCompatActivity {
    private EditText edtSugerencia;
    private RatingBar ratingBar;
    private Button btnEnviarSugerencia;
    private SugerenciaViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buzon_sugerencias);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.atras);
        toolbar.setNavigationOnClickListener(v -> {//Reemplazo con lamba
            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
            overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
        });
        viewModel = new ViewModelProvider(this).get(SugerenciaViewModel.class);
        init();
    }

    private void init() {
        ratingBar = findViewById(R.id.ratingbar);
        edtSugerencia = findViewById(R.id.edtsugerencia);
        btnEnviarSugerencia = findViewById(R.id.btnEnviarSugerencia);
        ratingBar.setOnRatingBarChangeListener((ratingBar1, v, b) -> {
            Toast.makeText(BuzonSugerenciasActivity.this, "Usted a votado con:" + v, Toast.LENGTH_SHORT).show();
        });
        btnEnviarSugerencia.setOnClickListener(view -> {
            Sugerencia sugerencia = new Sugerencia();
            sugerencia.setComentario(edtSugerencia.getText().toString());
            sugerencia.setEstrellas(ratingBar.getRating());
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);//getPreferences(Context.MODE_PRIVATE);
            Gson g = new GsonBuilder()
                    .registerTypeAdapter(Date.class, new DateSerializer())
                    .registerTypeAdapter(Time.class,new TimeSerializer())
                    .create();
            String user = preferences.getString("UsuarioJson", null);
            Usuario usuario = g.fromJson(user, Usuario.class);
            sugerencia.setUsuario(usuario);
            viewModel.save(sugerencia).observe(this, response -> {
                if (response.getRpta() == 1){
                    Toast.makeText(this, "Felicitaciones. Su sugerencia ha sido guardo con éxito !!!", Toast.LENGTH_LONG).show();
                    finish();
                    overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
                }
            });
        });
    }
}