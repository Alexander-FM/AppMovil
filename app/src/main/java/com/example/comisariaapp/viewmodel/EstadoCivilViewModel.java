package com.example.comisariaapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.EstadoCivil;
import com.example.comisariaapp.repository.EstadoCivilRepository;

import java.util.List;

public class EstadoCivilViewModel extends AndroidViewModel {
    private final EstadoCivilRepository repository;

    public EstadoCivilViewModel(@NonNull Application application) {
        super(application);
        repository = EstadoCivilRepository.getInstance();
    }

    public LiveData<GenericResponse<List<EstadoCivil>>> list() {
        return this.repository.list();
    }
}
