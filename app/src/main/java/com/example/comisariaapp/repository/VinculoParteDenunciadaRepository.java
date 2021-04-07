package com.example.comisariaapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.comisariaapp.api.ConfigApi;
import com.example.comisariaapp.api.VinculoParteDenunciadaApi;
import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.VinculoParteDenunciada;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VinculoParteDenunciadaRepository {
    private static VinculoParteDenunciadaRepository repository;
    private final VinculoParteDenunciadaApi api;

    private VinculoParteDenunciadaRepository() {
        api = ConfigApi.getVpdApi();
    }

    public static VinculoParteDenunciadaRepository getInstance() {
        if (repository == null) {
            repository = new VinculoParteDenunciadaRepository();
        }
        return repository;
    }

    public LiveData<GenericResponse<List<VinculoParteDenunciada>>> list() {
        final MutableLiveData<GenericResponse<List<VinculoParteDenunciada>>> mld = new MutableLiveData<>();
        this.api.list().enqueue(new Callback<GenericResponse<List<VinculoParteDenunciada>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<VinculoParteDenunciada>>> call, Response<GenericResponse<List<VinculoParteDenunciada>>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<List<VinculoParteDenunciada>>> call, Throwable t) {
                System.out.println("error al obtener los distritos:" + t.getMessage());
            }
        });
        return mld;
    }
}
