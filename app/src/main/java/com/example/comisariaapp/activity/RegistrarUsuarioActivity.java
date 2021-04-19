package com.example.comisariaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.comisariaapp.utils.DatePickerFragment;
import com.example.comisariaapp.R;
import com.example.comisariaapp.entity.service.Distrito;
import com.example.comisariaapp.entity.service.EstadoCivil;
import com.example.comisariaapp.entity.service.TipoIdentificacion;
import com.example.comisariaapp.entity.service.Usuario;
import com.example.comisariaapp.viewmodel.*;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class RegistrarUsuarioActivity extends AppCompatActivity {
    private DistritoViewModel distritoViewModel;
    private BuscarPersonaViewModel bpViewModel;
    private UsuarioViewModel viewModel;
    public TextInputLayout textInputLayout;
    private EditText edtDoc, edtApellidoPaterno, edtApellidoMaterno, edtNombres, edtFechaNacimiento, edtEmail, edtContraseña, edtTelefono, edtDireccion;
    private Button btnSave;
    private RadioButton rbMasculino, rbFemenino;
    public MaterialSpinner dropdown_tipoIdentificacionU, dropdown_distritoUsuario, dropdown_estadocivilU;
    private List<Distrito> distritos = new ArrayList<>();
    private ArrayAdapter<String> adapterDistritos;
    private List<String> displayDistritos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.atras);
        toolbar.setNavigationOnClickListener(v -> {//Reemplazo con lamba
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
        });
        ViewModelProvider vmp = new ViewModelProvider(this);
        this.distritoViewModel = vmp.get(DistritoViewModel.class);
        this.bpViewModel = vmp.get(BuscarPersonaViewModel.class);
        this.viewModel = vmp.get(UsuarioViewModel.class);
        init();
        initAdapter();
        loadData();
    }

    private void init() {
        //INPUTS DEL MÓDULOS DE USUARIOS
        edtDoc = findViewById(R.id.edtNroDocIdentidad);
        edtApellidoPaterno = findViewById(R.id.edtApellidoPaterno);
        edtApellidoMaterno = findViewById(R.id.edtApellidoMaterno);
        edtNombres = findViewById(R.id.edtNombres);
        edtFechaNacimiento = findViewById(R.id.dtpFechaNacimiento);
        edtEmail = findViewById(R.id.edtEmail);
        edtContraseña = findViewById(R.id.edtPassword);
        edtTelefono = findViewById(R.id.edtTelefonoUsuario);
        edtDireccion = findViewById(R.id.edtDireccionUsuario);
        //MATERIAL SPINNER DEL MÓDULO DE USUARIOS
        dropdown_tipoIdentificacionU = findViewById(R.id.dropdown_tipoIdentificacionU);
        dropdown_distritoUsuario = findViewById(R.id.dropdown_distritoUsuario);
        dropdown_estadocivilU = findViewById(R.id.dropdown_estadocivilU);
        //RADIO BUTTONS DEL MÓDULO DE USUARIOS
        rbMasculino = findViewById(R.id.radio_masculino);
        rbFemenino = findViewById(R.id.radio_femenino);
        //BOTONES DEL MÓDULO DE USUARIOS
        btnSave = findViewById(R.id.btnSave);
        edtFechaNacimiento.setOnClickListener(v -> showDatePickerDialog());


        edtDoc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buscarReniec();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        btnSave.setOnClickListener(v -> {
            bpViewModel.buscar(edtDoc.getText().toString()).observe(this, reniecResponse -> {
                        if (reniecResponse.isSuccess()) {
                            registrarUsuario();
                        } else {
                            Toast.makeText(this, "algunos campos ingresados no son validos,verifíquelos e intentelo de nuevo", Toast.LENGTH_LONG).show();
                        }
                    }
            );

        });
    }

    private void loadData() {
        this.distritoViewModel.list().observe(this, response -> {
            if (response.getRpta() == 1) {
                this.distritos.clear();
                this.distritos.addAll(response.getBody());
                displayDistritos.clear();
                for (Distrito d : distritos) {
                    displayDistritos.add(d.getDistrito());
                }
                adapterDistritos.notifyDataSetChanged();
            }
        });
    }

    private void initAdapter() {
        adapterDistritos = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, displayDistritos);
        adapterDistritos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown_distritoUsuario.setAdapter(adapterDistritos);

        dropdown_tipoIdentificacionU.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{
                "Natural",
                "Jurídica"
        }));
    }

    private void buscarReniec() {
        int length = edtDoc.getText().length();
        switch (length) {
            case 8:
                String doc = edtDoc.getText().toString();

                bpViewModel.buscar(doc).observe(RegistrarUsuarioActivity.this, reniecResponse -> {
                    if (reniecResponse.isSuccess()) {
                        edtApellidoPaterno.setText(reniecResponse.getData().getApellido_paterno());
                        edtApellidoMaterno.setText(reniecResponse.getData().getApellido_materno());
                        edtNombres.setText(reniecResponse.getData().getNombres());
                        if (reniecResponse.getData().getFecha_nacimiento() != null) {
                            edtFechaNacimiento.setText(reniecResponse.getData().getFecha_nacimiento());
                        }
                    }

                });
                break;
            case 11:
                break;
            default:
                edtNombres.setText("");
                edtApellidoPaterno.setText("");
                edtApellidoMaterno.setText("");
                edtFechaNacimiento.setText("");
                break;
        }
    }

    private void registrarUsuario() {
        try {
            final Usuario u = new Usuario();
            u.setNumeroIdentificacion(edtDoc.getText().toString());
            u.setNombres(edtNombres.getText().toString());
            u.setApellidoPaterno(edtApellidoPaterno.getText().toString());
            u.setApellidoMaterno(edtApellidoMaterno.getText().toString());
            String fn = edtFechaNacimiento.getText().toString();
            u.setFechaNacimiento(new SimpleDateFormat("dd-MM-yyyy").parse(fn));
            u.setVigencia(true);
            u.setCorreo(edtEmail.getText().toString());
            u.setContraseña(edtContraseña.getText().toString());
            u.setTipoIdentificacion(new TipoIdentificacion());
            u.getTipoIdentificacion().setId(1);
            u.setDistrito(new Distrito());
            u.getDistrito().setId(1);
            u.setEstadoCivil(new EstadoCivil());
            u.getEstadoCivil().setId(2);
            u.getDistrito().setId(1);
            u.setDireccion("No existe información");
            u.setTelefono("968458123");
            //u.getTipoIdentificacion().setId(dropdown_text.getListSelection() + 1);
            u.getTipoIdentificacion().setEstado(true);
            u.getTipoIdentificacion().setTipoIdentificacion("");
            u.setSexo(rbMasculino.isChecked() ? "H" : "M");
            u.setId(0);
            this.viewModel.save(u).observe(this, response -> {
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_LONG).show();
                if (response.getRpta() == 1) {
                    Toast.makeText(this, "registro realizado con éxito 😀,ahora inicia sesión para continuar", Toast.LENGTH_SHORT).show();
                    this.finish();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "se ha producido un error:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance((view, year, month, dayOfMonth) -> {
            final String selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year;
            edtFechaNacimiento.setText(selectedDate);
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}
