package com.example.comisariaapp.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comisariaapp.R;
import com.example.comisariaapp.activity.SeleccioneUbicacionActivity;
import com.example.comisariaapp.entity.service.Comisarias;
import com.example.comisariaapp.entity.service.Denuncia;
import com.example.comisariaapp.entity.service.Distrito;
import com.example.comisariaapp.entity.service.Policia;
import com.example.comisariaapp.entity.service.TipoDenuncia;
import com.example.comisariaapp.entity.service.Usuario;
import com.example.comisariaapp.entity.service.VinculoParteDenunciada;
import com.example.comisariaapp.entity.service.dto.DenunciaConDetallesDTO;
import com.example.comisariaapp.utils.DatePickerFragment;
import com.example.comisariaapp.utils.DateSerializer;
import com.example.comisariaapp.utils.DenunciaManager;
import com.example.comisariaapp.utils.TimePickerFragment;
import com.example.comisariaapp.utils.TimeSerializer;
import com.example.comisariaapp.viewmodel.ComisariasViewModel;
import com.example.comisariaapp.viewmodel.DistritoViewModel;
import com.example.comisariaapp.viewmodel.TipoDenunciaViewModel;
import com.example.comisariaapp.viewmodel.VinculoParteDenunciadaViewModel;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.FutureTask;

import cn.pedant.SweetAlert.SweetAlertDialog;
import fr.ganfra.materialspinner.MaterialSpinner;

public class DenunciaFragment extends Fragment {

    private DistritoViewModel distritoViewModel;
    private VinculoParteDenunciadaViewModel vpdViewModel;
    private TipoDenunciaViewModel tdViewModel;
    private ComisariasViewModel comisariasViewModel;
    private MaterialSpinner drop_distritoD, drop_vpd, drop_td, drop_Comisarias;
    private EditText edtFechaHechos, edtLugarHechos, edtReferenciaHechos, edtHoraHechos, edtLatitud, edtLongitud;

    private Button btnLimpiarDenunciaF, btnGuardarDenunciaF, btnGoMaps;
    private TextInputLayout text_input_fecha, text_input_hora, text_input_direccion_hechos,
            text_input_referencia_hechos, text_input_latitud, text_input_longitud;

