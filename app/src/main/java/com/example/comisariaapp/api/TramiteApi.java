package com.example.comisariaapp.api;

import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.Tramite;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface TramiteApi {
    String baseUrl = "api/tramite";

    @POST(baseUrl)
    Call<GenericResponse<Tramite>> save(@Body Tramite t);
}
