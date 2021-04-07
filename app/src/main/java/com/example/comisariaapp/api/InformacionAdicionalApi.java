package com.example.comisariaapp.api;

import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.InformacionAdicional;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface InformacionAdicionalApi {
    String baseUrl = "api/informacionAdicional";

    @GET(baseUrl)
    Call<GenericResponse<List<InformacionAdicional>>> list();
}
