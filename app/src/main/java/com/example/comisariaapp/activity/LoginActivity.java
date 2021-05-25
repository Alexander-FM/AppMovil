package com.example.comisariaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comisariaapp.R;
import com.example.comisariaapp.entity.service.Usuario;
import com.example.comisariaapp.utils.DateSerializer;
import com.example.comisariaapp.utils.TimeSerializer;
import com.example.comisariaapp.viewmodel.UsuarioViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.sql.Date;
import java.sql.Time;

import dagger.Lazy;

public class LoginActivity extends AppCompatActivity {

    private Button btnRegistrarse, btnIniciarSesion;
    private UsuarioViewModel viewModel;
    private TextView olvidePassword;
    private ExtendedFloatingActionButton fabWhatsapps;
    private FloatingActionButton fabComisariaFamilia, fabComisariaChiclayo,
            fabComisariaVictoria, fabComisariaElPorvenir,
            fabComisariaCesarLlatas, fabComisariaAtusparias, fabComisariaSectorialMonsefu;
    private TextView textcomisariofamilia, textcomisarioChiclayo, textcomisarioVictoria, textcomisarioElPorvenir, textcomisarioCesarLlatas,
            textcomisarioAtusparias, textcomisarioMonsefu;
    private Animation rotateOpen, rotateClose, fromBottom, toBottom;
    private Boolean clicked = false;
    private EditText edtEmail, edtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        viewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
        fabWhatsapps.setOnClickListener(view -> {
            addButtonClicked();
        });
        fabComisariaFamilia.setOnClickListener(view -> {
            if (this.estaInstaladoWhatsapp()) {
                Intent _intencion = new Intent("android.intent.action.MAIN");
                _intencion.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                _intencion.putExtra("jid", PhoneNumberUtils.stripSeparators("51" + "942450657") + "@s.whatsapp.net");
                this.startActivity(_intencion);
            } else {
                mostrarToast(" Lo sentimimos, ocurrio un error.");
            }
        });
        olvidePassword.setOnClickListener(v -> {
            startActivity(new Intent(this, OlvideClaveActivity.class));
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        });
        btnRegistrarse.setOnClickListener(v -> {
            startActivity(new Intent(this, RegistrarUsuarioActivity.class));
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        });
        btnIniciarSesion.setOnClickListener(v -> {
            try {
                if (validar()) {
                    viewModel.login(edtEmail.getText().toString(), edtPassword.getText().toString()).observe(this, response -> {
                        Usuario u = response.getBody();
                        if (response.getRpta() == 1) {
                            mostrarToastOk("Bienvenido: " + u.getNombres() + " " + u.getApellidoPaterno() + " " + u.getApellidoMaterno());
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);//getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            final Gson g = new GsonBuilder()
                                    .registerTypeAdapter(Date.class, new DateSerializer())
                                    .registerTypeAdapter(Time.class, new TimeSerializer())
                                    .create();
                            editor.putString("UsuarioJson", g.toJson(u, new TypeToken<Usuario>() {
                            }.getType()));
                            editor.apply();

                            startActivity(new Intent(this, MenuActivity.class));
                        } else {
                            mostrarToast("Credenciales Incorrectas");
                        }
                    });
                } else {
                    mostrarToast("Completa todos los campos, por favor.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                mostrarToast("Se ha producido un error al intentar loguearte:" + e.getMessage());
                //Toast.makeText(this, "se ha producido un error al intentar loguearte:" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addButtonClicked() {
        setVisibility(clicked);
        setAnimation(clicked);
        setClicklabled(clicked);
        clicked = !clicked;
    }

    private void setClicklabled(Boolean clicked) {
        if (!clicked) {
            fabComisariaFamilia.setClickable(true);
            fabComisariaVictoria.setClickable(true);
            fabComisariaSectorialMonsefu.setClickable(true);
            fabComisariaElPorvenir.setClickable(true);
            fabComisariaChiclayo.setClickable(true);
            fabComisariaCesarLlatas.setClickable(true);
            fabComisariaAtusparias.setClickable(true);
        } else {
            fabComisariaFamilia.setClickable(false);
            fabComisariaVictoria.setClickable(false);
            fabComisariaSectorialMonsefu.setClickable(false);
            fabComisariaElPorvenir.setClickable(false);
            fabComisariaChiclayo.setClickable(false);
            fabComisariaCesarLlatas.setClickable(false);
            fabComisariaAtusparias.setClickable(false);
        }
    }

    private void setVisibility(Boolean clicked) {
        if (!clicked) {
            fabComisariaFamilia.setVisibility(View.VISIBLE);
            fabComisariaVictoria.setVisibility(View.VISIBLE);
            fabComisariaSectorialMonsefu.setVisibility(View.VISIBLE);
            fabComisariaElPorvenir.setVisibility(View.VISIBLE);
            fabComisariaChiclayo.setVisibility(View.VISIBLE);
            fabComisariaCesarLlatas.setVisibility(View.VISIBLE);
            fabComisariaAtusparias.setVisibility(View.VISIBLE);
            textcomisariofamilia.setVisibility(View.VISIBLE);
            textcomisarioChiclayo.setVisibility(View.VISIBLE);
            textcomisarioVictoria.setVisibility(View.VISIBLE);
            textcomisarioElPorvenir.setVisibility(View.VISIBLE);
            textcomisarioCesarLlatas.setVisibility(View.VISIBLE);
            textcomisarioAtusparias.setVisibility(View.VISIBLE);
            textcomisarioMonsefu.setVisibility(View.VISIBLE);
        } else {
            fabComisariaFamilia.setVisibility(View.INVISIBLE);
            fabComisariaVictoria.setVisibility(View.INVISIBLE);
            fabComisariaSectorialMonsefu.setVisibility(View.INVISIBLE);
            fabComisariaElPorvenir.setVisibility(View.INVISIBLE);
            fabComisariaChiclayo.setVisibility(View.INVISIBLE);
            fabComisariaCesarLlatas.setVisibility(View.INVISIBLE);
            fabComisariaAtusparias.setVisibility(View.INVISIBLE);
            textcomisariofamilia.setVisibility(View.INVISIBLE);
            textcomisarioChiclayo.setVisibility(View.INVISIBLE);
            textcomisarioVictoria.setVisibility(View.INVISIBLE);
            textcomisarioElPorvenir.setVisibility(View.INVISIBLE);
            textcomisarioCesarLlatas.setVisibility(View.INVISIBLE);
            textcomisarioAtusparias.setVisibility(View.INVISIBLE);
            textcomisarioMonsefu.setVisibility(View.INVISIBLE);
        }
    }

    private void setAnimation(Boolean clicked) {
        if (!clicked) {
            fabComisariaFamilia.startAnimation(fromBottom);
            fabComisariaVictoria.startAnimation(fromBottom);
            fabComisariaSectorialMonsefu.startAnimation(fromBottom);
            fabComisariaElPorvenir.startAnimation(fromBottom);
            fabComisariaChiclayo.startAnimation(fromBottom);
            fabComisariaCesarLlatas.startAnimation(fromBottom);
            fabComisariaAtusparias.startAnimation(fromBottom);
            fabWhatsapps.startAnimation(rotateOpen);
        } else {
            fabComisariaFamilia.startAnimation(toBottom);
            fabComisariaVictoria.startAnimation(toBottom);
            fabComisariaSectorialMonsefu.startAnimation(toBottom);
            fabComisariaElPorvenir.startAnimation(toBottom);
            fabComisariaChiclayo.startAnimation(toBottom);
            fabComisariaCesarLlatas.startAnimation(toBottom);
            fabComisariaAtusparias.startAnimation(toBottom);
            fabWhatsapps.startAnimation(rotateClose);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String pref = preferences.getString("UsuarioJson", "");
        if (!pref.equals("")) {
            mostrarToastOk("Se detectó una sesion activa, el login será omitido!");
            //Toast.makeText(this, "se detectó una sesion activa,login omitido", Toast.LENGTH_SHORT).show();
            this.startActivity(new Intent(this, MenuActivity.class));
            this.overridePendingTransition(R.anim.left_in, R.anim.left_out);
        }
    }

    private void init() {
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        olvidePassword = findViewById(R.id.olvideContraseña);
        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim);
        fabWhatsapps = findViewById(R.id.fabWhatsapps);
        //Floating Action Button Comisarías
        fabComisariaAtusparias = findViewById(R.id.fabComisariaAtusparias);
        fabComisariaCesarLlatas = findViewById(R.id.fabComisariaCesarLlatas);
        fabComisariaChiclayo = findViewById(R.id.fabComisariaChiclayo);
        fabComisariaElPorvenir = findViewById(R.id.fabComisariaElPorvenir);
        fabComisariaSectorialMonsefu = findViewById(R.id.fabComisariaSectorialMonsefu);
        fabComisariaVictoria = findViewById(R.id.fabComisariaVictoria);
        fabComisariaFamilia = findViewById(R.id.fabComisariaFamilia);
        //TExto comisarias
        textcomisariofamilia = findViewById(R.id.textcomisariofamilia);
        textcomisarioChiclayo = findViewById(R.id.textcomisarioChiclayo);
        textcomisarioVictoria = findViewById(R.id.textcomisarioVictoria);
        textcomisarioElPorvenir = findViewById(R.id.textcomisarioElPorvenir);
        textcomisarioCesarLlatas = findViewById(R.id.textcomisarioCesarLlatas);
        textcomisarioAtusparias = findViewById(R.id.textcomisarioAtusparias);
        textcomisarioMonsefu = findViewById(R.id.textcomisarioMonsefu);
        edtEmail = findViewById(R.id.username_input);
        edtPassword = findViewById(R.id.password_input);

    }

    private boolean estaInstaladoWhatsapp() {
        PackageManager pm = this.getPackageManager();
        try {
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void mostrarToast(String texto) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View layouView = layoutInflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.layout_base_1));
        TextView textView = layouView.findViewById(R.id.textoinfo);
        textView.setText(texto);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layouView);
        toast.show();

    }

    public void mostrarToastOk(String texto) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View layouView = layoutInflater.inflate(R.layout.custom_toast_check, (ViewGroup) findViewById(R.id.layout_base_2));
        TextView textView = layouView.findViewById(R.id.textoinfo2);
        textView.setText(texto);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layouView);
        toast.show();

    }

    private boolean validar() {
        boolean retorno = true;
        Drawable customErrorDrawable = ContextCompat.getDrawable(this, R.drawable.ic_error);
        customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());
        String email = edtEmail.getText().toString();
        String passwordUser = edtPassword.getText().toString();
        if (email.isEmpty() || passwordUser.isEmpty()) {
            edtEmail.setError("Este campo no puede quedar vacío.", customErrorDrawable);
            edtPassword.setError("Este campo no puede quedar vacio", customErrorDrawable);
            retorno = false;
        }
        return retorno;
    }
}