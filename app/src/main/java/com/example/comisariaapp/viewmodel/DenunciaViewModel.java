package com.example.comisariaapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.dto.DenunciaConDetallesDTO;
import com.example.comisariaapp.repository.DenunciaRepository;

import java.util.List;

public class DenunciaViewModel extends AndroidViewModel {
    private final DenunciaRepository repository;

    public DenunciaViewModel(@NonNull Application application) {
        super(application);
        this.repository = DenunciaRepository.getInstance();
    }

    public LiveData<GenericResponse<String>> save(DenunciaConDetallesDTO dto) {
        return this.repository.save(dto);
    }

    public LiveData<GenericResponse<List<DenunciaConDetallesDTO>>> devolverMisDenuncias(int idUsu) {
        return this.repository.devolverMisDenuncias(idUsu);
    }

    public LiveData<GenericResponse<DenunciaConDetallesDTO>> consultarDenuncias(String cod_denuncia, int idUsu) {
        return this.repository.consultarDenuncias(cod_denuncia, idUsu);
    }
}
