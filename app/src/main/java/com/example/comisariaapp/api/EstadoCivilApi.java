package com.example.comisariaapp.api;

import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.EstadoCivil;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface EstadoCivilApi {
    String baseUrl = "api/estadocivil";

    @GET(baseUrl)
    Call<GenericResponse<List<EstadoCivil>>> list();
}
