package com.example.comisariaapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.TipoDenuncia;
import com.example.comisariaapp.repository.TipoDenunciaRepository;

import java.util.List;

public class TipoDenunciaViewModel extends AndroidViewModel {
    private TipoDenunciaRepository repository;

    public TipoDenunciaViewModel(@NonNull Application application) {
        super(application);
        repository = TipoDenunciaRepository.getInstance();
    }

    public LiveData<GenericResponse<List<TipoDenuncia>>> listActivos() {
        return this.repository.listActivos();
    }
}
