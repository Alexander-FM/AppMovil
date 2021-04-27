package com.example.comisariaapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.comisariaapp.entity.GenericResponse;
import com.example.comisariaapp.entity.service.Usuario;
import com.example.comisariaapp.repository.UsuarioRepository;

public class UsuarioViewModel extends AndroidViewModel {
    private final UsuarioRepository repository;

    public UsuarioViewModel(@NonNull Application application) {
        super(application);
        this.repository = UsuarioRepository.getInstance();
    }

    public LiveData<GenericResponse<Usuario>> login(String email, String contrasenia) {
        return this.repository.login(email, contrasenia);
    }

    public LiveData<GenericResponse<Usuario>> save(Usuario u) {
        return repository.save(u);
    }

    public LiveData<GenericResponse<Boolean>> existByEmail(String email) {
        return repository.existByEmail(email);
    }
}
