package com.example.comisariaapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.comisariaapp.api.ConfigApi;
import com.example.comisariaapp.api.TipoTramiteApi;
import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.TipoTramite;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TipoTramiteRepository {
    private static TipoTramiteRepository repository;
    private final TipoTramiteApi api;

    private TipoTramiteRepository() {
        api = ConfigApi.getTTApi();
    }

    public static TipoTramiteRepository getInstance() {
        if (repository == null) {
            repository = new TipoTramiteRepository();
        }
        return repository;
    }

    public LiveData<GenericResponse<List<TipoTramite>>> list() {
        MutableLiveData<GenericResponse<List<TipoTramite>>> mld = new MutableLiveData<>();
        this.api.list().enqueue(new Callback<GenericResponse<List<TipoTramite>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<TipoTramite>>> call, Response<GenericResponse<List<TipoTramite>>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<List<TipoTramite>>> call, Throwable t) {
                System.out.println("No se pudo listar los tipos de Trámite:" + t.getMessage());
            }
        });
        return mld;
    }
}
