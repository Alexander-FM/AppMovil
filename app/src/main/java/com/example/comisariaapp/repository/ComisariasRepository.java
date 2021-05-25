package com.example.comisariaapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.comisariaapp.api.ComisariasApi;
import com.example.comisariaapp.api.ConfigApi;
import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.Comisarias;
import com.example.comisariaapp.entity.service.EstadoCivil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComisariasRepository {
    private static ComisariasRepository repository;
    private final ComisariasApi api;

    public ComisariasRepository() {
        this.api = ConfigApi.getComisariaApi();
    }
    public static ComisariasRepository getInstance() {
        if (repository == null) {
            repository = new ComisariasRepository();
        }
        return repository;
    }

    public LiveData<GenericResponse<List<Comisarias>>> list() {
        final MutableLiveData<GenericResponse<List<Comisarias>>> mld = new MutableLiveData<>();
        this.api.list().enqueue(new Callback<GenericResponse<List<Comisarias>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<Comisarias>>> call, Response<GenericResponse<List<Comisarias>>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<List<Comisarias>>> call, Throwable t) {
                System.out.println("Error al obtener las comisarías :" + t.getMessage());
                t.printStackTrace();
            }
        });
        return mld;
    }
}
