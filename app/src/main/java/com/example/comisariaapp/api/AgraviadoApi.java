package com.example.comisariaapp.api;

import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.Agraviado;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AgraviadoApi {
    String baseUrl = "api/agraviado";

    @POST(baseUrl)
    Call<GenericResponse<Agraviado>> save(@Body Agraviado a);
}
