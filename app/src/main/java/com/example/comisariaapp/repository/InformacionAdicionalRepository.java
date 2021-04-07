package com.example.comisariaapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.comisariaapp.api.ConfigApi;
import com.example.comisariaapp.api.InformacionAdicionalApi;
import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.InformacionAdicional;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InformacionAdicionalRepository {
    private static InformacionAdicionalApi api;
    private static InformacionAdicionalRepository repository;

    private InformacionAdicionalRepository() {
        this.api = ConfigApi.getInformacionAdicionalApi();
    }

    public static InformacionAdicionalRepository getInstance() {
        if (repository == null) {
            repository = new InformacionAdicionalRepository();
        }
        return repository;
    }

    public LiveData<GenericResponse<List<InformacionAdicional>>> list() {
        final MutableLiveData<GenericResponse<List<InformacionAdicional>>> mld = new MutableLiveData<>();
        api.list().enqueue(new Callback<GenericResponse<List<InformacionAdicional>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<InformacionAdicional>>> call, Response<GenericResponse<List<InformacionAdicional>>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<List<InformacionAdicional>>> call, Throwable t) {
                System.out.println("error al obtener la informaciones adicionales:" + t.getMessage());
            }
        });
        return mld;
    }
}
