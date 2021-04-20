package com.example.comisariaapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.comisariaapp.api.ConfigApi;
import com.example.comisariaapp.api.DistritoApi;
import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.Distrito;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DistritoRepository {
    private static DistritoRepository repository;
    private final DistritoApi api;

    private DistritoRepository() {
        this.api = ConfigApi.getDistritoApi();
    }

    public static DistritoRepository getInstance() {
        if (repository == null) {
            repository = new DistritoRepository();
        }
        return repository;
    }

    public LiveData<GenericResponse<List<Distrito>>> list() {
        final MutableLiveData<GenericResponse<List<Distrito>>> mld = new MutableLiveData<>();
        this.api.list().enqueue(new Callback<GenericResponse<List<Distrito>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<Distrito>>> call, Response<GenericResponse<List<Distrito>>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<List<Distrito>>> call, Throwable t) {
                System.out.println("error al obtener los distritos:" + t.getMessage());
            }
        });
        return mld;
    }
    public LiveData<GenericResponse<List<Distrito>>> listAll() {
        final MutableLiveData<GenericResponse<List<Distrito>>> mld = new MutableLiveData<>();
        this.api.listAll().enqueue(new Callback<GenericResponse<List<Distrito>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<Distrito>>> call, Response<GenericResponse<List<Distrito>>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<List<Distrito>>> call, Throwable t) {
                System.out.println("error al obtener los distritos:" + t.getMessage());
            }
        });
        return mld;
    }
}
