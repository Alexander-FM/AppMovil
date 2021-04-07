package com.example.comisariaapp.api;

import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.TipoTramite;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TipoTramiteApi {
    String baseUrl = "api/tipoTramite";
    @GET(baseUrl)
    Call<GenericResponse<List<TipoTramite>>>list();
}
