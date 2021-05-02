package com.example.comisariaapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.comisariaapp.R;
import com.example.comisariaapp.entity.service.Sugerencia;

import java.util.List;

public class MisSugerenciasAdapter extends ArrayAdapter<Sugerencia> {
    public MisSugerenciasAdapter(@NonNull Context context, int resource, @NonNull List<Sugerencia> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.item_mis_sugerencias, parent, false);
        }
        Sugerencia s = getItem(position);
        TextView txtMisEstrellas = convertView.findViewById(R.id.MisEstrellas),
                txtMisSugerencias = convertView.findViewById(R.id.txtMiSugerencia);

        txtMisEstrellas.setText("Usted calificó con: " + s.getEstrellas() + " Estrellas ");
        txtMisSugerencias.setText(s.getComentario());
        return convertView;
    }
}
