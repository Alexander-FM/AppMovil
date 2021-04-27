package com.example.comisariaapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.Sugerencia;
import com.example.comisariaapp.entity.service.dto.DenunciaConDetallesDTO;
import com.example.comisariaapp.repository.SugerenciaRepository;

import java.util.List;

public class SugerenciaViewModel extends AndroidViewModel {
    private final SugerenciaRepository repository;

    public SugerenciaViewModel(@NonNull Application application) {
        super(application);
        repository = SugerenciaRepository.getInstance();
    }

    public LiveData<GenericResponse<Sugerencia>> save(Sugerencia s) {
        return this.repository.save(s);
    }

    public LiveData<GenericResponse<List<Sugerencia>>> devolverMisComentarioByUsu(int idUsu) {
        return this.repository.devolverSugerenciaByUsu(idUsu);
    }

}
