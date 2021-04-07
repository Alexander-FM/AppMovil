package com.example.comisariaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.comisariaapp.R;
import com.example.comisariaapp.entity.service.Usuario;
import com.example.comisariaapp.viewmodel.UsuarioViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.util.Date;

public class LoginActivity extends AppCompatActivity {
    private Button btnRegistrarse, btnIniciarSesion;
    private UsuarioViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnRegistrarse.setOnClickListener(v -> {
            startActivity(new Intent(this, RegistrarUsuarioActivity.class));
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        });
        btnIniciarSesion.setOnClickListener(v -> {
            EditText edtEmail = findViewById(R.id.username_input), edtPassword = findViewById(R.id.password_input);
            viewModel.login(edtEmail.getText().toString(), edtPassword.getText().toString()).observe(this, response -> {
                Usuario u = response.getBody();
                if (response.getRpta() == 1) {
                    Toast.makeText(this, "Bienvenido:" + u.getNombres() + " " + u.getApellidoPaterno() + " " + u.getApellidoMaterno(), Toast.LENGTH_SHORT).show();
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);//getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    Gson g = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                            .registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()))
                            .create();
                    editor.putString("UsuarioJson", g.toJson(u));
                    editor.apply();

                    startActivity(new Intent(this, MenuActivity.class));
                } else {
                    Toast.makeText(this, "Crendeciales incorrectas :(", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String pref = preferences.getString("UsuarioJson", "");
        if (!pref.equals("")) {
            Toast.makeText(this, "se detectó una sesion activa,login omitido", Toast.LENGTH_SHORT).show();
            this.startActivity(new Intent(this, MenuActivity.class));
            this.overridePendingTransition(R.anim.left_in, R.anim.left_out);
        }
    }
}