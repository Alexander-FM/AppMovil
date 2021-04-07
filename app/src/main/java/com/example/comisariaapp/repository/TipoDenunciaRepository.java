package com.example.comisariaapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.comisariaapp.api.ConfigApi;
import com.example.comisariaapp.api.TipoDenunciaApi;
import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.TipoDenuncia;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TipoDenunciaRepository {
    private static TipoDenunciaRepository repository;
    private TipoDenunciaApi api;

    private TipoDenunciaRepository() {
        this.api = ConfigApi.getTipoDenunciaApi();
    }

    public static TipoDenunciaRepository getInstance() {
        if (repository == null) {
            repository = new TipoDenunciaRepository();
        }
        return repository;
    }

    public LiveData<GenericResponse<List<TipoDenuncia>>> listActivos() {
        final MutableLiveData<GenericResponse<List<TipoDenuncia>>> mld = new MutableLiveData<>();
        this.api.listActivos().enqueue(new Callback<GenericResponse<List<TipoDenuncia>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<TipoDenuncia>>> call, Response<GenericResponse<List<TipoDenuncia>>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<List<TipoDenuncia>>> call, Throwable t) {
                System.out.println("error al obtener los distritos:" + t.getMessage());
            }
        });
        return mld;
    }
}
