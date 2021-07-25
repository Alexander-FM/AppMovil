package com.example.comisariaapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.comisariaapp.R;
import com.example.comisariaapp.adapter.AgraviadoAdapter;
import com.example.comisariaapp.adapter.DenunciadoAdapter;
import com.example.comisariaapp.communication.AgraviadoCommunication;
import com.example.comisariaapp.communication.DenunciadoCommunication;
import com.example.comisariaapp.entity.service.dto.DenunciaConDetallesDTO;
import com.example.comisariaapp.utils.DenunciaManager;
import com.example.comisariaapp.viewmodel.DenunciaViewModel;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DetalleDenunciaFragment extends Fragment implements AgraviadoCommunication, DenunciadoCommunication {

    private Button btnSaveDenuncia, btnActualizarData;
    private CheckBox chkTerminosyCondiciones;
    private AgraviadoAdapter agraviadosAdapter;
    private DenunciadoAdapter denunciadosAdapter;
    private RecyclerView rcvAgraviados, rcvDenunciados;
    private DenunciaViewModel viewModel;
    DenunciaConDetallesDTO dto;

    public DetalleDenunciaFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detalle_denuncia, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.init(view);
        this.initAdapters();
    }

    private void init(View view) {
        viewModel = new ViewModelProvider(this).get(DenunciaViewModel.class);
        btnSaveDenuncia = view.findViewById(R.id.btnGuardarDenuncia);
        btnSaveDenuncia.setOnClickListener(v -> {
            DenunciaConDetallesDTO dto = DenunciaManager.getDto(getContext());
            this.viewModel.save(dto).observe(getViewLifecycleOwner(), response -> {
                Toast.makeText(getContext(), response.getBody(), Toast.LENGTH_LONG).show();
                if (response.getRpta() == 1) {
                    DenunciaManager.clear(getContext());
                    this.getActivity().finish();
                    this.getActivity().overridePendingTransition(R.anim.down_in, R.anim.down_out);

                }
            });
        });
        this.chkTerminosyCondiciones = view.findViewById(R.id.chkTerminosyCondiciones);
        this.chkTerminosyCondiciones.setOnCheckedChangeListener((buttonView, isChecked) -> btnSaveDenuncia.setEnabled(isChecked));
        this.rcvAgraviados = view.findViewById(R.id.rcvAgraviados);
        this.rcvDenunciados = view.findViewById(R.id.rcvDenunciados);
        this.rcvAgraviados.setLayoutManager(new GridLayoutManager(getContext(), 1));
        this.rcvDenunciados.setLayoutManager(new GridLayoutManager(getContext(), 1));

    }

    private void initAdapters() {
        dto = DenunciaManager.getDto(getContext());
        this.agraviadosAdapter = new AgraviadoAdapter(this, dto.getAgraviados());
        this.rcvAgraviados.setAdapter(this.agraviadosAdapter);

        this.denunciadosAdapter = new DenunciadoAdapter(this, dto.getDenunciados());
        this.rcvDenunciados.setAdapter(this.denunciadosAdapter);
    }

    @Override
    public void deleteA(String numeroIdentificacion) {
        sweetAlertWithButtons(numeroIdentificacion, 'A');
        //Toast.makeText(getContext(), DenunciaManager.removeAgraviado(numeroIdentificacion, getContext()), Toast.LENGTH_SHORT).show();
        this.agraviadosAdapter.updateItems(DenunciaManager.getDto(getContext()).getAgraviados());
    }

    @Override
    public void deleteD(String numeroidentificacion) {
        sweetAlertWithButtons(numeroidentificacion, 'D');
        this.denunciadosAdapter.updateItems(DenunciaManager.getDto(getContext()).getDenunciados());
    }

    public void onResume() {
        super.onResume();
        dto = DenunciaManager.getDto(getContext());
        this.agraviadosAdapter.updateItems(dto.getAgraviados());
        this.denunciadosAdapter.updateItems(dto.getDenunciados());
    }

    //Eliminar con los botones de cancelar y confirmar
    public void sweetAlertWithButtons(String documento, char tipo) {
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE).setTitleText("Estás seguro de eliminar?")
                .setContentText("Una vez eliminado, ya no estará disponible!")
                .setCancelText("No, Cancelar!").setConfirmText("Sí, Eliminar")
                .showCancelButton(true).setCancelClickListener(sDialog -> {
            sDialog.dismissWithAnimation();
            new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE).setTitleText("Petición Cancelada")
                    .setContentText("No se eliminó ningún registro")
                    .show();
        }).setConfirmClickListener(sweetAlertDialog -> {
            sweetAlertDialog.dismissWithAnimation();
            new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE).setTitleText("Operación Finalizada Correctamente")
                    .setContentText(tipo == 'A' ? DenunciaManager.removeAgraviado(documento, getContext()) : DenunciaManager.removeDenunciado(documento, getContext())).show();
            onResume();
        }).show();
    }
}