package com.example.comisariaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.comisariaapp.R;
import com.example.comisariaapp.adapter.AgraviadoAdapter;
import com.example.comisariaapp.adapter.DenunciadoAdapter;
import com.example.comisariaapp.communication.AgraviadoCommunication;
import com.example.comisariaapp.communication.DenunciadoCommunication;
import com.example.comisariaapp.entity.service.dto.DenunciaConDetallesDTO;
import com.example.comisariaapp.utils.DenunciaManager;
import com.example.comisariaapp.viewmodel.DenunciaViewModel;

public class PersonasCarritoActity extends AppCompatActivity implements AgraviadoCommunication, DenunciadoCommunication {
    private Button btnSaveDenuncia;
    private AgraviadoAdapter agraviadosAdapter;
    private DenunciadoAdapter denunciadosAdapter;
    private RecyclerView rcvAgraviados, rcvDenunciados;
    private DenunciaViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_personas_carrito_actity);
        this.init();
        this.initAdapters();
    }

    private void init() {
        viewModel = new ViewModelProvider(this).get(DenunciaViewModel.class);
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.atras);
        toolbar.setNavigationOnClickListener(v -> {//Reemplazo con lamba
            this.startActivity(new Intent(getApplicationContext(), RegistrarDenunciaActivity.class));
            this.overridePendingTransition(R.anim.down_in, R.anim.down_out);
        });
        btnSaveDenuncia = this.findViewById(R.id.btnGuardarDenuncia);
        btnSaveDenuncia.setOnClickListener(v -> {
            DenunciaConDetallesDTO dto = DenunciaManager.getDto(this);
            this.viewModel.save(dto).observe(this, response -> {
                Toast.makeText(this, response.getBody(), Toast.LENGTH_SHORT).show();
                if (response.getRpta() == 1) {
                    DenunciaManager.clear(this);
                    this.startActivity(new Intent(this, MenuActivity.class));
                    this.overridePendingTransition(R.anim.down_in, R.anim.down_out);

                }
            });
        });
        this.rcvAgraviados = findViewById(R.id.rcvAgraviados);
        this.rcvDenunciados = findViewById(R.id.rcvDenunciados);
        this.rcvAgraviados.setLayoutManager(new GridLayoutManager(this, 1));
        this.rcvDenunciados.setLayoutManager(new GridLayoutManager(this, 1));
    }

    private void initAdapters() {
        this.agraviadosAdapter = new AgraviadoAdapter(this, DenunciaManager.getDto(this).getAgraviados());
        this.rcvAgraviados.setAdapter(this.agraviadosAdapter);

        this.denunciadosAdapter = new DenunciadoAdapter(this, DenunciaManager.getDto(this).getDenunciados());
        this.rcvDenunciados.setAdapter(this.denunciadosAdapter);

    }

    @Override
    public void deleteA(String numeroIdentificacion) {
        Toast.makeText(this, DenunciaManager.removeAgraviado(numeroIdentificacion, this), Toast.LENGTH_SHORT).show();
        this.agraviadosAdapter.updateItems(DenunciaManager.getDto(this).getAgraviados());
    }

    @Override
    public void deleteD(String numeroidentificacion) {
        Toast.makeText(this, DenunciaManager.removeDenunciado(numeroidentificacion, this), Toast.LENGTH_SHORT).show();
        this.denunciadosAdapter.updateItems(DenunciaManager.getDto(this).getDenunciados());
    }
}