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
import com.example.comisariaapp.entity.service.Tramite;
import com.example.comisariaapp.entity.service.Usuario;
import com.example.comisariaapp.utils.DateSerializer;
import com.example.comisariaapp.utils.TimeSerializer;
import com.example.comisariaapp.viewmodel.TramiteViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.sql.Time;
public class ConsultarTramiteActivity extends AppCompatActivity {
    private Button btnConsultar;
    private EditText edtCodTramite;
    private TramiteViewModel tramiteViewModel;
    private ConstraintLayout constraintLayoutTramite;

    private TextView txtNombreTipoTramite, txtNombreCodTramite, txtNombrePoliciaTramite,
            txtNombreFechaDenunciaTramite;
    private ImageView imgEstadoTramite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_tramite);
        tramiteViewModel = new ViewModelProvider(this).get(TramiteViewModel.class);
        init();
    }

    private void init() {
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.atras);
        toolbar.setNavigationOnClickListener(v -> {//Reemplazo con lamba
            this.startActivity(new Intent(getApplicationContext(), MenuActivity.class));
            this.overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
        });
        btnConsultar = findViewById(R.id.btnConsultarTramite);
        edtCodTramite = findViewById(R.id.edtCodTramite);
        constraintLayoutTramite = findViewById(R.id.constraintLayoutTramite);
        txtNombreCodTramite = findViewById(R.id.txtNombreCodTramite);
        txtNombreTipoTramite = findViewById(R.id.txtNombreTipoTramite);
        txtNombrePoliciaTramite = findViewById(R.id.txtNombrePoliciaTramite);
        txtNombreFechaDenunciaTramite = findViewById(R.id.txtNombreFechaDenunciaTramite);
        imgEstadoTramite = findViewById(R.id.imgEstadoTramite);
        btnConsultar.setOnClickListener(v -> {
            if (!edtCodTramite.getText().toString().equals("")) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);//getPreferences(Context.MODE_PRIVATE);
                Gson g = new GsonBuilder()
                        .registerTypeAdapter(Date.class, new DateSerializer())
                        .registerTypeAdapter(Time.class,new TimeSerializer())
                        .create();
                String user = preferences.getString("UsuarioJson", null);
                if (user != null) {
                    Usuario usuario = g.fromJson(user, Usuario.class);
                    tramiteViewModel.consultarTramites(edtCodTramite.getText().toString(), usuario.getId()).observe(this, response -> {
                        if (response.getRpta() == 1) {
                            if (response.getBody().getId() != 0) {
                                Toast.makeText(this, "Excelente, se encontro un trámite", Toast.LENGTH_LONG).show();
                                constraintLayoutTramite.setVisibility(View.VISIBLE);
                                showTramite(response.getBody());
                            } else {
                                Toast.makeText(this, "Trámite no encontrado", Toast.LENGTH_SHORT).show();
                                constraintLayoutTramite.setVisibility(View.INVISIBLE);
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

    private void showTramite(Tramite t) {
        imgEstadoTramite.setImageResource(t.isEstadoTramite() ? R.drawable.cheque : R.drawable.reloj);
        txtNombreTipoTramite.setText(t.getTipoTramite().getTipoTramite());
        txtNombrePoliciaTramite.setText(t.getPolicia().getNombres() + " " + t.getPolicia().getApellidoPaterno() + " " + t.getPolicia().getApellidoMaterno());
        txtNombreCodTramite.setText(t.getCodTramite());
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = dateFormat.format(t.getFechaTramite());
        txtNombreFechaDenunciaTramite.setText(strDate);
    }
}