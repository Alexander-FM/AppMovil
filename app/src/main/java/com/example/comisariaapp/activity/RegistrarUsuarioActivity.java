package com.example.comisariaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.comisariaapp.DatePickerFragment;
import com.example.comisariaapp.R;
import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.Distrito;
import com.example.comisariaapp.entity.service.EstadoCivil;
import com.example.comisariaapp.entity.service.TipoIdentificacion;
import com.example.comisariaapp.entity.service.Usuario;
import com.example.comisariaapp.viewmodel.*;
import com.google.android.material.textfield.TextInputLayout;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class RegistrarUsuarioActivity extends AppCompatActivity {
    private BuscarPersonaViewModel bpViewModel;
    private UsuarioViewModel viewModel;
    public TextInputLayout textInputLayout;
    public AutoCompleteTextView dropdown_text;
    private EditText edtDoc, edtApellidoPaterno, edtApellidoMaterno, edtNombres, edtFechaNacimiento, edtEmail, edtContraseña;
    private Button btnSave;
    private RadioButton rbMasculino, rbFemenino;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
        ViewModelProvider vmp = new ViewModelProvider(this);
        this.bpViewModel = vmp.get(BuscarPersonaViewModel.class);
        this.viewModel = vmp.get(UsuarioViewModel.class);
        init();
    }

    private void init() {
        textInputLayout = findViewById(R.id.text_input_layout);
        dropdown_text = findViewById(R.id.dropdown_text);

        edtDoc = findViewById(R.id.edtNroDocIdentidad);
        edtApellidoPaterno = findViewById(R.id.edtApellidoPaterno);
        edtApellidoMaterno = findViewById(R.id.edtApellidoMaterno);
        edtNombres = findViewById(R.id.edtNombres);
        edtFechaNacimiento = findViewById(R.id.dtpFechaNacimiento);
        edtEmail = findViewById(R.id.edtEmail);
        edtContraseña = findViewById(R.id.edtPassword);

        rbMasculino = findViewById(R.id.radio_masculino);
        rbFemenino = findViewById(R.id.radio_femenino);
        btnSave = findViewById(R.id.btnSave);
        edtFechaNacimiento.setOnClickListener(v -> showDatePickerDialog());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.dropdown_item, new String[]{
                "Natural",
                "Jurídica"
        });

        dropdown_text.setAdapter(adapter);

        dropdown_text.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

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
            u.setEmail(edtEmail.getText().toString());
            u.setContrasenia(edtContraseña.getText().toString());
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
