package com.example.comisariaapp.api;

import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.Tramites;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TramiteApi {
    String baseUrl = "api/tramite";

    @POST(baseUrl)
    Call<GenericResponse<Tramites>> save(@Body Tramites t);
}
