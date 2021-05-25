package com.example.comisariaapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.Comisarias;
import com.example.comisariaapp.entity.service.EstadoCivil;
import com.example.comisariaapp.repository.ComisariasRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ComisariasViewModel extends AndroidViewModel {
    private final ComisariasRepository repository;

    public ComisariasViewModel(@NonNull Application application) {
        super(application);
        this.repository = ComisariasRepository.getInstance();
    }

    public LiveData<GenericResponse<List<Comisarias>>> list() {
        return this.repository.list();
    }
}
