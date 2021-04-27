package com.example.comisariaapp.api;

import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.Sugerencia;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SugerenciaApi {
    String baseUrl = "api/sugerencia";

    @GET(baseUrl + "/byUsuario/{id}")
    Call<GenericResponse<List<Sugerencia>>> devolverComentarioByUser(@Path("id") int idUsuario);

    @POST(baseUrl)
    Call<GenericResponse<Sugerencia>> save(@Body Sugerencia s);
}
