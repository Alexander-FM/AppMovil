package com.example.comisariaapp.api.reniec;

import com.example.comisariaapp.entity.ReniecResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BuscarPersonaApi {
    @GET("api/dni/{dni}")
    Call<ReniecResponse> BuscarReniec(@Path("dni") String dni);
}
