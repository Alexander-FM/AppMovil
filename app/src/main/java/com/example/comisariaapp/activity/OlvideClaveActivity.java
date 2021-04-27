package com.example.comisariaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.comisariaapp.R;

public class OlvideClaveActivity extends AppCompatActivity {
    private EditText edtCorreoRecuperacion;
    private Button btnContinuar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvide_clave);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.atras);
        toolbar.setNavigationOnClickListener(v -> {//Reemplazo con lamba
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
        });
        init();
    }

    private void init() {
        edtCorreoRecuperacion = findViewById(R.id.edtCorreoRecuperacion);
        btnContinuar = findViewById(R.id.btnContinuar);
        btnContinuar.setOnClickListener(view -> {
            startActivity(new Intent(this, CambiarClaveActivity.class));
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        });
    }
}