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
import com.example.comisariaapp.entity.service.Agraviado;
import com.example.comisariaapp.entity.service.Denuncia;
import com.example.comisariaapp.entity.service.Denunciado;
import com.example.comisariaapp.entity.service.Usuario;
import com.example.comisariaapp.entity.service.dto.DenunciaConDetallesDTO;
import com.example.comisariaapp.utils.DateSerializer;
import com.example.comisariaapp.utils.TimeSerializer;
import com.example.comisariaapp.viewmodel.DenunciaViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Time;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

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
                final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);//getPreferences(Context.MODE_PRIVATE);
                final Gson g = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd")
                        .registerTypeAdapter(Date.class, new DateSerializer())
                        .create();
                final String user = preferences.getString("UsuarioJson", null);
                if (user != null) {
                    final Usuario usuario = g.fromJson(user, Usuario.class);
                    this.denunciaViewModel.consultarDenuncias(edtCodDenuncia.getText().toString(), usuario.getId()).observe(this, response -> {
                        if (response.getRpta() == 1) {
                            if (response.getBody().getDenuncia().getId() != 0) {
                                Toast.makeText(this, "Excelente, se encontro una denuncia", Toast.LENGTH_LONG).show();
                                this.constraintLayoutDenuncia.setVisibility(View.VISIBLE);
                                final DenunciaConDetallesDTO dto = response.getBody();
                                this.showDenuncia(dto.getDenuncia());
                                this.asignarListenerConstraintLayout(dto);
                            } else {
                                Toast.makeText(this, "Denuncia no encontrada", Toast.LENGTH_SHORT).show();
                                this.constraintLayoutDenuncia.setVisibility(View.INVISIBLE);
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

    private void asignarListenerConstraintLayout(DenunciaConDetallesDTO dto) {
        this.constraintLayoutDenuncia.setOnClickListener(v -> {
            final Gson g = new GsonBuilder()
                    .registerTypeAdapter(Date.class, new DateSerializer())
                    .registerTypeAdapter(Time.class,new TimeSerializer())
                    .create();
            final List<Agraviado> agraviados = dto.getAgraviados();
            final List<Denunciado> denunciados = dto.getDenunciados();
            final Intent i = new Intent(this, DetalleMisDenunciasActivity.class);
            i.putExtra("agraviados", g.toJson(agraviados));
            i.putExtra("denunciados", g.toJson(denunciados));
            this.startActivity(i);
        });
    }
}