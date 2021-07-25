package com.example.comisariaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.comisariaapp.R;
import com.example.comisariaapp.entity.service.Comisarias;
import com.example.comisariaapp.entity.service.Policia;
import com.example.comisariaapp.entity.service.TipoTramite;
import com.example.comisariaapp.entity.service.Tramite;
import com.example.comisariaapp.entity.service.Usuario;
import com.example.comisariaapp.utils.DateSerializer;
import com.example.comisariaapp.utils.TimeSerializer;
import com.example.comisariaapp.viewmodel.ComisariasViewModel;
import com.example.comisariaapp.viewmodel.TipoTramiteViewModel;
import com.example.comisariaapp.viewmodel.TramiteViewModel;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import fr.ganfra.materialspinner.MaterialSpinner;

public class RegistrarTramiteActivity extends AppCompatActivity {
    private MaterialSpinner sp_tipoTramite, sp_comisariaT;
    private List<TipoTramite> tiposTramite = new ArrayList<>();
    private List<Comisarias> comisarias = new ArrayList<>();
    private List<String> displayTiposTramite = new ArrayList<>();
    private List<String> displayComisarias = new ArrayList<>();
    private TipoTramiteViewModel tipoTramiteViewModel;
    private ComisariasViewModel comisariasViewModel;
    private TramiteViewModel viewModel;
    private ArrayAdapter<String> adapterTramite;
    private ArrayAdapter<String> adapterComisarias;
    private EditText correoUsuarioTramite, edtTelefonoTramite, edtObservaciones, edtMotivoDenunciaPolicias, edtEspecificarS;
    private CheckBox chkmismoCorreo;
    private RadioButton radio_denunciante, radio_denunciado, radio_otro;
    private LinearLayout lineaEspecificarS;
    private TextInputLayout text_input_telefonoTramite, text_input_correoT, text_input_motivo_denuncia, text_input_observaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_tramite);
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.atras);
        toolbar.setNavigationOnClickListener(v -> {//Reemplazo con lamba
            this.startActivity(new Intent(getApplicationContext(), MenuActivity.class));
            this.overridePendingTransition(R.anim.down_in, R.anim.down_out);
        });
        ViewModelProvider vmp = new ViewModelProvider(this);
        viewModel = vmp.get(TramiteViewModel.class);
        tipoTramiteViewModel = vmp.get(TipoTramiteViewModel.class);
        this.comisariasViewModel = vmp.get(ComisariasViewModel.class);
        this.init();
        this.loadData();
    }

    private void init() {
        Button btnRegistrarTramite = findViewById(R.id.btnRegistrarTramite), btnCancelar = findViewById(R.id.btnCancelTramite);
        //EDITEXT
        correoUsuarioTramite = findViewById(R.id.edtCorreoElectronicoT);
        edtTelefonoTramite = findViewById(R.id.edtTelefonoTramite);
        edtObservaciones = findViewById(R.id.edtObservaciones);
        edtEspecificarS = findViewById(R.id.edtEspecificarS);
        edtMotivoDenunciaPolicias = findViewById(R.id.edtMotivoDenunciaPolicias);
        //CHECKBOX
        chkmismoCorreo = findViewById(R.id.chkmismocorreotramite);
        //RADIOBUTTONS
        radio_denunciante = findViewById(R.id.radio_denunciante);
        radio_denunciado = findViewById(R.id.radio_denunciado);
        radio_otro = findViewById(R.id.radio_otro);
        //LINEARLAYOUT
        lineaEspecificarS = findViewById(R.id.lineaEspecificarS);
        //TEXTlAYOUT
        text_input_telefonoTramite = findViewById(R.id.text_input_telefonoTramite);
        text_input_correoT = findViewById(R.id.text_input_correoT);
        text_input_motivo_denuncia = findViewById(R.id.text_input_motivo_denuncia);
        text_input_observaciones = findViewById(R.id.text_input_observaciones);
        //ASIGNANDO EL CHANGE LISTENER AL CHECKBOX.
        chkmismoCorreo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                getCorreoUsuario();//asigna a la caja de texto el correo del usuario en sesión.
            } else {
                correoUsuarioTramite.setText("");//limpia la caja de texto si esta desmarcada
                edtTelefonoTramite.setText("");
                correoUsuarioTramite.setEnabled(true);
                edtTelefonoTramite.setEnabled(true);
            }
        });
        radio_otro.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                lineaEspecificarS.setVisibility(View.VISIBLE);
            } else {
                lineaEspecificarS.setVisibility(View.GONE);
            }
        });

        btnCancelar.setOnClickListener(v -> finish());
        btnRegistrarTramite.setOnClickListener(v -> save());
        sp_tipoTramite = findViewById(R.id.sp_tipoTramite);
        sp_comisariaT = findViewById(R.id.sp_comisariaT);
        //Cargando los tipo de tramite en el spinner
        adapterTramite = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, displayTiposTramite);
        adapterTramite.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_tipoTramite.setAdapter(adapterTramite);
        //Cargando las comisarías disponibles en el spinner.
        adapterComisarias = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, displayComisarias);
        adapterComisarias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_comisariaT.setAdapter(adapterComisarias);
        //Agregando addChangeListener al los editext.
        correoUsuarioTramite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                text_input_correoT.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtTelefonoTramite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                text_input_telefonoTramite.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtMotivoDenunciaPolicias.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                text_input_motivo_denuncia.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtObservaciones.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                text_input_observaciones.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private boolean validar() {
        boolean retorno = true;
        String correoT, telefonoT, observacionT, motivoT;
        correoT = correoUsuarioTramite.getText().toString();
        telefonoT = edtTelefonoTramite.getText().toString();
        motivoT = edtMotivoDenunciaPolicias.getText().toString();

        if (correoT.isEmpty()) {
            text_input_correoT.setError("Introduce tu correo electrónico");
            retorno = false;
        } else {
            text_input_correoT.setErrorEnabled(false);
        }
        if (telefonoT.isEmpty()) {
            text_input_telefonoTramite.setError("Introduce tu teléfono");
            retorno = false;
        } else {
            text_input_telefonoTramite.setErrorEnabled(false);
        }
        if (motivoT.isEmpty()) {
            text_input_motivo_denuncia.setError("Específica el motivo de la denuncia");
            retorno = false;
        } else {
            text_input_motivo_denuncia.setErrorEnabled(false);
        }
        if (sp_tipoTramite.getSelectedItemPosition() == 0) {
            TextView errorText1 = (TextView) sp_tipoTramite.getSelectedView();
            errorText1.setError("");
            errorText1.setTextColor(Color.RED);
            errorText1.setText("Seleccione");
        } else {
            sp_tipoTramite.setError(null);
        }
        if (sp_comisariaT.getSelectedItemPosition() == 0) {
            TextView errorText2 = (TextView) sp_comisariaT.getSelectedView();
            errorText2.setError("");
            errorText2.setTextColor(Color.RED);
            errorText2.setText("Seleccione");
        } else {
            sp_comisariaT.setError(null);
        }

        return retorno;
    }

    private void getCorreoUsuario() {
        //.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()))
        Gson g = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .registerTypeAdapter(Time.class, new TimeSerializer())
                .create();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String strU = preferences.getString("UsuarioJson", "");
        if (!strU.equals("")) {
            Usuario us = g.fromJson(strU, Usuario.class);
            this.correoUsuarioTramite.setText(us.getCorreo());//Obtengo su correo del usuario en sesión
            correoUsuarioTramite.setEnabled(false);
            this.edtTelefonoTramite.setText(us.getTelefono());//Obtengo su telefono del usuario en sesión
            edtTelefonoTramite.setEnabled(false);
        }
    }

    private void loadData() {
        tipoTramiteViewModel.list().observe(this, response -> {
            if (response.getRpta() == 1) {
                tiposTramite.clear();
                tiposTramite.addAll(response.getBody());

                displayTiposTramite.clear();
                for (TipoTramite t : tiposTramite) {
                    displayTiposTramite.add(t.getTipoTramite());
                }
                adapterTramite.notifyDataSetChanged();
            }
        });
        this.comisariasViewModel.list().observe(this, response -> {
            if (response.getRpta() == 1) {
                this.comisarias.clear();
                this.comisarias.addAll(response.getBody());
                displayComisarias.clear();
                for (Comisarias c : comisarias) {
                    displayComisarias.add(c.getNombreComisaria());
                }
                adapterComisarias.notifyDataSetChanged();
            }
        });
    }

    private void save() {
        java.util.Date date;
        Tramite t;
        if (validar()) {
            t = new Tramite();
            date = new java.util.Date();
            try {
                t.setEstadoTramite(false);
                t.setFechaTramite(new Date(date.getTime()));
                t.setHoraTramite(new Time(date.getTime()));
                t.setTipoTramite(tiposTramite.get(sp_tipoTramite.getSelectedItemPosition() - 1));
                t.setComisarias(comisarias.get(sp_comisariaT.getSelectedItemPosition() - 1));
                t.setMotivo_denuncia_policial(edtMotivoDenunciaPolicias.getText().toString());
                if (!edtObservaciones.getText().toString().equals("")) {
                    t.setObservaciones(edtObservaciones.getText().toString());
                } else {
                    t.setObservaciones("Ninguna");
                }
                t.setPolicia(new Policia());
                t.getPolicia().setId(1);
                t.setCodTramite("- - -");
                if (radio_denunciante.isChecked()) {
                    t.setSolicitante(radio_denunciante.getText().toString());
                } else if (radio_denunciado.isChecked()) {
                    t.setSolicitante(radio_denunciado.getText().toString());
                } else {
                    t.setSolicitante(edtEspecificarS.getText().toString());
                }
                t.setCorreo(correoUsuarioTramite.getText().toString());
                t.setTelefono(edtTelefonoTramite.getText().toString());
                final Gson g = new GsonBuilder()
                        .registerTypeAdapter(Date.class, new DateSerializer())
                        .registerTypeAdapter(Time.class, new TimeSerializer())
                        .create();
                t.setUsuario(g.fromJson(PreferenceManager.getDefaultSharedPreferences(this).getString("UsuarioJson", ""), Usuario.class));
                viewModel.save(t).observe(this, response -> {
                    if (response.getRpta() == 1) {
                        successMessage("Tramite registrado con Éxito 📃!!!");
                        finish();
                    } else {
                        errorMessage("Tramite no registrado 😥");
                    }
                });
            } catch (Exception e) {
                warningMessage("Se ha producido un error al intentar armar el trámite:" + e.getMessage());
            }
        } else {
            errorMessage("Por favor, complete todos los campos");
        }

    }

    public void successMessage(String message) {
        new SweetAlertDialog(this,
                SweetAlertDialog.SUCCESS_TYPE).setTitleText("Buen Trabajo!")
                .setContentText(message).show();
    }

    public void errorMessage(String message) {
        new SweetAlertDialog(this,
                SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...").setContentText(message).show();
    }

    public void warningMessage(String message) {
        new SweetAlertDialog(this,
                SweetAlertDialog.WARNING_TYPE).setTitleText("Notificación del Sistema")
                .setContentText(message).setConfirmText("Ok").show();
    }
}