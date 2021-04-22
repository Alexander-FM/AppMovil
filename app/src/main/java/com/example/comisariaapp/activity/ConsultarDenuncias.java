package com.example.comisariaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comisariaapp.R;
import com.example.comisariaapp.entity.service.Denuncia;
import com.example.comisariaapp.entity.service.Tramites;
import com.example.comisariaapp.entity.service.Usuario;
import com.example.comisariaapp.utils.DateDeserializer;
import com.example.comisariaapp.viewmodel.DenunciaViewModel;
import com.example.comisariaapp.viewmodel.TramiteViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConsultarDenuncias extends AppCompatActivity {
    private Button btnConsultarDenuncia;
    private EditText edtCodDenuncia;
    private DenunciaViewModel denunciaViewModel;
    private ConstraintLayout constraintLayoutDenuncia;

    private TextView txtNombreTipoDenuncia, txtNombreCodDenuncia, txtNombrePolicia,
            txtNombreVPD, txtNombreFechaDenuncia;
    private ImageView imgEstado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_denuncias);
        denunciaViewModel = new ViewModelProvider(this).get(DenunciaViewModel.class);
        init();
    }

    private void init() {
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.atras);
        toolbar.setNavigationOnClickListener(v -> {//Reemplazo con lamba
            this.startActivity(new Intent(getApplicationContext(), MenuActivity.class));
            this.overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
        });
        btnConsultarDenuncia = findViewById(R.id.btnConsultarDenuncia);
        edtCodDenuncia = findViewById(R.id.edtCodDenuncia);
        constraintLayoutDenuncia = findViewById(R.id.constraintLayoutDenuncia);
        txtNombreCodDenuncia = findViewById(R.id.txtNombreCodDenuncia);
        txtNombreTipoDenuncia = findViewById(R.id.txtNombreTipoDenuncia);
        txtNombrePolicia = findViewById(R.id.txtNombrePolicia);
        txtNombreFechaDenuncia = findViewById(R.id.txtNombreFechaDenuncia);
        txtNombreVPD = findViewById(R.id.txtNombreVPD);
        imgEstado = findViewById(R.id.imgEstado);
        btnConsultarDenuncia.setOnClickListener(v -> {
            if (!edtCodDenuncia.getText().toString().equals("")) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);//getPreferences(Context.MODE_PRIVATE);
                Gson g = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd")
                        .registerTypeAdapter(Date.class, new DateDeserializer())
                        .create();
                String user = preferences.getString("UsuarioJson", null);
                if (user != null) {
                    Usuario usuario = g.fromJson(user, Usuario.class);
                    denunciaViewModel.consultarDenuncias(edtCodDenuncia.getText().toString(), usuario.getId()).observe(this, response -> {
                        if (response.getRpta() == 1) {
                            if (response.getBody().getId() != 0) {
                                Toast.makeText(this, "Excelente, se encontro una denuncia", Toast.LENGTH_LONG).show();
                                constraintLayoutDenuncia.setVisibility(View.VISIBLE);
                                showDenuncia(response.getBody());
                            } else {
                                Toast.makeText(this, "Denuncia no encontrada", Toast.LENGTH_SHORT).show();
                                constraintLayoutDenuncia.setVisibility(View.INVISIBLE);
                            }

                        } else {
                            Toast.makeText(this, "Se ha producido un error en el servidor", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(this, "Complete los campos, por favor!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showDenuncia(Denuncia d) {
        imgEstado.setImageResource(d.isEstadoDenuncia() ? R.drawable.cheque : R.drawable.reloj);
        txtNombreTipoDenuncia.setText(d.getTipoDenuncia().getTipoDenuncia());
        txtNombreVPD.setText(d.getVinculoParteDenunciada().getNombre());
        txtNombrePolicia.setText(d.getPolicia().getNombres() + " " + d.getPolicia().getApellidoPaterno() + " " + d.getPolicia().getApellidoMaterno());
        txtNombreCodDenuncia.setText(d.getCod_denuncia());
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = dateFormat.format(d.getFechaDenuncia());
        txtNombreFechaDenuncia.setText(strDate);
    }
}