package com.example.comisariaapp.api;

import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.VinculoParteDenunciada;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface VinculoParteDenunciadaApi {
    String baseUrl = "api/vinculoParteDenunciada";

    @GET(baseUrl)
    Call<GenericResponse<List<VinculoParteDenunciada>>> list();
}
