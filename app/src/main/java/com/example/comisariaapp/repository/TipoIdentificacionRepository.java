package com.example.comisariaapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.comisariaapp.api.ComisariasApi;
import com.example.comisariaapp.api.ConfigApi;
import com.example.comisariaapp.api.TipoIdentificacionApi;
import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.Comisarias;
import com.example.comisariaapp.entity.service.TipoIdentificacion;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TipoIdentificacionRepository {
    private static TipoIdentificacionRepository repository;
    private final TipoIdentificacionApi api;

    public TipoIdentificacionRepository() {
        this.api = ConfigApi.getTipoIdentificacionApi();
    }
    public static TipoIdentificacionRepository getInstance() {
        if (repository == null) {
            repository = new TipoIdentificacionRepository();
        }
        return repository;
    }

    public LiveData<GenericResponse<List<TipoIdentificacion>>> list() {
        final MutableLiveData<GenericResponse<List<TipoIdentificacion>>> mld = new MutableLiveData<>();
        this.api.list().enqueue(new Callback<GenericResponse<List<TipoIdentificacion>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<TipoIdentificacion>>> call, Response<GenericResponse<List<TipoIdentificacion>>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<List<TipoIdentificacion>>> call, Throwable t) {
                System.out.println("Error al obtener los tipo de identificación :" + t.getMessage());
                t.printStackTrace();
            }
        });
        return mld;
    }
}
