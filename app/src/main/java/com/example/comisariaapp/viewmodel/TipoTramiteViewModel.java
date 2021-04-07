package com.example.comisariaapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.TipoTramite;
import com.example.comisariaapp.repository.TipoTramiteRepository;

import java.util.List;

public class TipoTramiteViewModel extends AndroidViewModel {
    private TipoTramiteRepository repository;

    public TipoTramiteViewModel(@NonNull Application application) {
        super(application);
        repository = TipoTramiteRepository.getInstance();
    }

    public LiveData<GenericResponse<List<TipoTramite>>> list() {
        return repository.list();
    }
}