    private List<Distrito> distritos = new ArrayList<>();
    private List<VinculoParteDenunciada> vinculos = new ArrayList<>();
    private List<Comisarias> comisarias = new ArrayList<>();
    private List<TipoDenuncia> tiposDenuncia = new ArrayList<>();
    private ArrayAdapter<String> adapterDistritos, adapterVinculos, adapterTd, adapterComisarias;
    private List<String> displayDistritos = new ArrayList<>(), displayVinculos = new ArrayList<>(),
            displayTd = new ArrayList<>(), displayComisarias = new ArrayList<>();
    private final Gson g = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateSerializer())
            .registerTypeAdapter(Time.class, new TimeSerializer())
            .create();

    public DenunciaFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_denuncia, container, false);
        this.init(v);
        this.initViewModels();
        this.initAdapters();
        this.loadData();
        return v;
    }

    private void getTempData() {
        String d = DenunciaManager.getDenuncia(this.getContext());
        if (d != null) {
            Denuncia de = g.fromJson(d, DenunciaConDetallesDTO.class).getDenuncia();
            Toast.makeText(this.getContext(), "se han encontrado datos de denuncia guardados en memoria", Toast.LENGTH_SHORT).show();
            this.showData(de);

        }
    }

    private int selectDistrito(int idD) {
        int index = 0;
        for (Distrito d : this.distritos) {
            if (d.getId() == idD) {
                return index + 1;
            }
            index++;
        }
        return 0;
    }

    private int selectComisarias(int idC) {
        int index = 0;
        for (Comisarias c : this.comisarias) {
            if (c.getId() == idC) {
                return index + 1;
            }
            index++;
        }
        return 0;
    }

    private int selectVPD(int id) {
        int index = 0;
        for (VinculoParteDenunciada vpd : this.vinculos) {
            if (vpd.getId() == id) {
                return index + 1;
            }
            index++;
        }
        return 0;
    }

    private int selectTD(int id) {
        int index = 0;
        for (TipoDenuncia td : this.tiposDenuncia) {
            if (td.getId() == id) {
                return index + 1;
            }
            index++;
        }
        return 0;
    }

    private void showData(Denuncia d) {
        edtFechaHechos.setText(this.dateToString('D', d.getFechaHechos()));
        edtHoraHechos.setText(this.dateToString('T', d.getHoraHechos()));
        drop_distritoD.setSelection(this.selectDistrito(d.getDistrito().getId()));
        drop_Comisarias.setSelection(this.selectComisarias(d.getComisarias().getId()));
        edtLugarHechos.setText(d.getDireccion());
        edtLongitud.setText(d.getLongitud());
        edtLatitud.setText(d.getLatitud());
        edtReferenciaHechos.setText(d.getReferenciaDireccion());
        drop_vpd.setSelection(this.selectVPD(d.getVinculoParteDenunciada().getId()));
        drop_td.setSelection(this.selectTD(d.getTipoDenuncia().getId()));
    }

    private String dateToString(char type, java.util.Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        if (type == 'D') {
            int day = c.get(Calendar.DAY_OF_MONTH), month = c.get(Calendar.MONTH) + 1, year = c.get(Calendar.YEAR);
            return day + "-" + month + "-" + year;
        } else {
            int hour = c.get(Calendar.HOUR), minute = c.get(Calendar.MINUTE);
            return hour + ":" + minute;
        }

    }

    private void save() {
        Denuncia d;
        if (validarCamposDenuncia()) {
            d = new Denuncia();
            try {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());//getPreferences(Context.MODE_PRIVATE);
                d.setUsuario(g.fromJson(preferences.getString("UsuarioJson", null), Usuario.class));
                d.setCod_denuncia("? ? ?");
                d.setPolicia(new Policia());
                d.getPolicia().setId(1);
                java.util.Date date = new java.util.Date();
                d.setFechaDenuncia(new Date(date.getTime()));
                d.setHoraDenuncia(new Time(date.getTime()));
                d.setFechaHechos(new Date(new SimpleDateFormat("dd-MM-yyyy").parse(this.edtFechaHechos.getText().toString()).getTime()));
                d.setHoraHechos(new Time(new SimpleDateFormat("hh:mm").parse(edtHoraHechos.getText().toString()).getTime()));
                d.setDistrito(this.distritos.get(this.drop_distritoD.getSelectedItemPosition() - 1));
                d.setComisarias(this.comisarias.get(this.drop_Comisarias.getSelectedItemPosition() - 1));
                d.setDireccion(this.edtLugarHechos.getText().toString());
                d.setLongitud(this.edtLongitud.getText().toString());
                d.setLatitud(this.edtLatitud.getText().toString());
                d.setReferenciaDireccion(this.edtReferenciaHechos.getText().toString());
                d.setVinculoParteDenunciada(this.vinculos.get(this.drop_vpd.getSelectedItemPosition() - 1));
                d.setTipoDenuncia(this.tiposDenuncia.get(this.drop_td.getSelectedItemPosition() - 1));
                DenunciaManager.setDenuncia(d, getContext());
                successMessage("Los datos de la denuncia fueron guardados correctamente");
                limpiarCamposDenuncia();
            } catch (Exception e) {
                warningMessage("Se ha producido un error al intentar armar la denuncia:" + e.getMessage());
                //Toast.makeText(getContext(), "se ha producido un error al intentar armar la denuncia:" + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } else {
            errorMessage("Por favor, complete todos los campos");
        }

    }

    public void initViewModels() {
        ViewModelProvider vmp = new ViewModelProvider(this);
        this.distritoViewModel = vmp.get(DistritoViewModel.class);
        this.vpdViewModel = vmp.get(VinculoParteDenunciadaViewModel.class);
        this.tdViewModel = vmp.get(TipoDenunciaViewModel.class);
        this.comisariasViewModel = vmp.get(ComisariasViewModel.class);
    }

    private void showDatePickerDialog(DatePickerDialog.OnDateSetListener listener) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(listener);
        newFragment.show(getActivity().getSupportFragmentManager(), "Seleccione Fecha Hechos");
    }

    private void showTimePickerDialog(TimePickerDialog.OnTimeSetListener listener) {
        TimePickerFragment newFragment = TimePickerFragment.newInstance(listener);
        newFragment.show(getActivity().getSupportFragmentManager(), "Seleccione Hora Hechos");
    }

    public void init(View v) {
        edtLugarHechos = v.findViewById(R.id.edtDireccionHechos);
        edtReferenciaHechos = v.findViewById(R.id.edtreferenciahechos);
        btnLimpiarDenunciaF = v.findViewById(R.id.btnLimpiarDenunciaF);
        btnGuardarDenunciaF = v.findViewById(R.id.btnGuardarDenunciaF);
        text_input_fecha = v.findViewById(R.id.text_input_fecha);
        text_input_hora = v.findViewById(R.id.text_input_hora);
        text_input_direccion_hechos = v.findViewById(R.id.text_input_direccion_hechos);
        text_input_referencia_hechos = v.findViewById(R.id.text_input_referencia_hechos);
        text_input_longitud = v.findViewById(R.id.text_input_longitud);
        text_input_latitud = v.findViewById(R.id.text_input_latitud);
        edtLatitud = v.findViewById(R.id.edtlatitud);
        edtLongitud = v.findViewById(R.id.edtlongitud);
        btnGuardarDenunciaF.setOnClickListener(view -> save()); //Guardamos la denuncia
        btnLimpiarDenunciaF.setOnClickListener(view -> limpiarCamposDenuncia());
        this.btnGoMaps = v.findViewById(R.id.btnGoMaps);
        btnGoMaps.setOnClickListener(v1 -> {
            this.startActivityForResult(new Intent(getActivity(), SeleccioneUbicacionActivity.class), 1);
        });

        this.edtFechaHechos = v.findViewById(R.id.dtpFechaHechos);
        edtFechaHechos.setOnClickListener(vi -> showDatePickerDialog((view, year, month, dayOfMonth) -> {
            final String selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year;
            edtFechaHechos.setText(selectedDate);
        }));

        this.edtHoraHechos = v.findViewById(R.id.dtpHoraHechos);
        edtHoraHechos.setOnClickListener(view -> showTimePickerDialog((timePicker, hours, minutes) -> {
            final String selectedTime = hours + ":" + minutes;
            edtHoraHechos.setText(selectedTime);
        }));

        drop_distritoD = v.findViewById(R.id.sp_distritoDenuncia);
        drop_vpd = v.findViewById(R.id.sp_vinculoParteDenunciada);
        drop_td = v.findViewById(R.id.sp_tipodenuncia);
        drop_Comisarias = v.findViewById(R.id.sp_comisarias);

        edtFechaHechos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                text_input_fecha.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtHoraHechos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                text_input_hora.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtLugarHechos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                text_input_direccion_hechos.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtReferenciaHechos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                text_input_referencia_hechos.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        edtLatitud.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                text_input_latitud.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtLongitud.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                text_input_longitud.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void loadData() {
        this.distritoViewModel.list().observe(getViewLifecycleOwner(), response -> {
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
        this.comisariasViewModel.list().observe(getViewLifecycleOwner(), response -> {
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
        this.vpdViewModel.list().observe(getViewLifecycleOwner(), response -> {
            if (response.getRpta() == 1) {
                this.vinculos.clear();
                this.vinculos.addAll(response.getBody());
                displayVinculos.clear();
                for (VinculoParteDenunciada v : vinculos) {
                    displayVinculos.add(v.getNombre());
                }
                adapterVinculos.notifyDataSetChanged();
            }
        });
        this.tdViewModel.listActivos().observe(getViewLifecycleOwner(), response -> {
            if (response.getRpta() == 1) {
                this.tiposDenuncia.clear();
                this.tiposDenuncia.addAll(response.getBody());
                displayTd.clear();
                for (TipoDenuncia t : tiposDenuncia) {
                    displayTd.add(t.getTipoDenuncia());
                }
                adapterTd.notifyDataSetChanged();
            }
            new FutureTask<Object>(() -> {
                this.getTempData();
                return null;
            }).run();
        });

    }

    private void initAdapters() {
        adapterDistritos = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, displayDistritos);
        adapterDistritos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drop_distritoD.setAdapter(adapterDistritos);

        adapterComisarias = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, displayComisarias);
        adapterComisarias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drop_Comisarias.setAdapter(adapterComisarias);

        adapterVinculos = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, displayVinculos);
        adapterVinculos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drop_vpd.setAdapter(adapterVinculos);

        adapterTd = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, displayTd);
        adapterTd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drop_td.setAdapter(adapterTd);
    }

    private boolean validarCamposDenuncia() {
        boolean retorno = true;
        String fechaHechos, horaDenuncia, lugar, referencialugar, latitud, longitud,
                sp_distrito, sp_comisarias, sp_vinculoPD, sp_tipoDenun;
        fechaHechos = edtFechaHechos.getText().toString();
        horaDenuncia = edtHoraHechos.getText().toString();
        lugar = edtLugarHechos.getText().toString();
        referencialugar = edtReferenciaHechos.getText().toString();
        latitud = edtLatitud.getText().toString();
        longitud = edtLongitud.getText().toString();
        if (fechaHechos.isEmpty()) {
            text_input_fecha.setError("Ingrese la fecha");
            retorno = false;
        } else {
            text_input_fecha.setErrorEnabled(false);
        }
        if (horaDenuncia.isEmpty()) {
            text_input_hora.setError("Ingrese la hora");
            retorno = false;
        } else {
            text_input_hora.setErrorEnabled(false);
        }
        if (lugar.isEmpty()) {
            text_input_direccion_hechos.setError("Ingrese el lugar de los hechos");
            retorno = false;
        } else {
            text_input_direccion_hechos.setErrorEnabled(false);
        }
        if (referencialugar.isEmpty()) {
            text_input_referencia_hechos.setError("Ingrese una referencia al lugar de los hechos");
            retorno = false;
        } else {
            text_input_referencia_hechos.setErrorEnabled(false);
        }
        if (latitud.isEmpty()) {
            text_input_latitud.setError("Latitud desconocida");
            retorno = false;
        } else {
            text_input_latitud.setErrorEnabled(false);
        }
        if (longitud.isEmpty()) {
            text_input_longitud.setError("Longitud desconocida");
            retorno = false;
        } else {
            text_input_longitud.setErrorEnabled(false);
        }

        //VALIDACIONES DE LOS SPINNERS
        if (drop_distritoD.getSelectedItemPosition() == 0) {
            TextView errorText = (TextView) drop_distritoD.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Seleccione");
        } else {
            drop_distritoD.setError(null);
        }
        if (drop_Comisarias.getSelectedItemPosition() == 0) {
            TextView errorText1 = (TextView) drop_Comisarias.getSelectedView();
            errorText1.setError("");
            errorText1.setTextColor(Color.RED);
            errorText1.setText("Seleccione");
        } else {
            drop_Comisarias.setError(null);
        }
        if (drop_vpd.getSelectedItemPosition() == 0) {
            TextView errorText2 = (TextView) drop_vpd.getSelectedView();
            errorText2.setError("");
            errorText2.setTextColor(Color.RED);
            errorText2.setText("Seleccione");
        } else {
            drop_vpd.setError(null);
        }
        if (drop_td.getSelectedItemPosition() == 0) {
            TextView errorText3 = (TextView) drop_td.getSelectedView();
            errorText3.setError("");
            errorText3.setTextColor(Color.RED);
            errorText3.setText("Seleccione");
        } else {
            drop_td.setError(null);
        }
        return retorno;
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

    public void limpiarCamposDenuncia() {
        edtFechaHechos.setText("");
        edtHoraHechos.setText("");
        drop_distritoD.setSelection(0);
        drop_Comisarias.setSelection(0);
        drop_td.setSelection(0);
        drop_vpd.setSelection(0);
        edtLugarHechos.setText("");
        edtReferenciaHechos.setText("");
        drop_distritoD.setError(null);
        drop_Comisarias.setError(null);
        drop_td.setError(null);
        drop_vpd.setError(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        switch (resultCode) {
            case AppCompatActivity.RESULT_OK:
                edtLatitud.setText(Double.toString(data.getDoubleExtra("latitud", 0)));
                edtLongitud.setText(Double.toString(data.getDoubleExtra("longitud", 0)));
                edtLugarHechos.setText(data.getStringExtra("address"));
                break;
            case AppCompatActivity.RESULT_CANCELED:
                new SweetAlertDialog(getContext(),
                        SweetAlertDialog.ERROR_TYPE).setTitleText("Has cancelado la operación").show();
                break;
            case 2:
                new SweetAlertDialog(getContext(),
                        SweetAlertDialog.ERROR_TYPE).setTitleText("No se puede realizar la operación").setContentText("se requiere que aceptes el permiso de ubicación para que el mapa georeferencial funcione").show();
                break;
            case 3:
                Toast.makeText(this.getContext(), "Aplicando Cambios . . . ", Toast.LENGTH_SHORT).show();
                this.startActivityForResult(new Intent(getActivity(), SeleccioneUbicacionActivity.class), 1);
                break;

        }
    }
}