package com.example.comisariaapp.api;

import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.Denuncia;
import com.example.comisariaapp.entity.service.Tramites;
import com.example.comisariaapp.entity.service.dto.DenunciaConDetallesDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DenunciaApi {
    String baseUrl = "api/denuncia";

    @POST(baseUrl)
    Call<GenericResponse<String>> save(@Body DenunciaConDetallesDTO dto);

    @GET(baseUrl + "/misDenuncias/{idUsu}")
    Call<GenericResponse<List<Denuncia>>> devolverMisDenuncias(@Path("idUsu") int idUsu);

    @GET(baseUrl + "/consultarDenuncia/{cod_denuncia}/{idUsu}")
    //hacemos la consulta al servicio
    Call<GenericResponse<Denuncia>> consultarDenuncias(@Path("cod_denuncia") String cod_denuncia, @Path("idUsu") int idUsu);
}
