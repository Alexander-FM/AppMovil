package com.example.comisariaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.comisariaapp.R;
import com.example.comisariaapp.utils.EmailUtil;
import com.example.comisariaapp.viewmodel.UsuarioViewModel;

import java.util.concurrent.atomic.AtomicReference;

public class OlvideClaveActivity extends AppCompatActivity {
    private EditText edtCorreoRecuperacion;
    private Button btnContinuar;
    private UsuarioViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvide_clave);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.atras);
        toolbar.setNavigationOnClickListener(v -> {//Reemplazo con lamba
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
        });
        this.viewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
        this.init();
    }

    private void init() {
        edtCorreoRecuperacion = findViewById(R.id.edtCorreoRecuperacion);
        btnContinuar = findViewById(R.id.btnContinuar);
        btnContinuar.setOnClickListener(view -> {
            this.viewModel.existByEmail(edtCorreoRecuperacion.getText().toString()).observe(this, response -> {
                try {
                    Boolean b = false;
                    AtomicReference<String> msje = new AtomicReference<>("");
                    if (response.getRpta() == 1) {
                        b = response.getBody();
                    }
                    if (b) {
                        Toast.makeText(this, "Correo Electrónico Válido.", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(this, CambiarClaveActivity.class);
                        i.putExtra("email", edtCorreoRecuperacion.getText().toString());
                        startActivity(i);
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        /*EnviarCorreoTask task = new EnviarCorreoTask();
                        task.execute(edtCorreoRecuperacion.getText().toString());
                        Toast.makeText(this, task.get() ? "correo de recuperación enviado" : "no se ha podido enviar el correo de recuperación", Toast.LENGTH_SHORT).show();
                        this.finish();*/
                    } else {
                        msje.set("El correo ingresado no está asociado a ningún usuario");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }

    private class EnviarCorreoTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            return EmailUtil.enviarCorreodeRecuperación(strings[0]);

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }
}