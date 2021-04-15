package com.example.comisariaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.comisariaapp.R;
import com.example.comisariaapp.adapter.AgraviadoAdapter;
import com.example.comisariaapp.utils.DenunciaManager;

public class PersonasCarritoActity extends AppCompatActivity {
    private AgraviadoAdapter agraviadosAdapter;
    private RecyclerView rcvAgraviados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personas_carrito_actity);
        this.init();
    }

    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.atras);
        toolbar.setNavigationOnClickListener(v -> {//Reemplazo con lamba
            startActivity(new Intent(getApplicationContext(), RegistrarDenunciaActivity.class));
            overridePendingTransition(R.anim.down_in, R.anim.down_out);
        });
        this.rcvAgraviados = findViewById(R.id.rcvAgraviados);
        this.rcvAgraviados.setLayoutManager(new GridLayoutManager(this,0));
    }
    private void initAdapters(){
        this.agraviadosAdapter = new AgraviadoAdapter(DenunciaManager.getDto().getAgraviados());
        this.rcvAgraviados.setAdapter(this.agraviadosAdapter);
    }
}