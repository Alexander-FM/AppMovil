package com.example.comisariaapp.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.comisariaapp.R;
import com.example.comisariaapp.entity.service.Denuncia;
import com.example.comisariaapp.entity.service.Distrito;
import com.example.comisariaapp.entity.service.Policia;
import com.example.comisariaapp.entity.service.TipoDenuncia;
import com.example.comisariaapp.entity.service.Usuario;
import com.example.comisariaapp.entity.service.VinculoParteDenunciada;
import com.example.comisariaapp.utils.DatePickerFragment;
import com.example.comisariaapp.utils.DateSerializer;
import com.example.comisariaapp.utils.DenunciaManager;
import com.example.comisariaapp.utils.TimePickerFragment;
import com.example.comisariaapp.utils.TimeSerializer;
import com.example.comisariaapp.viewmodel.DistritoViewModel;
import com.example.comisariaapp.viewmodel.InformacionAdicionalViewModel;
import com.example.comisariaapp.viewmodel.TipoDenunciaViewModel;
import com.example.comisariaapp.viewmodel.VinculoParteDenunciadaViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class DenunciaFragment extends Fragment {

    private DistritoViewModel distritoViewModel;
    private VinculoParteDenunciadaViewModel vpdViewModel;
    private TipoDenunciaViewModel tdViewModel;

    private MaterialSpinner drop_distritoD, drop_vpd, drop_td;
    private EditText edtFechaHechos, edtLugarHechos, edtReferenciaHechos, edtHoraHechos;

    private Button btnLimpiarDenunciaF, btnGuardarDenunciaF;

    private List<Distrito> distritos = new ArrayList<>();
    private List<VinculoParteDenunciada> vinculos = new ArrayList<>();
    private List<TipoDenuncia> tiposDenuncia = new ArrayList<>();
    private ArrayAdapter<String> adapterDistritos, adapterVinculos, adapterTd;
    private List<String> displayDistritos = new ArrayList<>(), displayVinculos = new ArrayList<>(), displayTd = new ArrayList<>(), displayInfAdic;

    public DenunciaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_denuncia, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        initViewModels();
        initAdapters();
        loadData();
    }

    public void initViewModels() {
        ViewModelProvider vmp = new ViewModelProvider(this);
        this.distritoViewModel = vmp.get(DistritoViewModel.class);
        this.vpdViewModel = vmp.get(VinculoParteDenunciadaViewModel.class);
        this.tdViewModel = vmp.get(TipoDenunciaViewModel.class);
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

        btnGuardarDenunciaF.setOnClickListener(view -> save()); //Guardamos la denuncia
        btnLimpiarDenunciaF.setOnClickListener(view -> limpiarCamposDenuncia());

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
        });
    }

    private void initAdapters() {
        adapterDistritos = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, displayDistritos);
        adapterDistritos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drop_distritoD.setAdapter(adapterDistritos);

        adapterVinculos = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, displayVinculos);
        adapterVinculos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drop_vpd.setAdapter(adapterVinculos);

        adapterTd = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, displayTd);
        adapterTd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drop_td.setAdapter(adapterTd);
    }

    private void save() {
        Denuncia d = new Denuncia();
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());//getPreferences(Context.MODE_PRIVATE);
            Gson g = new GsonBuilder()
                    .registerTypeAdapter(Date.class, new DateSerializer())
                    .registerTypeAdapter(Time.class, new TimeSerializer())
                    .create();
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
            d.setDireccion(this.edtLugarHechos.getText().toString());
            d.setReferenciaDireccion(this.edtReferenciaHechos.getText().toString());
            d.setVinculoParteDenunciada(this.vinculos.get(this.drop_vpd.getSelectedItemPosition() - 1));
            d.setTipoDenuncia(this.tiposDenuncia.get(this.drop_td.getSelectedItemPosition() - 1));
            DenunciaManager.setDenuncia(d, getContext());
            Toast.makeText(getContext(), "Los datos de la denuncia fueron guardados correctamente", Toast.LENGTH_LONG).show();
            limpiarCamposDenuncia();
        } catch (Exception e) {
            Toast.makeText(getContext(), "se ha producido un error al intentar armar la denuncia:" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void limpiarCamposDenuncia(){
        edtFechaHechos.setText("");
        edtHoraHechos.setText("");
        drop_distritoD.setSelection(0);
        drop_td.setSelection(0);
        drop_vpd.setSelection(0);
        edtLugarHechos.setText("");
        edtReferenciaHechos.setText("");
    }

    private boolean validarDatosDenuncia() {
        return !edtFechaHechos.getText().toString().equals("")
                && drop_distritoD.getSelectedItemPosition() != -1
                && !edtLugarHechos.getText().toString().equals("")
                && !edtReferenciaHechos.getText().toString().equals("")
                && drop_vpd.getSelectedItemPosition() != -1
                && drop_td.getSelectedItemPosition() != -1;
    }

}