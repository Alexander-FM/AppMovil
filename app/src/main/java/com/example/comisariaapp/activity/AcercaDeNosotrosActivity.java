package com.example.comisariaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.comisariaapp.R;

public class AcercaDeNosotrosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de_nosotros);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.atras);
        toolbar.setNavigationOnClickListener(v -> {//Reemplazo con lamba
            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
            overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
        });
        WebView wbMapa=this.findViewById(R.id.wbMapa);
        WebSettings ajustesVisor=wbMapa.getSettings();
        ajustesVisor.setJavaScriptEnabled(true);
        wbMapa.loadUrl("https://www.google.com/maps/place/Comisaria+de+la+familia/@-6.7495713,-79.8547991,17z/data=!4m5!3m4!1s0x904cef1901a637f9:0x46ef805a14d4ccb4!8m2!3d-6.7496417!4d-79.8532417");
    }
}