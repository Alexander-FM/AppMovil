package com.example.comisariaapp.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ConsultarTramiteFragment extends Fragment {

    private Button btnConsultar;
    private EditText edtCodTramite;
    private TramiteViewModel tramiteViewModel;
    private ConstraintLayout constraintLayoutTramite;

    private TextView txtNombreTipoTramite, txtNombreCodTramite, txtNombrePoliciaTramite,
            txtNombreFechaDenunciaTramite;
    private ImageView imgEstadoTramite;

    public ConsultarTramiteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_consultar_tramite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tramiteViewModel = new ViewModelProvider(this).get(TramiteViewModel.class);
        init(view);
    }

    private void init(View v) {
        btnConsultar = v.findViewById(R.id.btnConsultarTramite);
        edtCodTramite = v.findViewById(R.id.edtCodTramite);
        constraintLayoutTramite = v.findViewById(R.id.constraintLayoutTramite);
        txtNombreCodTramite = v.findViewById(R.id.txtNombreCodTramite);
        txtNombreTipoTramite = v.findViewById(R.id.txtNombreTipoTramite);
        txtNombrePoliciaTramite = v.findViewById(R.id.txtNombrePoliciaTramite);
        txtNombreFechaDenunciaTramite = v.findViewById(R.id.txtNombreFechaDenunciaTramite);
        imgEstadoTramite = v.findViewById(R.id.imgEstadoTramite);
        btnConsultar.setOnClickListener(vi -> {
            if (!edtCodTramite.getText().toString().equals("")) {
                if (!edtCodTramite.getText().toString().equals("? ? ?")) {
                    final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());//getPreferences(Context.MODE_PRIVATE);
                    final Gson g = new GsonBuilder()
                            .registerTypeAdapter(Date.class, new DateSerializer())
                            .registerTypeAdapter(Time.class, new TimeSerializer())
                            .create();
                    final String user = preferences.getString("UsuarioJson", null);
                    if (user != null) {
                        Usuario usuario = g.fromJson(user, Usuario.class);
                        tramiteViewModel.consultarTramites(edtCodTramite.getText().toString(), usuario.getId()).observe(getViewLifecycleOwner(), response -> {
                            if (response.getRpta() == 1) {
                                if (response.getBody().getId() != 0) {
                                    Toast.makeText(getContext(), "Excelente, se encontro un trámite", Toast.LENGTH_LONG).show();
                                    constraintLayoutTramite.setVisibility(View.VISIBLE);
                                    showTramite(response.getBody());
                                } else {
                                    Toast.makeText(getContext(), "Trámite no encontrado", Toast.LENGTH_SHORT).show();
                                    constraintLayoutTramite.setVisibility(View.INVISIBLE);
                                }

                            } else {
                                Toast.makeText(getContext(), "Se ha producido un error en el servidor", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(getContext(), "Los trámites sin código podrá verlos en el apartado de *Mis Trámites*", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getContext(), "Complete los campos, por favor!", Toast.LENGTH_LONG).show();
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