package com.example.comisariaapp.api;

import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.Distrito;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DistritoApi {
    String baseUrl = "api/distrito";

    @GET(baseUrl)
    Call<GenericResponse<List<Distrito>>> list();

    @GET(baseUrl + "/todos")
    Call<GenericResponse<List<Distrito>>> listAll();
}
