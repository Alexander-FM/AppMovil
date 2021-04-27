package com.example.comisariaapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.comisariaapp.api.ConfigApi;
import com.example.comisariaapp.api.DenunciaApi;
import com.example.comisariaapp.api.SugerenciaApi;
import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.Sugerencia;
import com.example.comisariaapp.entity.service.dto.DenunciaConDetallesDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SugerenciaRepository {
    private final SugerenciaApi api;
    private static SugerenciaRepository repository;

    private SugerenciaRepository() {
        api = ConfigApi.getSugerenciaApi();
    }

    public static SugerenciaRepository getInstance() {
        if (repository == null) {
            repository = new SugerenciaRepository();
        }
        return repository;
    }

    public LiveData<GenericResponse<Sugerencia>> save(Sugerencia s) {
        MutableLiveData<GenericResponse<Sugerencia>> mld = new MutableLiveData<>();
        this.api.save(s).enqueue(new Callback<GenericResponse<Sugerencia>>() {
            @Override
            public void onResponse(Call<GenericResponse<Sugerencia>> call, Response<GenericResponse<Sugerencia>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<Sugerencia>> call, Throwable t) {
                mld.setValue(new GenericResponse<>());
                mld.getValue().setRpta(0);
                t.printStackTrace();
            }
        });
        return mld;
    }

    public LiveData<GenericResponse<List<Sugerencia>>> devolverSugerenciaByUsu(int idUsu) {
        MutableLiveData<GenericResponse<List<Sugerencia>>> mld = new MutableLiveData<>();
        this.api.devolverComentarioByUser(idUsu).enqueue(new Callback<GenericResponse<List<Sugerencia>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<Sugerencia>>> call, Response<GenericResponse<List<Sugerencia>>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<List<Sugerencia>>> call, Throwable t) {
                mld.setValue(new GenericResponse<>());
                t.printStackTrace();
            }
        });
        return mld;
    }
}
