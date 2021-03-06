package com.example.comisariaapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.comisariaapp.api.ConfigApi;
import com.example.comisariaapp.api.DenunciaApi;
import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.dto.DenunciaConDetallesDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DenunciaRepository {
    private final DenunciaApi api;
    private static DenunciaRepository repository;

    private DenunciaRepository() {
        this.api = ConfigApi.getDenunciaApi();
    }

    public static DenunciaRepository getInstance() {
        if (repository == null) {
            repository = new DenunciaRepository();
        }
        return repository;
    }

    public LiveData<GenericResponse<String>> save(DenunciaConDetallesDTO dto) {
        MutableLiveData<GenericResponse<String>> mld = new MutableLiveData<>();
        this.api.save(dto).enqueue(new Callback<GenericResponse<String>>() {
            @Override
            public void onResponse(Call<GenericResponse<String>> call, Response<GenericResponse<String>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<String>> call, Throwable t) {

                mld.setValue(new GenericResponse<>());
                mld.getValue().setRpta(0);
                mld.getValue().setBody("se ha producido un error ak intentar guadrar la denuncia:" + t.getMessage());
                t.printStackTrace();
            }
        });
        return mld;
    }

    public LiveData<GenericResponse<List<DenunciaConDetallesDTO>>> devolverMisDenuncias(int idUsu) {
        MutableLiveData<GenericResponse<List<DenunciaConDetallesDTO>>> mld = new MutableLiveData<>();
        this.api.devolverMisDenuncias(idUsu).enqueue(new Callback<GenericResponse<List<DenunciaConDetallesDTO>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<DenunciaConDetallesDTO>>> call, Response<GenericResponse<List<DenunciaConDetallesDTO>>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<List<DenunciaConDetallesDTO>>> call, Throwable t) {
                mld.setValue(new GenericResponse<>());
                t.printStackTrace();
            }
        });
        return mld;
    }

    public LiveData<GenericResponse<DenunciaConDetallesDTO>> consultarDenuncias(String cod_denuncia, int idUsu) {
        MutableLiveData<GenericResponse<DenunciaConDetallesDTO>> mld = new MutableLiveData<>();
        this.api.consultarDenuncias(cod_denuncia, idUsu).enqueue(new Callback<GenericResponse<DenunciaConDetallesDTO>>() {
            @Override
            public void onResponse(Call<GenericResponse<DenunciaConDetallesDTO>> call, Response<GenericResponse<DenunciaConDetallesDTO>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<DenunciaConDetallesDTO>> call, Throwable t) {
                mld.setValue(new GenericResponse<>());
                t.printStackTrace();
            }
        });
        return mld;
    }
}
