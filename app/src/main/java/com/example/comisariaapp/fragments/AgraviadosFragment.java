package com.example.comisariaapp.fragments;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.comisariaapp.R;
import com.example.comisariaapp.entity.service.Agraviado;
import com.example.comisariaapp.entity.service.Distrito;
import com.example.comisariaapp.entity.service.EstadoCivil;
import com.example.comisariaapp.entity.service.InformacionAdicional;
import com.example.comisariaapp.entity.service.TipoDenuncia;
import com.example.comisariaapp.entity.service.TipoIdentificacion;
import com.example.comisariaapp.entity.service.Usuario;
import com.example.comisariaapp.entity.service.VinculoParteDenunciada;
import com.example.comisariaapp.utils.DatePickerFragment;
import com.example.comisariaapp.utils.DateSerializer;
import com.example.comisariaapp.utils.DenunciaManager;
import com.example.comisariaapp.utils.TimeSerializer;
import com.example.comisariaapp.viewmodel.DistritoViewModel;
import com.example.comisariaapp.viewmodel.EstadoCivilViewModel;
import com.example.comisariaapp.viewmodel.InformacionAdicionalViewModel;
import com.example.comisariaapp.viewmodel.TipoDenunciaViewModel;
import com.example.comisariaapp.viewmodel.VinculoParteDenunciadaViewModel;
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

import fr.ganfra.materialspinner.MaterialSpinner;

public class AgraviadosFragment extends Fragment {

    private DistritoViewModel distritoViewModel;
    private InformacionAdicionalViewModel infoAdicViewModel;

    private CheckBox mismaPersona;
    public MaterialSpinner drop_tipoIdentificacionA, drop_generoA, drop_distritoA, drop_infoAdicionalA, drop_medidaProteccion, drop_Juzgado;
    private EditText edtDocA, edtApellidoPaternoA, edtApellidoMaternoA, edtNombresA, edtFechaNacimientoA, edtCeluarA, edtReferenciaDomicilioReal, edtHechoDenunciar,
            edtFechaEmisionProteccion, edtDetalleProtección;

    private Button btnSaveA, btnCancelarA;

    private List<Distrito> allDistritos = new ArrayList<>();
    private List<InformacionAdicional> infosAdicional = new ArrayList<>();
    private List<EstadoCivil> estadosCiviles = new ArrayList<>();
    private ArrayAdapter<String> adapterAllDistritos, adapterInfAdic;
    private List<String> displayAllDistritos = new ArrayList<>(), displayInfAdic = new ArrayList<>(), displayEstadosCiviles = new ArrayList<>();

    public AgraviadosFragment() {
        // Required empty public constructor
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
    }

    public void initViewModels() {
        ViewModelProvider vmp = new ViewModelProvider(this);
        this.distritoViewModel = vmp.get(DistritoViewModel.class);
        this.infoAdicViewModel = vmp.get(InformacionAdicionalViewModel.class);
    }

    public void initAdapters() {
        drop_tipoIdentificacionA.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, new String[]{
                "Natural",
                "Jurídica"
        }));

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

    private void guardarAgraviado() {
        Agraviado a;
        if (!edtDocA.getText().toString().equals("") && !edtNombresA.getText().toString().equals("") && !edtApellidoPaternoA.getText().toString().equals("") && !edtApellidoMaternoA.getText().toString().equals("")
                && drop_tipoIdentificacionA.getSelectedItemPosition() != 0
                && !edtFechaNacimientoA.getText().toString().equals("") && !edtCeluarA.getText().toString().equals("")
                && !edtReferenciaDomicilioReal.getText().toString().equals("") && !edtHechoDenunciar.getText().toString().equals("") && drop_medidaProteccion.getSelectedItemPosition() != -1) {
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
                int indexDistritoSelected = drop_distritoA.getSelectedItemPosition();
                a.setDistrito(allDistritos.get(indexDistritoSelected - 1));
                a.setSexo(drop_generoA.getSelectedItem().toString());
                a.setTipoIdentificacion(new TipoIdentificacion());
                final int indexti = drop_tipoIdentificacionA.getSelectedItemPosition();
                a.getTipoIdentificacion().setId(indexti);
                a.setEstadoCivil(new EstadoCivil());
                a.getEstadoCivil().setId(1);
                int indexIA = drop_infoAdicionalA.getSelectedItemPosition();
                a.setInformacionAdicional(infosAdicional.get(indexIA - 1));
                if (drop_medidaProteccion.getSelectedItemPosition() == 1) {
                    a.setMedidaProteccion(true);
                    a.setJuzgado(drop_Juzgado.getSelectedItem().toString());
                    a.setFechaEmision(new Date(new SimpleDateFormat("dd-MM-yyyy").parse(edtFechaEmisionProteccion.getText().toString()).getTime()));
                    a.setDetalleProteccion(edtDetalleProtección.getText().toString());
                }
                Toast.makeText(getContext(), DenunciaManager.addAgraviado(a, getContext()), Toast.LENGTH_SHORT).show();
                //El this es para actividades y el getContext es para fragments, etc. - > Papá Cumpa.
            } catch (Exception e) {
                Toast.makeText(getContext(), "Error al intentar crear el objeto Agraviado:" + e.getMessage() + " 😥", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            this.clearCamposAgraviado();
        } else {
            Toast.makeText(getContext(), "Por favor complete todos los campos 😑", Toast.LENGTH_SHORT).show();
        }
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