package com.example.comisariaapp.api;

import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.Denunciado;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DenunciadoApi {
    String baseUrl = "api/denunciado";

    @POST(baseUrl)
    Call<GenericResponse<Denunciado>> save(@Body Denunciado d);
}
