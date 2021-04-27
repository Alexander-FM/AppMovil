package com.example.comisariaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.comisariaapp.R;

public class BuzonSugerenciasActivity extends AppCompatActivity {

    private RatingBar ratingBar;

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
        init();
    }

    private void init() {
        ratingBar = findViewById(R.id.ratingbar);
        ratingBar.setOnRatingBarChangeListener((ratingBar1, v, b) -> {
            Toast.makeText(BuzonSugerenciasActivity.this, "Usted a votado con:" + v, Toast.LENGTH_SHORT).show();
        });
    }
}