package com.example.comisariaapp.fragments;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comisariaapp.R;
import com.example.comisariaapp.entity.service.Agraviado;
import com.example.comisariaapp.entity.service.Distrito;
import com.example.comisariaapp.entity.service.EstadoCivil;
import com.example.comisariaapp.entity.service.InformacionAdicional;
import com.example.comisariaapp.entity.service.TipoIdentificacion;
import com.example.comisariaapp.entity.service.Usuario;
import com.example.comisariaapp.utils.DatePickerFragment;
import com.example.comisariaapp.utils.DateSerializer;
import com.example.comisariaapp.utils.DenunciaManager;
import com.example.comisariaapp.utils.TimeSerializer;
import com.example.comisariaapp.viewmodel.DistritoViewModel;
import com.example.comisariaapp.viewmodel.InformacionAdicionalViewModel;
import com.example.comisariaapp.viewmodel.TipoIdentificacionViewModel;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import fr.ganfra.materialspinner.MaterialSpinner;

public class AgraviadosFragment extends Fragment {
    private TipoIdentificacionViewModel tipoViewModel;
    private DistritoViewModel distritoViewModel;
    private InformacionAdicionalViewModel infoAdicViewModel;

    private CheckBox mismaPersona;
    public MaterialSpinner drop_tipoIdentificacionA, drop_generoA, drop_distritoA, drop_infoAdicionalA, drop_medidaProteccion, drop_Juzgado;
    private EditText edtDocA, edtApellidoPaternoA, edtApellidoMaternoA, edtNombresA, edtFechaNacimientoA, edtCeluarA, edtReferenciaDomicilioReal, edtHechoDenunciar,
            edtFechaEmisionProteccion, edtDetalleProtección;

    private Button btnSaveA, btnCancelarA;
    private TextInputLayout text_input_numero_doc, text_input_nombresA, txtInputApellidoMaternoAgraviado,
            txtInputApellidoPaternoAgraviado, text_input_fechaNacimientoA, text_input_celularA, text_input_direccionA,
            text_input_fechaEmisionP, text_input_detalle_proteccion_juez, text_input_relate_hechos_denuncia;

    private List<Distrito> allDistritos = new ArrayList<>();
    private List<TipoIdentificacion> tiposIdentificacion = new ArrayList<>();
    private List<InformacionAdicional> infosAdicional = new ArrayList<>();
    private List<EstadoCivil> estadosCiviles = new ArrayList<>();
    private ArrayAdapter<String> adapterAllDistritos, adapterInfAdic, adapterTipoIden;
    private List<String> displayAllDistritos = new ArrayList<>(),
            displayInfAdic = new ArrayList<>(),
            displayEstadosCiviles = new ArrayList<>(), displayTipoIdent = new ArrayList<>();

