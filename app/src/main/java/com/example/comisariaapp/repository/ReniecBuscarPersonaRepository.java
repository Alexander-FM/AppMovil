package com.example.comisariaapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.comisariaapp.api.reniec.BuscarPersonaApi;
import com.example.comisariaapp.api.reniec.ReniecConfigApi;
import com.example.comisariaapp.entity.ReniecResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReniecBuscarPersonaRepository {
    private static ReniecBuscarPersonaRepository repository;
    private BuscarPersonaApi api;

    public static ReniecBuscarPersonaRepository getInstance() {
        if (repository == null) {
            repository = new ReniecBuscarPersonaRepository();
        }
        return repository;
    }

    private ReniecBuscarPersonaRepository() {
        this.api = ReniecConfigApi.getBuscarPersonaApi();
    }

    public LiveData<ReniecResponse> buscar(String dni) {
        final MutableLiveData<ReniecResponse> mld = new MutableLiveData<>();
        this.api.BuscarReniec(dni).enqueue(new Callback<ReniecResponse>() {
            @Override
            public void onResponse(Call<ReniecResponse> call, Response<ReniecResponse> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ReniecResponse> call, Throwable t) {
                mld.setValue(new ReniecResponse());
                //System.out.println("Error al intentar consultar los datos con la RENIEC:" + t.getMessage());
            }
        });


        return mld;
    }
}
