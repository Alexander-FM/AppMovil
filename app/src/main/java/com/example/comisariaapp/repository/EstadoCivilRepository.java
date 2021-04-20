package com.example.comisariaapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.comisariaapp.api.ConfigApi;
import com.example.comisariaapp.api.EstadoCivilApi;
import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.EstadoCivil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EstadoCivilRepository {
    private static EstadoCivilRepository repository;
    private final EstadoCivilApi api;

    private EstadoCivilRepository() {
        this.api = ConfigApi.getEstadoCivilApi();
    }

    public static EstadoCivilRepository getInstance() {
        if (repository == null) {
            repository = new EstadoCivilRepository();
        }
        return repository;
    }

    public LiveData<GenericResponse<List<EstadoCivil>>> list() {
        final MutableLiveData<GenericResponse<List<EstadoCivil>>> mld = new MutableLiveData<>();
        this.api.list().enqueue(new Callback<GenericResponse<List<EstadoCivil>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<EstadoCivil>>> call, Response<GenericResponse<List<EstadoCivil>>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<List<EstadoCivil>>> call, Throwable t) {
                System.out.println("error al obtener los estados civiles:" + t.getMessage());
                t.printStackTrace();
            }
        });
        return mld;
    }
}