    public AgraviadosFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_agraviados, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        initViewModels();
        initAdapters();
        loadData();
    }

    private void showDatePickerDialog(DatePickerDialog.OnDateSetListener listener) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(listener);
        newFragment.show(getActivity().getSupportFragmentManager(), "Seleccione Fecha Hechos");
    }

    private void init(View view) {
        edtDocA = view.findViewById(R.id.edtNumeroIdentificacionA);
        edtNombresA = view.findViewById(R.id.edt_nombresA);
        edtApellidoPaternoA = view.findViewById(R.id.edt_apellidoPaternoA);
        edtApellidoMaternoA = view.findViewById(R.id.edt_ApellidoMaternoA);
        edtFechaNacimientoA = view.findViewById(R.id.edt_FechaNacA);
        edtFechaNacimientoA.setOnClickListener(v ->
                showDatePickerDialog((vi, year, month, dayOfMonth) -> {
                    final String selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year;
                    edtFechaNacimientoA.setText(selectedDate);
                })
        );
        drop_distritoA = view.findViewById(R.id.dropdown_distritoA);
        drop_generoA = view.findViewById(R.id.dropdown_generoA);
        edtCeluarA = view.findViewById(R.id.edt_celularA);
        edtReferenciaDomicilioReal = view.findViewById(R.id.edt_DireccionA);
        drop_infoAdicionalA = view.findViewById(R.id.sp_infAdicionalA);
        drop_medidaProteccion = view.findViewById(R.id.dropdown_medida_proteccion_A);

        drop_Juzgado = view.findViewById(R.id.dropdown_juzgadoA);
        drop_tipoIdentificacionA = view.findViewById(R.id.sp_tipoIdentificacionA);
        edtFechaEmisionProteccion = view.findViewById(R.id.edtFechaEmisionP);
        edtDetalleProtección = view.findViewById(R.id.edtdetalleproteccion_juez);
        edtHechoDenunciar = view.findViewById(R.id.edtrelateHechosDenuncia);

        this.edtFechaEmisionProteccion.setOnClickListener(v -> showDatePickerDialog((vi, year, month, dayOfMonth) -> {
            final String selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year;
            edtFechaEmisionProteccion.setText(selectedDate);
        }));

        //BOTONES
        btnSaveA = view.findViewById(R.id.btnSaveA);
        btnSaveA.setOnClickListener(v -> guardarAgraviado());
        btnCancelarA = view.findViewById(R.id.btnCancelA);
        btnCancelarA.setOnClickListener(vi -> {
            this.clearCamposAgraviado();
        });

        mismaPersona = view.findViewById(R.id.chkSoyLaMismaPesona);
        mismaPersona.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cambiarEstadoCampos(!isChecked);
        });
        drop_medidaProteccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                asignarCamposProteccion(position == 0);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //Accediendo a los id de los text layout para poder setear el error en caso de que el usuario lo deje vacío.
        text_input_numero_doc = view.findViewById(R.id.text_input_numero_doc);
        text_input_nombresA = view.findViewById(R.id.text_input_nombresA);
        txtInputApellidoMaternoAgraviado = view.findViewById(R.id.txtInputApellidoMaternoAgraviado);
        txtInputApellidoPaternoAgraviado = view.findViewById(R.id.txtInputApellidoPaternoAgraviado);
        text_input_fechaNacimientoA = view.findViewById(R.id.text_input_fechaNacimientoA);
        text_input_celularA = view.findViewById(R.id.text_input_celularA);
        text_input_direccionA = view.findViewById(R.id.text_input_direccionA);
        text_input_fechaEmisionP = view.findViewById(R.id.text_input_fechaEmisionP);
        text_input_detalle_proteccion_juez = view.findViewById(R.id.text_input_detalle_proteccion_juez);
        text_input_relate_hechos_denuncia = view.findViewById(R.id.text_input_relate_hechos_denuncia);
        edtDocA.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                text_input_numero_doc.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtNombresA.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                text_input_nombresA.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtApellidoPaternoA.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                txtInputApellidoPaternoAgraviado.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtApellidoMaternoA.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                txtInputApellidoMaternoAgraviado.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtCeluarA.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                text_input_celularA.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtFechaNacimientoA.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                text_input_fechaNacimientoA.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtReferenciaDomicilioReal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                text_input_direccionA.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtHechoDenunciar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                text_input_relate_hechos_denuncia.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void initViewModels() {
        ViewModelProvider vmp = new ViewModelProvider(this);
        this.distritoViewModel = vmp.get(DistritoViewModel.class);
        this.infoAdicViewModel = vmp.get(InformacionAdicionalViewModel.class);
        this.tipoViewModel = vmp.get((TipoIdentificacionViewModel.class));
    }

    public void initAdapters() {
        adapterTipoIden = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, displayTipoIdent);
        adapterTipoIden.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drop_tipoIdentificacionA.setAdapter(adapterTipoIden);

        adapterAllDistritos = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, displayAllDistritos);
        adapterAllDistritos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drop_distritoA.setAdapter(adapterAllDistritos);

        drop_generoA.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, new String[]{
                "Masculino",
                "Femenino"
        }));

        adapterInfAdic = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, displayInfAdic);
        adapterInfAdic.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drop_infoAdicionalA.setAdapter(adapterInfAdic);

        drop_medidaProteccion.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, new String[]{
                "Sí",
                "No"
        }));

        drop_Juzgado.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, new String[]{
                "Juzgado de Familia"
        }));
    }

    public void loadData() {
        this.distritoViewModel.listAll().observe(getViewLifecycleOwner(), response -> {
            if (response.getRpta() == 1) {
                this.allDistritos.clear();
                this.allDistritos.addAll(response.getBody());
                displayAllDistritos.clear();
                for (Distrito d : allDistritos) {
                    displayAllDistritos.add(d.getDistrito());
                }
                adapterAllDistritos.notifyDataSetChanged();
            }
        });

        this.tipoViewModel.list().observe(getViewLifecycleOwner(), response -> {
            if (response.getRpta() == 1) {
                this.tiposIdentificacion.clear();
                this.tiposIdentificacion.addAll(response.getBody());
                this.displayTipoIdent.clear();
                for (TipoIdentificacion i : this.tiposIdentificacion) {
                    this.displayTipoIdent.add(i.getTipoIdentificacion());
                }
                this.adapterTipoIden.notifyDataSetChanged();
            }
        });

        this.infoAdicViewModel.list().observe(getViewLifecycleOwner(), response -> {
            if (response.getRpta() == 1) {
                this.infosAdicional.clear();
                this.infosAdicional.addAll(response.getBody());
                this.displayInfAdic.clear();
                for (InformacionAdicional i : this.infosAdicional) {
                    this.displayInfAdic.add(i.getNombre());
                }
                this.adapterInfAdic.notifyDataSetChanged();
            }
        });
    }

    private boolean validarCamposAgraviado() {
        boolean retorno = true;
        //Declaro variables de tipo String para los editText
        String numDocA, nombreA, apellidoPaternoA, apellidoMaternoA, fechaNacA, celularA,
                direccionA, fechaEmisionProteccionA, detalleProteccionA, relateDenuncia;
        numDocA = edtDocA.getText().toString();
        nombreA = edtNombresA.getText().toString();
        apellidoPaternoA = edtApellidoPaternoA.getText().toString();
        apellidoMaternoA = edtApellidoMaternoA.getText().toString();
        fechaNacA = edtFechaNacimientoA.getText().toString();
        celularA = edtCeluarA.getText().toString();
        direccionA = edtReferenciaDomicilioReal.getText().toString();
        relateDenuncia = edtHechoDenunciar.getText().toString();
        if (numDocA.isEmpty()) {
            text_input_numero_doc.setError("Introducir núm. doc.");
            retorno = false;
        } else {
            text_input_numero_doc.setErrorEnabled(false);
        }
        if (nombreA.isEmpty()) {
            text_input_nombresA.setError("Introducir Nombre");
            retorno = false;
        } else {
            text_input_nombresA.setErrorEnabled(false);
        }
        if (apellidoPaternoA.isEmpty()) {
            txtInputApellidoPaternoAgraviado.setError("Introducir Apellido Paterno");
            retorno = false;
        } else {
            txtInputApellidoPaternoAgraviado.setErrorEnabled(false);
        }
        if (apellidoMaternoA.isEmpty()) {
            txtInputApellidoMaternoAgraviado.setError("Introducir Apellido Materno");
            retorno = false;
        } else {
            txtInputApellidoMaternoAgraviado.setErrorEnabled(false);
        }
        if (fechaNacA.isEmpty()) {
            text_input_fechaNacimientoA.setError("Introducir Fecha Nacimiento");
            retorno = false;
        } else {
            text_input_fechaNacimientoA.setErrorEnabled(false);
        }
        if (celularA.isEmpty()) {
            text_input_celularA.setError("Introducir Número Telefónico");
            retorno = false;
        } else {
            text_input_celularA.setErrorEnabled(false);
        }
        if (direccionA.isEmpty()) {
            text_input_direccionA.setError("Introducir Dirección De Casa");
            retorno = false;
        } else {
            text_input_direccionA.setErrorEnabled(false);
        }
        if (relateDenuncia.isEmpty()) {
            text_input_relate_hechos_denuncia.setError("Relate los hechos a denunciar");
            retorno = false;
        } else {
            text_input_relate_hechos_denuncia.setErrorEnabled(false);
        }

        ///Validación de spinner

        if (drop_tipoIdentificacionA.getSelectedItemPosition() == 0) {
            TextView errorText = (TextView) drop_tipoIdentificacionA.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Seleccione");
        } else {
            drop_tipoIdentificacionA.setError(null);
        }
        if (drop_distritoA.getSelectedItemPosition() == 0) {
            TextView errorText = (TextView) drop_distritoA.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Seleccione");
        } else {
            drop_distritoA.setError(null);
        }
        if (drop_generoA.getSelectedItemPosition() == 0) {
            TextView errorText = (TextView) drop_generoA.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Seleccione");
        } else {
            drop_generoA.setError(null);
        }
        if (drop_infoAdicionalA.getSelectedItemPosition() == 0) {
            TextView errorText = (TextView) drop_infoAdicionalA.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Seleccione");
        } else {
            drop_infoAdicionalA.setError(null);
        }
        return retorno;
    }

    private void guardarAgraviado() {
        Agraviado a;
        if (validarCamposAgraviado()) {
            a = new Agraviado();
            try {
                a.setNumeroIdentificacion(edtDocA.getText().toString());
                a.setNombres(edtNombresA.getText().toString());
                a.setApellidoPaterno(edtApellidoPaternoA.getText().toString());
                a.setApellidoMaterno(edtApellidoMaternoA.getText().toString());
                a.setFechaNacimiento(new Date(new SimpleDateFormat("dd-MM-yyyy").parse(edtFechaNacimientoA.getText().toString()).getTime()));
                a.setTelefono(edtCeluarA.getText().toString());
                a.setDireccion(edtReferenciaDomicilioReal.getText().toString());
                a.setRhd(edtHechoDenunciar.getText().toString());
                a.setDistrito(this.allDistritos.get(this.drop_distritoA.getSelectedItemPosition() - 1));
                a.setSexo(drop_generoA.getSelectedItem().toString());
                a.setTipoIdentificacion(this.tiposIdentificacion.get(this.drop_tipoIdentificacionA.getSelectedItemPosition() - 1));
                a.setEstadoCivil(new EstadoCivil());
                a.getEstadoCivil().setId(1);
                /*int indexIA = drop_infoAdicionalA.getSelectedItemPosition();
                a.setInformacionAdicional(infosAdicional.get(indexIA - 1));*/
                a.setInformacionAdicional(this.infosAdicional.get(this.drop_infoAdicionalA.getSelectedItemPosition() - 1));
                if (drop_medidaProteccion.getSelectedItemPosition() == 1) {
                    a.setMedidaProteccion(true);
                    a.setJuzgado(drop_Juzgado.getSelectedItem().toString());
                    a.setFechaEmision(new Date(new SimpleDateFormat("dd-MM-yyyy").parse(edtFechaEmisionProteccion.getText().toString()).getTime()));
                    a.setDetalleProteccion(edtDetalleProtección.getText().toString());
                }
                successMessage(DenunciaManager.addAgraviado(a, getContext()));
                //Toast.makeText(getContext(), DenunciaManager.addAgraviado(a, getContext()), Toast.LENGTH_SHORT).show();
                //El this es para actividades y el getContext es para fragments, etc. - > Papá Cumpa.
            } catch (Exception e) {
                warningMessage("Error al intentar crear el objeto Agraviado: " + e.getMessage());
                //Toast.makeText(getContext(), "Error al intentar crear el objeto Agraviado:" + e.getMessage() + " 😥", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            this.clearCamposAgraviado();
        } else {
            errorMessage("Por favor complete todos los campos !");
            //Toast.makeText(getContext(), "Por favor complete todos los campos 😑", Toast.LENGTH_SHORT).show();
        }
    }

    public void successMessage(String message) {
        new SweetAlertDialog(getContext(),
                SweetAlertDialog.SUCCESS_TYPE).setTitleText("Buen Trabajo!")
                .setContentText(message).show();
    }

    public void errorMessage(String message) {
        new SweetAlertDialog(getContext(),
                SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...").setContentText(message).show();
    }

    public void warningMessage(String message) {
        new SweetAlertDialog(getContext(),
                SweetAlertDialog.WARNING_TYPE).setTitleText("Notificación del Sistema")
                .setContentText(message).setConfirmText("Ok").show();
    }

    private void clearCamposAgraviado() {
        //this.mismaPersona.setChecked(false);
        drop_tipoIdentificacionA.setSelection(0);
        edtDocA.setText("");
        edtNombresA.setText("");
        edtApellidoPaternoA.setText("");
        edtApellidoMaternoA.setText("");
        edtFechaNacimientoA.setText("");
        drop_distritoA.setSelection(0);
        drop_generoA.setSelection(0);
        edtCeluarA.setText("");
        edtReferenciaDomicilioReal.setText("");
        edtHechoDenunciar.setText("");

        drop_Juzgado.setSelection(0);
        drop_infoAdicionalA.setSelection(0);
        edtDetalleProtección.setText("");
        drop_medidaProteccion.setSelection(0);
        edtFechaEmisionProteccion.setText("");
        mismaPersona.setChecked(false);
        drop_infoAdicionalA.setError(null);
        drop_distritoA.setError(null);
        drop_tipoIdentificacionA.setError(null);
        drop_medidaProteccion.setError(null);
        drop_Juzgado.setError(null);
        drop_generoA.setError(null);
    }

    private void asignarCamposProteccion(Boolean b) {
        drop_Juzgado.setEnabled(b);
        edtFechaEmisionProteccion.setEnabled(b);
        edtDetalleProtección.setEnabled(b);
        if (b == false) {
            drop_Juzgado.setSelection(0);
            edtDetalleProtección.setText("");
        }
    }

    private void cambiarEstadoCampos(boolean b) {
        drop_tipoIdentificacionA.setEnabled(b);
        edtDocA.setEnabled(b);
        edtNombresA.setEnabled(b);
        edtApellidoPaternoA.setEnabled(b);
        edtApellidoMaternoA.setEnabled(b);
        edtFechaNacimientoA.setEnabled(b);
        edtReferenciaDomicilioReal.setEnabled(b);
        edtCeluarA.setEnabled(b);
        //drop_distritoA.setEnabled(b);
        drop_generoA.setEnabled(b);
        if (b) {
            clearCamposAgraviado();
        } else {
            this.asignarDatosagraviadoSesion();
        }
    }

    private void asignarDatosagraviadoSesion() {
        final Gson g = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .registerTypeAdapter(Time.class, new TimeSerializer())
                .create();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String strU = preferences.getString("UsuarioJson", "");
        if (!strU.equals("")) {
            Usuario us = g.fromJson(strU, new TypeToken<Usuario>() {
            }.getType());
            this.drop_tipoIdentificacionA.setSelection((us.getTipoIdentificacion().getId() == 1 ? 0 : 1) + 1);
            this.edtDocA.setText(us.getNumeroIdentificacion());
            this.edtNombresA.setText(us.getNombres());
            this.edtApellidoPaternoA.setText(us.getApellidoPaterno());
            this.edtApellidoMaternoA.setText(us.getApellidoMaterno());
            String strFechaN;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                LocalDate ldate = Instant.ofEpochMilli(us.getFechaNacimiento().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
                strFechaN = ldate.getDayOfMonth() + "-" + (ldate.getMonthValue() + 1) + "-" + ldate.getYear();
            } else {
                Date d = us.getFechaNacimiento();
                Calendar c = new GregorianCalendar();
                c.setTime(d);
                strFechaN = c.get(Calendar.DAY_OF_MONTH) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR);
            }
            this.edtFechaNacimientoA.setText(strFechaN);
            int indexDistrito = displayAllDistritos.indexOf(us.getDistrito().getDistrito());
            this.drop_distritoA.setSelection(indexDistrito + 1);
            this.drop_generoA.setSelection(us.getSexo().equals("H") ? 1 : 2);
            this.edtReferenciaDomicilioReal.setText(us.getDireccion());
            this.edtCeluarA.setText(us.getTelefono());

        } else {
            Toast.makeText(getContext(), "No se pudo recuperar los datos de la seccion actual 😟,por favor reinice la aplicación y vuelva a intentarlo en unos minutos ⚙⚙⚙", Toast.LENGTH_LONG).show();
        }

    }
}