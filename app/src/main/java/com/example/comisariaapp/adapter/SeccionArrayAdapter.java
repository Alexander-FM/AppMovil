package com.example.comisariaapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.comisariaapp.R;
import com.example.comisariaapp.activity.ConsultarDenuncias;
import com.example.comisariaapp.activity.ConsultarTramiteActivity;
import com.example.comisariaapp.activity.RegistrarDenunciaActivity;
import com.example.comisariaapp.activity.RegistrarTramiteActivity;
import com.example.comisariaapp.communication.MainCommunication;
import com.example.comisariaapp.entity.GridSeccion;

import java.util.ArrayList;
import java.util.List;

public class SeccionArrayAdapter extends ArrayAdapter<GridSeccion> {
    private List<GridSeccion> seccions;
    private MainCommunication mc;

    public SeccionArrayAdapter(@NonNull Context context, int resource, @NonNull List<GridSeccion> seccions, MainCommunication mc) {
        super(context, resource, seccions);
        this.seccions = (ArrayList<GridSeccion>) seccions;
        this.mc = mc;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.gridview_seccion, parent, false);
        }

        GridSeccion seccion = this.seccions.get(position);
        ImageView imgSeccion = convertView.findViewById(R.id.imgSeccion);
        Button txtTitulo = convertView.findViewById(R.id.btnTitulo);


        imgSeccion.setImageResource(seccion.getId_imagen());
        txtTitulo.setText(seccion.getTxtBoton());

        txtTitulo.setOnClickListener(v -> {
            Class<?> destiny = null;
            switch (seccion.getId()) {
                case 1:
                    destiny = RegistrarDenunciaActivity.class;
                    break;
                case 2:
                    destiny = RegistrarTramiteActivity.class;
                    break;
                case 3:
                    destiny = ConsultarTramiteActivity.class;
                    break;
                case 4:
                    destiny = ConsultarDenuncias.class;
                    break;
            }
            if (destiny != null) {
                Intent i = new Intent(this.getContext(), destiny);
                this.mc.loadActivity(i);
            } else {
                Toast.makeText(getContext(), "Esta característica esta en desarrollo", Toast.LENGTH_SHORT).show();
            }

        });

        return convertView;
    }
}
