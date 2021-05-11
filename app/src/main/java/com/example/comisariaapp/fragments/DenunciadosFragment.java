package com.example.comisariaapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.comisariaapp.R;
import com.example.comisariaapp.entity.service.Agraviado;
import com.example.comisariaapp.entity.service.Denunciado;
import com.example.comisariaapp.entity.service.Distrito;
import com.example.comisariaapp.entity.service.EstadoCivil;
import com.example.comisariaapp.entity.service.InformacionAdicional;
import com.example.comisariaapp.entity.service.TipoDenuncia;
import com.example.comisariaapp.entity.service.TipoIdentificacion;
import com.example.comisariaapp.entity.service.VinculoParteDenunciada;
import com.example.comisariaapp.utils.DenunciaManager;
import com.example.comisariaapp.viewmodel.DistritoViewModel;
import com.example.comisariaapp.viewmodel.EstadoCivilViewModel;
import com.example.comisariaapp.viewmodel.InformacionAdicionalViewModel;
import com.example.comisariaapp.viewmodel.TipoDenunciaViewModel;
import com.example.comisariaapp.viewmodel.VinculoParteDenunciadaViewModel;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class DenunciadosFragment extends Fragment {
    private InformacionAdicionalViewModel infoAdicViewModel;
    private MaterialSpinner sp_TipoIdentificacionD, sp_InfoAdicionalD, sp_GeneroD;
    private EditText edt_DocD, edt_NombresD, edt_ApellidoPaternoD, edt_ApellidoMaternoD;
    private Button btnSaveD, btnCancelarD;

    private List<InformacionAdicional> infosAdicional = new ArrayList<>();
    private ArrayAdapter<String> adapterInfAdic;
    private List<String> displayInfAdic = new ArrayList<>();

    public DenunciadosFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_denunciados, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        initViewModels();
        initAdapter();
        loadData();
    }

    private void initViewModels() {
        ViewModelProvider vmp = new ViewModelProvider(this);
        this.infoAdicViewModel = vmp.get(InformacionAdicionalViewModel.class);
    }

    private void loadData() {
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

    private void initAdapter() {
        sp_TipoIdentificacionD.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, new String[]{
                "Natural",
                "Jurídica"
        }));
        sp_GeneroD.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, new String[]{
                "Masculino",
                "Femenino"
        }));
        adapterInfAdic = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, displayInfAdic);
        adapterInfAdic.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_InfoAdicionalD.setAdapter(adapterInfAdic);
    }

    private void init(View view) {
        edt_DocD = view.findViewById(R.id.edtNumeroIdentificacionD);
        edt_NombresD = view.findViewById(R.id.edtnombreD);
        edt_ApellidoPaternoD = view.findViewById(R.id.edt_ApellidoPaternoD);
        edt_ApellidoMaternoD = view.findViewById(R.id.edt_ApellidoMaternoD);
        sp_TipoIdentificacionD = view.findViewById(R.id.sp_tipoIdentificacionD);
        sp_InfoAdicionalD = view.findViewById(R.id.sp_infAdicionalD);
        sp_GeneroD = view.findViewById(R.id.sp_generoD);
        btnSaveD = view.findViewById(R.id.btnSaveDenunciado);
        btnSaveD.setOnClickListener(v -> guardarDenunciado());
        btnCancelarD = view.findViewById(R.id.btnCancelD);
        btnCancelarD.setOnClickListener(vi -> {
            this.clearCamposDenunciado();
        });
    }

    private void clearCamposDenunciado() {
        edt_NombresD.setText("");
        edt_ApellidoPaternoD.setText("");
        edt_ApellidoMaternoD.setText("");
        edt_DocD.setText("");
        sp_TipoIdentificacionD.setSelection(0);
        sp_InfoAdicionalD.setSelection(0);
        sp_GeneroD.setSelection(0);
    }

    private void guardarDenunciado() {
        Denunciado d;
        if (!edt_DocD.getText().toString().equals("")
                && !edt_NombresD.getText().toString().equals("")
                && !edt_ApellidoPaternoD.getText().toString().equals("")
                && !edt_ApellidoMaternoD.getText().toString().equals("")) {
            d = new Denunciado();
            try {
                d.setTipoIdentificacion(new TipoIdentificacion());
                d.getTipoIdentificacion().setId(sp_TipoIdentificacionD.getSelectedItemPosition() - 1);
                d.getTipoIdentificacion().setId(1);
                d.setNumeroIdentificacion(edt_DocD.getText().toString());
                d.setNombres(edt_NombresD.getText().toString());
                d.setApellidoPaterno(edt_ApellidoPaternoD.getText().toString());
                d.setApellidoMaterno(edt_ApellidoMaternoD.getText().toString());
                int indexIA = sp_InfoAdicionalD.getSelectedItemPosition() - 1;
                d.setInformacionAdicional(infosAdicional.get(indexIA));
                d.setSexo(sp_GeneroD.getSelectedItem().toString());
                Toast.makeText(getContext(), DenunciaManager.addDenunciado(d, getContext()), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getContext(), "Error al intentar crear el objeto Denunciado:" + e.getMessage() + " 😥", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            this.clearCamposDenunciado();
        } else {
            Toast.makeText(getContext(), "Por favor complete todos los campos 😑", Toast.LENGTH_LONG).show();
        }
    }
}