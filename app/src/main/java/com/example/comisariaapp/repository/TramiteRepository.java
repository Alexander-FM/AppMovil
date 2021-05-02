package com.example.comisariaapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.comisariaapp.api.ConfigApi;
import com.example.comisariaapp.api.TramiteApi;
import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.Tramite;

import java.util.List;

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

    public LiveData<GenericResponse<Tramite>> save(Tramite t) {
        MutableLiveData mld = new MutableLiveData();
        this.api.save(t).enqueue(new Callback<GenericResponse<Tramite>>() {
            @Override
            public void onResponse(Call<GenericResponse<Tramite>> call, Response<GenericResponse<Tramite>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<Tramite>> call, Throwable t) {
                System.out.println("error al intentar registrar el Trámite:" + t.getMessage());
            }
        });
        return mld;
    }

    public LiveData<GenericResponse<List<Tramite>>> devolverMisTramites(int idUsu) {
        MutableLiveData<GenericResponse<List<Tramite>>> mld = new MutableLiveData<>();
        this.api.devolverMisTramites(idUsu).enqueue(new Callback<GenericResponse<List<Tramite>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<Tramite>>> call, Response<GenericResponse<List<Tramite>>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<List<Tramite>>> call, Throwable t) {
                mld.setValue(new GenericResponse<>());
                t.printStackTrace();
            }
        });
        return mld;
    }

    public LiveData<GenericResponse<Tramite>> consultarTramites(String codTramite, int idUsu) {
        MutableLiveData<GenericResponse<Tramite>> mld = new MutableLiveData<>();
        this.api.consultarTramites(codTramite, idUsu).enqueue(new Callback<GenericResponse<Tramite>>() {
            @Override
            public void onResponse(Call<GenericResponse<Tramite>> call, Response<GenericResponse<Tramite>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<Tramite>> call, Throwable t) {
                mld.setValue(new GenericResponse<>());
                t.printStackTrace();
            }
        });
        return mld;
    }
}
