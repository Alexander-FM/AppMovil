package com.example.comisariaapp.fragments;

import android.content.Intent;
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
import com.example.comisariaapp.activity.DetalleMisDenunciasActivity;
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

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class ConsultarDenunciaFragment extends Fragment {

    private Button btnConsultarDenuncia;
    private EditText edtCodDenuncia;
    private DenunciaViewModel denunciaViewModel;
    private ConstraintLayout constraintLayoutDenuncia;

    private TextView txtNombreTipoDenuncia, txtNombreCodDenuncia, txtNombrePolicia,
            txtNombreVPD, txtNombreFechaDenuncia;
    private ImageView imgEstado;


    public ConsultarDenunciaFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_consultar_denuncia, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        denunciaViewModel = new ViewModelProvider(this).get(DenunciaViewModel.class);
        init(view);

    }

    private void init(View v) {
        btnConsultarDenuncia = v.findViewById(R.id.btnConsultarDenuncia);
        edtCodDenuncia = v.findViewById(R.id.edtCodDenuncia);
        constraintLayoutDenuncia = v.findViewById(R.id.constraintLayoutDenuncia);
        txtNombreCodDenuncia = v.findViewById(R.id.txtNombreCodDenuncia);
        txtNombreTipoDenuncia = v.findViewById(R.id.txtNombreTipoDenuncia);
        txtNombrePolicia = v.findViewById(R.id.txtNombrePolicia);
        txtNombreFechaDenuncia = v.findViewById(R.id.txtNombreFechaDenuncia);
        txtNombreVPD = v.findViewById(R.id.txtNombreVPD);
        imgEstado = v.findViewById(R.id.imgEstado);
        btnConsultarDenuncia.setOnClickListener(vi -> {
            if (!edtCodDenuncia.getText().toString().equals("")) {
                if (!edtCodDenuncia.getText().toString().equals("? ? ?")) {
                    final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());//getPreferences(Context.MODE_PRIVATE);
                    final Gson g = new GsonBuilder()
                            .registerTypeAdapter(Date.class, new DateSerializer())
                            .registerTypeAdapter(Time.class, new TimeSerializer())
                            .create();
                    final String user = preferences.getString("UsuarioJson", null);
                    if (user != null) {
                        final Usuario usuario = g.fromJson(user, Usuario.class);
                        this.denunciaViewModel.consultarDenuncias(edtCodDenuncia.getText().toString(), usuario.getId()).observe(getViewLifecycleOwner(), response -> {
                            if (response.getRpta() == 1) {
                                if (response.getBody().getDenuncia().getId() != 0) {
                                    Toast.makeText(getContext(), "Excelente, se encontro una denuncia", Toast.LENGTH_LONG).show();
                                    this.constraintLayoutDenuncia.setVisibility(View.VISIBLE);
                                    final DenunciaConDetallesDTO dto = response.getBody();
                                    this.showDenuncia(dto.getDenuncia());
                                    this.asignarListenerConstraintLayout(dto);
                                } else {
                                    Toast.makeText(getContext(), "Denuncia no encontrada", Toast.LENGTH_SHORT).show();
                                    this.constraintLayoutDenuncia.setVisibility(View.INVISIBLE);
                                }

                            } else {
                                Toast.makeText(getContext(), "Se ha producido un error en el servidor", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(getContext(), "Las denuncias sin código podrá verlas en el apartado de *Mis Denuncias*", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Complete los campos, por favor!", Toast.LENGTH_LONG).show();
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
                    .registerTypeAdapter(Time.class, new TimeSerializer())
                    .create();
            final List<Agraviado> agraviados = dto.getAgraviados();
            final List<Denunciado> denunciados = dto.getDenunciados();
            final Intent i = new Intent(getContext(), DetalleMisDenunciasActivity.class);
            i.putExtra("agraviados", g.toJson(agraviados));
            i.putExtra("denunciados", g.toJson(denunciados));
            this.startActivity(i);
        });
    }

}