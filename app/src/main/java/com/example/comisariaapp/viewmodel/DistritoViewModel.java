package com.example.comisariaapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.Distrito;
import com.example.comisariaapp.repository.DistritoRepository;

import java.util.List;

public class DistritoViewModel extends AndroidViewModel {
    private final DistritoRepository repository;

    public DistritoViewModel(@NonNull Application application) {
        super(application);
        this.repository = DistritoRepository.getInstance();
    }

    public LiveData<GenericResponse<List<Distrito>>> list() {
        return this.repository.list();
    }

    public LiveData<GenericResponse<List<Distrito>>> listAll() {
        return this.repository.listAll();
    }
}
