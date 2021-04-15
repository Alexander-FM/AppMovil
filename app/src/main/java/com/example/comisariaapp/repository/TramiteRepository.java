package com.example.comisariaapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.comisariaapp.api.ConfigApi;
import com.example.comisariaapp.api.TramiteApi;
import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.Tramites;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TramiteRepository {
    private final TramiteApi api;
    private static TramiteRepository repository;

    private TramiteRepository() {
        api = ConfigApi.getTramiteApi();
    }

    public static TramiteRepository getInstance() {
        if (repository == null) {
            repository = new TramiteRepository();
        }
        return repository;
    }

    public LiveData<GenericResponse<Tramites>> save(Tramites t) {
        MutableLiveData mld = new MutableLiveData();
        this.api.save(t).enqueue(new Callback<GenericResponse<Tramites>>() {
            @Override
            public void onResponse(Call<GenericResponse<Tramites>> call, Response<GenericResponse<Tramites>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<Tramites>> call, Throwable t) {
                System.out.println("error al intentar registrar el Trámite:" + t.getMessage());
            }
        });
        return mld;
    }
}
