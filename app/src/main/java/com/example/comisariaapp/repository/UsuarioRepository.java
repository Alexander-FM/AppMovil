package com.example.comisariaapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.comisariaapp.api.ConfigApi;
import com.example.comisariaapp.entity.service.Usuario;
import com.example.comisariaapp.api.UsuarioApi;
import com.example.comisariaapp.entity.GenericResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioRepository {
    private static UsuarioRepository repository;
    private final UsuarioApi api;

    public static UsuarioRepository getInstance() {
        if (repository == null) {
            repository = new UsuarioRepository();
        }
        return repository;
    }

    private UsuarioRepository() {
        this.api = ConfigApi.getUsuarioApi();
    }

    public LiveData<GenericResponse<Usuario>> login(String email, String contrasenia) {
        final MutableLiveData<GenericResponse<Usuario>> mld = new MutableLiveData();
        this.api.login(email, contrasenia).enqueue(new Callback<GenericResponse<Usuario>>() {
            @Override
            public void onResponse(Call<GenericResponse<Usuario>> call, Response<GenericResponse<Usuario>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<Usuario>> call, Throwable t) {
                mld.setValue(new GenericResponse<Usuario>());
                System.out.println("se ha producido un error al inetntar loguearte:" + t.getMessage());
            }
        });
        return mld;
    }

    public LiveData<GenericResponse<Usuario>> save(Usuario u) {
        final MutableLiveData<GenericResponse<Usuario>> mld = new MutableLiveData<>();
        this.api.save(u).enqueue(new Callback<GenericResponse<Usuario>>() {
            @Override
            public void onResponse(Call<GenericResponse<Usuario>> call, Response<GenericResponse<Usuario>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<Usuario>> call, Throwable t) {
                System.out.println("se ha producido un error alintentar registrarte:" + t.getMessage());
            }
        });
        return mld;
    }
}
