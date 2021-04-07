package com.example.comisariaapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.InformacionAdicional;
import com.example.comisariaapp.repository.InformacionAdicionalRepository;

import java.util.List;

public class InformacionAdicionalViewModel extends AndroidViewModel {
    private InformacionAdicionalRepository repository;

    public InformacionAdicionalViewModel(@NonNull Application application) {
        super(application);
        this.repository = InformacionAdicionalRepository.getInstance();
    }

    public LiveData<GenericResponse<List<InformacionAdicional>>> list() {
        return repository.list();
    }
}
