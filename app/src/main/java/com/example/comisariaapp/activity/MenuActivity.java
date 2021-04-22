package com.example.comisariaapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.comisariaapp.R;
import com.example.comisariaapp.adapter.SeccionArrayAdapter;
import com.example.comisariaapp.communication.MainCommunication;
import com.example.comisariaapp.entity.GridSeccion;
import com.example.comisariaapp.entity.service.Usuario;
import com.example.comisariaapp.utils.DateDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;

public class MenuActivity extends AppCompatActivity implements MainCommunication {
    public Button btnVioFamiliar;
    private ArrayList<GridSeccion> seccions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);//getPreferences(Context.MODE_PRIVATE);
        Gson g = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();
        String user = preferences.getString("UsuarioJson", null);
        if (user!= null){
            Usuario usuario = g.fromJson(user, Usuario.class);
            toolbar.setTitle("HOLA, " + usuario.getNombres() + " " + usuario.getApellidoPaterno());
        }

        this.seccions = new ArrayList<>();
        seccions.add(new GridSeccion(1, R.drawable.denunciasonline, "Ingresar Denuncia"));
        seccions.add(new GridSeccion(2, R.drawable.tramite, "Ingresar Trámite"));
        seccions.add(new GridSeccion(3, R.drawable.consulta_tramite, "Consultar Trámite"));
        seccions.add(new GridSeccion(4, R.drawable.solicitudes, "Solicitar Información"));

        GridView grwSecciones = findViewById(R.id.grwSecciones);
        SeccionArrayAdapter adapter = new SeccionArrayAdapter(this, R.layout.gridview_seccion, seccions, this);
        grwSecciones.setAdapter(adapter);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);//getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("UsuarioJson");
                editor.apply();
                this.loadActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.misDenuncias:
                this.loadActivity(new Intent(this, MisDenunciasActivity.class));
                break;
            case R.id.misTramites:
                this.loadActivity(new Intent(this, MisTramitesActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadActivity(Intent i) {
        this.startActivity(i);
        this.overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }
}