package com.example.comisariaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comisariaapp.utils.DatePickerFragment;
import com.example.comisariaapp.R;
import com.example.comisariaapp.entity.service.Distrito;
import com.example.comisariaapp.entity.service.EstadoCivil;
import com.example.comisariaapp.entity.service.TipoIdentificacion;
import com.example.comisariaapp.entity.service.Usuario;
import com.example.comisariaapp.viewmodel.*;
import com.google.android.material.textfield.TextInputLayout;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import fr.ganfra.materialspinner.MaterialSpinner;

public class RegistrarUsuarioActivity extends AppCompatActivity {
    private DistritoViewModel distritoViewModel;
    private BuscarPersonaViewModel bpViewModel;
    private EstadoCivilViewModel ecViewModel;
    private TipoIdentificacionViewModel tiViewModel;
    private UsuarioViewModel viewModel;
    public TextInputLayout textInputLayout, text_input_nd_usuario, txtInputApellidoPaternoAgraviado, textInputLayout_apellidoMaternoU,
            txtInputNombresUsuario, fechaNacimientoUsuario, text_input_telefonoUsuario, text_input_direccionUsuario,
            txtInputCorreoElectronicoU, txtInputClaveEmail;
    private EditText edtDoc, edtApellidoPaterno, edtApellidoMaterno, edtNombres, edtFechaNacimiento, edtEmail, edtContraseña, edtTelefono, edtDireccion;
    private Button btnSave;
    private RadioButton rbMasculino, rbFemenino;
    public MaterialSpinner dropdown_tipoIdentificacionU, dropdown_distritoUsuario, dropdown_estadocivilU;
    private final List<Distrito> distritos = new ArrayList<>();
    private final List<EstadoCivil> estadosCiviles = new ArrayList<>();
    private final List<TipoIdentificacion> tiposIdentificacion = new ArrayList<>();
    private ArrayAdapter<String> adapterDistritos, adapterEstadosCiviles, adapterTiposIdentificacion;
    private final List<String> displayDistritos = new ArrayList<>(), displayEstadosCiviles = new ArrayList<>(), displayTiposIdentifiacion = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.atras);
        toolbar.setNavigationOnClickListener(v -> {//Reemplazo con lamba
            this.finish();
            this.overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
        });
        this.init();
        this.initViewModels();
        this.initAdapter();
        this.loadData();
    }

    private void initViewModels() {
        final ViewModelProvider vmp = new ViewModelProvider(this);
        this.distritoViewModel = vmp.get(DistritoViewModel.class);
        this.bpViewModel = vmp.get(BuscarPersonaViewModel.class);
        this.ecViewModel = vmp.get(EstadoCivilViewModel.class);
        this.viewModel = vmp.get(UsuarioViewModel.class);
        this.tiViewModel = vmp.get(TipoIdentificacionViewModel.class);
    }

    private void init() {
        //TEXTLAYOUT DEL MÓDULO DE USUARIO
        text_input_nd_usuario = findViewById(R.id.text_input_nd_usuario);
        txtInputApellidoPaternoAgraviado = findViewById(R.id.txtInputApellidoPaternoAgraviado);
        textInputLayout_apellidoMaternoU = findViewById(R.id.textInputLayout_apellidoMaternoU);
        txtInputNombresUsuario = findViewById(R.id.txtInputNombresUsuario);
        fechaNacimientoUsuario = findViewById(R.id.fechaNacimientoUsuario);
        text_input_telefonoUsuario = findViewById(R.id.text_input_telefonoUsuario);
        text_input_direccionUsuario = findViewById(R.id.text_input_direccionUsuario);
        txtInputCorreoElectronicoU = findViewById(R.id.txtInputCorreoElectronicoU);
        txtInputClaveEmail = findViewById(R.id.txtInputClaveEmail);
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
        //ASIGNANDO LOS CHANGES LISTENER.
        edtFechaNacimiento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                fechaNacimientoUsuario.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        edtDireccion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                text_input_direccionUsuario.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                txtInputCorreoElectronicoU.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        edtContraseña.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                txtInputClaveEmail.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        edtTelefono.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                text_input_telefonoUsuario.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
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
                text_input_nd_usuario.setErrorEnabled(false);
                buscarReniec();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        btnSave.setOnClickListener(v -> {
            if (validar()) {
                bpViewModel.buscar(edtDoc.getText().toString()).observe(this, reniecResponse -> {
                            if (reniecResponse.isSuccess()) {
                                registrarUsuario();
                            } else {
                                Toast.makeText(this, "algunos campos ingresados no son validos,verifíquelos e intentelo de nuevo", Toast.LENGTH_LONG).show();
                            }
                        }
                );
            }
        });
    }

    private void loadData() {
        this.distritoViewModel.list().observe(this, response -> {
            if (response.getRpta() == 1) {
                this.distritos.clear();
                this.distritos.addAll(response.getBody());
                this.displayDistritos.clear();
                for (Distrito d : distritos) {
                    displayDistritos.add(d.getDistrito());
                }
                this.adapterDistritos.notifyDataSetChanged();
            }
        });
        this.ecViewModel.list().observe(this, response -> {
            if (response.getRpta() == 1) {
                this.estadosCiviles.clear();
                this.estadosCiviles.addAll(response.getBody());
                this.displayDistritos.clear();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    this.estadosCiviles.forEach(e -> this.displayEstadosCiviles.add(e.getEstadoCivil()));
                } else {
                    for (EstadoCivil ec : this.estadosCiviles) {
                        this.displayEstadosCiviles.add(ec.getEstadoCivil());
                    }
                }
                this.adapterEstadosCiviles.notifyDataSetChanged();
            }
        });
        this.tiViewModel.list().observe(this, response -> {
            if (response.getRpta() == 1) {
                this.tiposIdentificacion.clear();
                this.tiposIdentificacion.addAll(response.getBody());
                this.displayTiposIdentifiacion.clear();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    this.tiposIdentificacion.forEach(e -> this.displayTiposIdentifiacion.add(e.getTipoIdentificacion()));
                } else {
                    for (TipoIdentificacion td : this.tiposIdentificacion) {
                        this.displayTiposIdentifiacion.add(td.getTipoIdentificacion());
                    }
                }
                this.adapterTiposIdentificacion.notifyDataSetChanged();
            }
        });
    }

    private void initAdapter() {
        this.adapterTiposIdentificacion = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, displayTiposIdentifiacion);
        this.dropdown_tipoIdentificacionU.setAdapter(this.adapterTiposIdentificacion);
        this.adapterDistritos = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, displayDistritos);
        this.adapterDistritos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.dropdown_distritoUsuario.setAdapter(this.adapterDistritos);

        this.adapterEstadosCiviles = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, displayEstadosCiviles);
        this.adapterEstadosCiviles.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.dropdown_estadocivilU.setAdapter(this.adapterEstadosCiviles);
    }

    private void buscarReniec() {
        final int length = this.edtDoc.getText().length();
        switch (length) {
            case 8:
                final String doc = this.edtDoc.getText().toString();

                this.bpViewModel.buscar(doc).observe(RegistrarUsuarioActivity.this, reniecResponse -> {
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

    private boolean validar() {
        boolean retorno = true;
        String direccionCasa, correoUsuario, claveUsuario, telefonoU, fechaNacU, dni;
        direccionCasa = edtDireccion.getText().toString();
        correoUsuario = edtEmail.getText().toString();
        claveUsuario = edtContraseña.getText().toString();
        telefonoU = edtTelefono.getText().toString();
        fechaNacU = edtFechaNacimiento.getText().toString();
        dni = edtDoc.getText().toString();

        if (dni.isEmpty()) {
            text_input_nd_usuario.setError("Ingresa numero de documento");
            retorno = false;
        } else {
            text_input_nd_usuario.setErrorEnabled(false);
        }

        if (direccionCasa.isEmpty()) {
            text_input_direccionUsuario.setError("Ingresa la dirección de tu casa");
            retorno = false;
        } else {
            text_input_direccionUsuario.setErrorEnabled(false);
        }
        if (correoUsuario.isEmpty()) {
            txtInputCorreoElectronicoU.setError("Ingresa tu correo electrónico.");
            retorno = false;
        } else {
            txtInputCorreoElectronicoU.setErrorEnabled(false);
        }
        if (claveUsuario.isEmpty()) {
            txtInputClaveEmail.setError("Ingresa una contraseña segura");
            retorno = false;
        } else {
            txtInputClaveEmail.setErrorEnabled(false);
        }
        if (telefonoU.isEmpty()) {
            text_input_telefonoUsuario.setError("Ingresa tu teléfono");
            retorno = false;
        } else {
            text_input_telefonoUsuario.setErrorEnabled(false);
        }
        if (fechaNacU.isEmpty()) {
            fechaNacimientoUsuario.setError("Ingresa tu fecha de nacimiento");
            retorno = false;
        } else {
            fechaNacimientoUsuario.setErrorEnabled(false);
        }
        if (dropdown_distritoUsuario.getSelectedItemPosition() == 0) {
            TextView errorText2 = (TextView) dropdown_distritoUsuario.getSelectedView();
            errorText2.setError("");
            errorText2.setTextColor(Color.RED);
            errorText2.setText("Seleccione");
        } else {
            dropdown_distritoUsuario.setError(null);
        }
        if (dropdown_estadocivilU.getSelectedItemPosition() == 0) {
            TextView errorText2 = (TextView) dropdown_estadocivilU.getSelectedView();
            errorText2.setError("");
            errorText2.setTextColor(Color.RED);
            errorText2.setText("Seleccione");
        } else {
            dropdown_estadocivilU.setError(null);
        }
        if (dropdown_tipoIdentificacionU.getSelectedItemPosition() == 0) {
            TextView errorText2 = (TextView) dropdown_tipoIdentificacionU.getSelectedView();
            errorText2.setError("");
            errorText2.setTextColor(Color.RED);
            errorText2.setText("Seleccione");
        } else {
            dropdown_tipoIdentificacionU.setError(null);
        }

        return retorno;
    }

    private void registrarUsuario() {
        Usuario u;
        if (validar()) {
            u = new Usuario();
            try {
                u.setNumeroIdentificacion(edtDoc.getText().toString());
                u.setNombres(edtNombres.getText().toString());
                u.setApellidoPaterno(edtApellidoPaterno.getText().toString());
                u.setApellidoMaterno(edtApellidoMaterno.getText().toString());
                String fn = edtFechaNacimiento.getText().toString();
                u.setFechaNacimiento(new Date(new SimpleDateFormat("dd-MM-yyyy").parse(fn).getTime()));
                u.setVigencia(true);
                u.setCorreo(edtEmail.getText().toString());
                u.setContraseña(edtContraseña.getText().toString());
                u.setTipoIdentificacion(new TipoIdentificacion());
                u.getTipoIdentificacion().setId(dropdown_tipoIdentificacionU.getSelectedItemPosition());
                u.setDistrito(distritos.get(dropdown_distritoUsuario.getSelectedItemPosition() - 1));
                u.setEstadoCivil(estadosCiviles.get(dropdown_estadocivilU.getSelectedItemPosition() - 1));
                u.setDireccion(edtDireccion.getText().toString());
                u.setTelefono(edtTelefono.getText().toString());
                //u.getTipoIdentificacion().setId(dropdown_text.getListSelection() + 1);
                u.getTipoIdentificacion().setEstado(true);
                u.getTipoIdentificacion().setTipoIdentificacion("");
                u.setSexo(rbMasculino.isChecked() ? "H" : "M");
                u.setId(0);
                this.viewModel.save(u).observe(this, response -> {
                    Toast.makeText(this, response.getMessage(), Toast.LENGTH_LONG).show();
                    if (response.getRpta() == 1) {
                        successMessage("Registro realizado con éxito 😀,ahora inicia sesión para continuar");
                        Toast.makeText(this, "registro realizado con éxito 😀, ahora inicia sesión para continuar", Toast.LENGTH_SHORT).show();
                        //this.finish();
                    }
                });
                limpiarCampos();
            } catch (Exception e) {
                warningMessage("Se ha producido un error: " + e.getMessage());
            }
        } else {
            errorMessage("Por favor, complete todos los campos del formulario.");
        }

    }

    private void limpiarCampos() {
        dropdown_tipoIdentificacionU.setSelection(0);
        dropdown_estadocivilU.setSelection(0);
        dropdown_distritoUsuario.setSelection(0);
        edtDoc.setText("");
        edtNombres.setText("");
        edtApellidoMaterno.setText("");
        edtApellidoPaterno.setText("");
        edtFechaNacimiento.setText("");
        edtTelefono.setText("");
        edtDireccion.setText("");
        edtEmail.setText("");
        edtContraseña.setText("");
        rbMasculino.setChecked(false);
        rbFemenino.setChecked(false);

    }

    private void showDatePickerDialog() {
        final DatePickerFragment newFragment = DatePickerFragment.newInstance((view, year, month, dayOfMonth) -> {
            final String selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year;
            this.edtFechaNacimiento.setText(selectedDate);
        });

        newFragment.show(this.getSupportFragmentManager(), "Seleccione su fecha de Nacimiento");
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
