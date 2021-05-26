package com.example.comisariaapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.TipoIdentificacion;
import com.example.comisariaapp.repository.TipoIdentificacionRepository;

import java.util.List;

public class TipoIdentificacionViewModel extends AndroidViewModel {
    private final TipoIdentificacionRepository repository;

    public TipoIdentificacionViewModel(@NonNull Application application) {
        super(application);
        this.repository = TipoIdentificacionRepository.getInstance();
    }

    public LiveData<GenericResponse<List<TipoIdentificacion>>> list() {
        return this.repository.list();
    }
}
