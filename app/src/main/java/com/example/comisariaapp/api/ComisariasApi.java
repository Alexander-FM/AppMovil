package com.example.comisariaapp.api;

import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.Comisarias;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ComisariasApi {
    String baseUrl = "api/comisarias";

    @GET(baseUrl)
    Call<GenericResponse<List<Comisarias>>> list();
}
