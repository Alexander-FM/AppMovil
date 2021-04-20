package com.example.comisariaapp.api;

import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.dto.DenunciaConDetallesDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DenunciaApi {
    String baseUrl = "api/denuncia";

    @POST(baseUrl)
    Call<GenericResponse<String>> save(@Body DenunciaConDetallesDTO dto);
}
