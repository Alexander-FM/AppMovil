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
import android.widget.Toast;

import com.example.comisariaapp.R;
import com.example.comisariaapp.entity.service.Usuario;
import com.example.comisariaapp.utils.DateDeserializer;
import com.example.comisariaapp.utils.EmailUtil;
import com.example.comisariaapp.viewmodel.UsuarioViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

public class CambiarClaveActivity extends AppCompatActivity {
    private EditText edtNuevaClave1, edtNuevaClave2;
    private Button btnCambiarContraseña;
    private UsuarioViewModel viewModel;
    private String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_clave);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.atras);
        toolbar.setNavigationOnClickListener(v -> {//Reemplazo con lamba
            startActivity(new Intent(getApplicationContext(), OlvideClaveActivity.class));
            overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
        });
        this.viewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
        this.email = this.getIntent().getExtras().getString("email");
        init();
    }

    private void init() {
        edtNuevaClave1 = findViewById(R.id.edtNuevaClave1);
        edtNuevaClave2 = findViewById(R.id.edtNuevaClave2);
        this.btnCambiarContraseña = findViewById(R.id.btnCambiarContraseña);
        this.btnCambiarContraseña.setOnClickListener(v -> {
            if (edtNuevaClave1.getText().toString().equals(edtNuevaClave2.getText().toString())) {
                viewModel.changePassword(this.email, this.edtNuevaClave2.getText().toString()).observe(
                        this, response -> {
                            if(response.getRpta() ==1){
                                Toast.makeText(this, response.getMessage(), Toast.LENGTH_LONG).show();
                                startActivity(new Intent(this, LoginActivity.class));
                                overridePendingTransition(R.anim.down_in, R.anim.down_out);
                            }
                        });
            } else {
                Toast.makeText(this, "Las contraseñas no coinciden, intente otra vez!", Toast.LENGTH_LONG).show();
            }
        });
    }
}