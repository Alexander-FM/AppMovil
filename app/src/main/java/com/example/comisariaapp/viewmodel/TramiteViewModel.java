package com.example.comisariaapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.Tramite;
import com.example.comisariaapp.repository.TramiteRepository;

public class TramiteViewModel extends AndroidViewModel {
    private final TramiteRepository repository;

    public TramiteViewModel(@NonNull Application application) {
        super(application);
        repository = TramiteRepository.getInstance();
    }

    public LiveData<GenericResponse<Tramite>> save(Tramite t) {
        return this.repository.save(t);
    }
}
