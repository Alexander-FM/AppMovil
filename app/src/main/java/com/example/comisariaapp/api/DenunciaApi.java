package com.example.comisariaapp.api;

import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.Denuncia;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DenunciaApi {
    String baseUrl = "api/denuncia";

    @POST
    Call<GenericResponse<Denuncia>> save(@Body Denuncia d);
}
