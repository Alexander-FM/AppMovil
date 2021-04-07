package com.example.comisariaapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.comisariaapp.entity.ReniecResponse;
import com.example.comisariaapp.repository.ReniecBuscarPersonaRepository;

public class BuscarPersonaViewModel extends AndroidViewModel {
    private final ReniecBuscarPersonaRepository repository;

    public BuscarPersonaViewModel(@NonNull Application application) {
        super(application);
        this.repository = ReniecBuscarPersonaRepository.getInstance();
    }

    public LiveData<ReniecResponse> buscar(String dni){
        return this.repository.buscar(dni);
    }
}
