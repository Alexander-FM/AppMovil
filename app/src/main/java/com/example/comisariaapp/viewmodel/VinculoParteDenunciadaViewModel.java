package com.example.comisariaapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.VinculoParteDenunciada;
import com.example.comisariaapp.repository.VinculoParteDenunciadaRepository;

import java.util.List;

public class VinculoParteDenunciadaViewModel extends AndroidViewModel {
    private final VinculoParteDenunciadaRepository repository;

    public VinculoParteDenunciadaViewModel(@NonNull Application application) {
        super(application);
        this.repository = VinculoParteDenunciadaRepository.getInstance();
    }

    public LiveData<GenericResponse<List<VinculoParteDenunciada>>> list() {
        return this.repository.list();
    }
}
