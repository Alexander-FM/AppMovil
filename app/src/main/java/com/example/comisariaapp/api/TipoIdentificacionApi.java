package com.example.comisariaapp.api;

import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.TipoIdentificacion;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TipoIdentificacionApi {
    String baseUrl = "api/tipoIdentificacion";

    @GET(baseUrl)
    Call<GenericResponse<List<TipoIdentificacion>>> list();
}
