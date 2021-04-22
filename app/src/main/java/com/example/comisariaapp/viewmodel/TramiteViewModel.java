package com.example.comisariaapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.Denuncia;
import com.example.comisariaapp.entity.service.Tramites;
import com.example.comisariaapp.repository.TramiteRepository;

import java.util.List;

public class TramiteViewModel extends AndroidViewModel {
    private final TramiteRepository repository;

    public TramiteViewModel(@NonNull Application application) {
        super(application);
        repository = TramiteRepository.getInstance();
    }

    public LiveData<GenericResponse<Tramites>> save(Tramites t) {
        return this.repository.save(t);
    }

    public LiveData<GenericResponse<List<Tramites>>> devolverMisTramites(int idUsu) {
        return this.repository.devolverMisTramites(idUsu);
    }

    public LiveData<GenericResponse<Tramites>> consultarTramites(String codTramite, int idUsu) {
        return this.repository.consultarTramites(codTramite, idUsu);
    }
}
