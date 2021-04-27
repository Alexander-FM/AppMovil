package com.example.comisariaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.comisariaapp.R;
import com.example.comisariaapp.utils.EmailUtil;

public class CambiarClaveActivity extends AppCompatActivity {
    private EditText edtNuevaClave1, edtNuevaClave2;
    private Button btnCambiarContraseña;

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
        init();
    }

    private void init() {
        edtNuevaClave1 = findViewById(R.id.edtNuevaClave1);
        edtNuevaClave2 = findViewById(R.id.edtNuevaClave2);
        this.btnCambiarContraseña = findViewById(R.id.btnCambiarContraseña);
        this.btnCambiarContraseña.setOnClickListener(v -> {
        });
    }
}