package com.example.comisariaapp.api;

import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.Tramite;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TramiteApi {
    String baseUrl = "api/tramite";

    @POST(baseUrl)
    Call<GenericResponse<Tramite>> save(@Body Tramite t);

    @GET(baseUrl + "/misTramites/{idUsu}")
//hacemos la consulta al servicio
    Call<GenericResponse<List<Tramite>>> devolverMisTramites(@Path("idUsu") int idUsu);

    @GET(baseUrl + "/consultarTramite/{codTramite}/{idUsu}")
//hacemos la consulta al servicio
    Call<GenericResponse<Tramite>> consultarTramites(@Path("codTramite") String codTramite, @Path("idUsu") int idUsu);
}
