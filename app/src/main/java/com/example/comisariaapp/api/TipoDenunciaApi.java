package com.example.comisariaapp.api;

import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.TipoDenuncia;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TipoDenunciaApi {
    String baseUrl="api/tipoDenuncia";
    @GET(baseUrl)
    Call<GenericResponse<List<TipoDenuncia>>> listActivos();
}
