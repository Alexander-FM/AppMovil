package com.example.comisariaapp.api;

import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.Usuario;

import retrofit2.Call;
import retrofit2.http.*;

public interface UsuarioApi {
    String base = "api/usuario";

    @FormUrlEncoded
    @POST(base + "/login")
    Call<GenericResponse<Usuario>> login(@Field("email") String email, @Field("contrasenia") String contrasenia);

    @POST(base)
    Call<GenericResponse<Usuario>> save(@Body Usuario u);

    @FormUrlEncoded
    @POST(base + "/eByMail")
    Call<GenericResponse<Boolean>> existByEmail(@Field("email") String email);
}
