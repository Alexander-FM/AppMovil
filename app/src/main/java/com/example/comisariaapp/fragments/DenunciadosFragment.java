package com.example.comisariaapp.fragments;

import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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
import com.example.comisariaapp.viewmodel.TipoIdentificacionViewModel;
import com.example.comisariaapp.viewmodel.VinculoParteDenunciadaViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class DenunciadosFragment extends Fragment {
    private InformacionAdicionalViewModel infoAdicViewModel;
    private TipoIdentificacionViewModel tipoViewModel;
    private MaterialSpinner sp_TipoIdentificacionD, sp_InfoAdicionalD, sp_GeneroD;
    private EditText edt_DocD, edt_NombresD, edt_ApellidoPaternoD, edt_ApellidoMaternoD;
    private Button btnSaveD, btnCancelarD;
    private CheckBox unknowPerson;
    private TextInputLayout text_input_nombresD, txtInputApellidoPaternoDenunciado, txtInputApellidoMaternoDenunciado;

    private List<InformacionAdicional> infosAdicional = new ArrayList<>();
    private List<TipoIdentificacion> tiposIdentificacion = new ArrayList<>();
    private ArrayAdapter<String> adapterInfAdic, adapterTipoIden;
    private List<String> displayInfAdic = new ArrayList<>(), displayTipoIdent = new ArrayList<>();
    private String messageToast = " Por favor complete todos los campos 😑";

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
        this.tipoViewModel = vmp.get((TipoIdentificacionViewModel.class));
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

    }

    private void initAdapter() {
        sp_GeneroD.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, new String[]{
                "Masculino",
                "Femenino"
        }));
        adapterInfAdic = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, displayInfAdic);
        adapterInfAdic.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_InfoAdicionalD.setAdapter(adapterInfAdic);

        adapterTipoIden = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, displayTipoIdent);
        adapterTipoIden.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_TipoIdentificacionD.setAdapter(adapterTipoIden);
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
        txtInputApellidoMaternoDenunciado = view.findViewById(R.id.txtInputApellidoMaternoDenunciado);
        txtInputApellidoPaternoDenunciado = view.findViewById(R.id.txtInputApellidoPaternoDenunciado);
        text_input_nombresD = view.findViewById(R.id.text_input_nombresD);
        unknowPerson = view.findViewById(R.id.unknowPerson);
        unknowPerson.setOnCheckedChangeListener((button, isChecked) -> {
            bloquearCampos(!isChecked);
        });
        edt_NombresD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                text_input_nombresD.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                text_input_nombresD.setErrorEnabled(false);
            }
        });
        edt_ApellidoPaternoD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                txtInputApellidoPaternoDenunciado.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                txtInputApellidoPaternoDenunciado.setErrorEnabled(false);
            }
        });
        edt_ApellidoMaternoD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                txtInputApellidoMaternoDenunciado.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                txtInputApellidoMaternoDenunciado.setErrorEnabled(false);
            }
        });
    }

    private void bloquearCampos(boolean b) {
        sp_TipoIdentificacionD.setEnabled(b);
        edt_DocD.setEnabled(b);
    }

    private boolean validar() {
        boolean retorno = true;
        String nombreD, apellidoPaternoD, apellidoMaternoD;
        nombreD = edt_NombresD.getText().toString();
        apellidoPaternoD = edt_ApellidoPaternoD.getText().toString();
        apellidoMaternoD = edt_ApellidoMaternoD.getText().toString();
        if (nombreD.isEmpty() || apellidoPaternoD.isEmpty() || apellidoMaternoD.isEmpty() ||
                sp_TipoIdentificacionD.getSelectedItemPosition() == -1 || sp_InfoAdicionalD.getSelectedItemPosition() == -1 ||
                sp_GeneroD.getSelectedItemPosition() == -1) {
            text_input_nombresD.setError("Ingresa tus nombres completos.");
            txtInputApellidoPaternoDenunciado.setError("Ingresa tu apellido paterno.");
            txtInputApellidoMaternoDenunciado.setError("Ingresa tu apellido materno.");
            ((TextView) sp_TipoIdentificacionD.getSelectedView()).setError("Seleccione");
            ((TextView) sp_GeneroD.getSelectedView()).setError("Seleccione");
            ((TextView) sp_InfoAdicionalD.getSelectedView()).setError("Seleccione");
            retorno = false;
        } else {
            txtInputApellidoPaternoDenunciado.setErrorEnabled(false);
            txtInputApellidoMaternoDenunciado.setErrorEnabled(false);
            text_input_nombresD.setErrorEnabled(false);
            ((TextView) sp_TipoIdentificacionD.getSelectedView()).setError(null);
            ((TextView) sp_GeneroD.getSelectedView()).setError(null);
            ((TextView) sp_InfoAdicionalD.getSelectedView()).setError(null);
        }
        return retorno;
    }

    private void clearCamposDenunciado() {
        edt_NombresD.setText("");
        edt_ApellidoPaternoD.setText("");
        edt_ApellidoMaternoD.setText("");
        edt_DocD.setText("");
        sp_TipoIdentificacionD.setSelection(0);
        sp_InfoAdicionalD.setSelection(0);
        sp_GeneroD.setSelection(0);
        text_input_nombresD.setErrorEnabled(false);
        txtInputApellidoPaternoDenunciado.setErrorEnabled(false);
        txtInputApellidoMaternoDenunciado.setErrorEnabled(false);
        sp_GeneroD.setError(null);
        sp_TipoIdentificacionD.setError(null);
        sp_InfoAdicionalD.setError(null);
    }

    /*edt_NombresD.getText().toString().equals("")
                && edt_ApellidoPaternoD.getText().toString().equals("")
                && edt_ApellidoMaternoD.getText().toString().equals("")*/

    private void guardarDenunciado() {
        Denunciado d;
        if (validar()) {
            d = new Denunciado();
            try {
                d.setTipoIdentificacion(new TipoIdentificacion());
                d.setNumeroIdentificacion(edt_DocD.getText().toString());
                d.setNombres(edt_NombresD.getText().toString());
                d.setApellidoPaterno(edt_ApellidoPaternoD.getText().toString());
                d.setApellidoMaterno(edt_ApellidoMaternoD.getText().toString());
                d.setInformacionAdicional(this.infosAdicional.get(this.sp_InfoAdicionalD.getSelectedItemPosition() - 1));
                d.setTipoIdentificacion(this.tiposIdentificacion.get(this.sp_TipoIdentificacionD.getSelectedItemPosition() - 1));
                d.setSexo(sp_GeneroD.getSelectedItem().toString());
                successMessage(DenunciaManager.addDenunciado(d, getContext()));
                //mostrarToastOk(DenunciaManager.addDenunciado(d, getContext()), getView());
                //Toast.makeText(getContext(), DenunciaManager.addDenunciado(d, getContext()), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                mostrarToast("Error al intentar crear el objeto Denunciado:" + e.getMessage(), getView());
                //Toast.makeText(getContext(), "Error al intentar crear el objeto Denunciado:" + e.getMessage() + " 😥", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            this.clearCamposDenunciado();
        } else {
            mostrarToast(messageToast, getView());
            //Toast.makeText(getContext(), "Por favor complete todos los campos 😑", Toast.LENGTH_LONG).show();
        }
    }

    public void mostrarToast(String texto, View v) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View layouView = layoutInflater.inflate(R.layout.custom_toast, (ViewGroup) v.findViewById(R.id.layout_base_1));
        TextView textView = layouView.findViewById(R.id.textoinfo);
        textView.setText(texto);

        Toast toast = new Toast(getContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layouView);
        toast.show();

    }

    public void mostrarToastOk(String texto, View v) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View layouView = layoutInflater.inflate(R.layout.custom_toast_check, (ViewGroup) v.findViewById(R.id.layout_base_2));
        TextView textView = layouView.findViewById(R.id.textoinfo2);
        textView.setText(texto);

        Toast toast = new Toast(getContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layouView);
        toast.show();

    }

    public void successMessage(String message) {
        new SweetAlertDialog(getContext(),
                SweetAlertDialog.SUCCESS_TYPE).setTitleText("Buen Trabajo!")
                .setContentText(message).show();
    }
}