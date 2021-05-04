package com.example.comisariaapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.comisariaapp.R;
import com.example.comisariaapp.activity.SeccionPaginaInformativa;

import java.util.ArrayList;
import java.util.List;

public class SeccionPaginaInformativaAdapter extends ArrayAdapter<SeccionPaginaInformativa> {
    private ArrayList<SeccionPaginaInformativa> seccions;

    public SeccionPaginaInformativaAdapter(@NonNull Context context, int resource, @NonNull List<SeccionPaginaInformativa> seccions) {
        super(context, resource, seccions);
        this.seccions = (ArrayList<SeccionPaginaInformativa>) seccions;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_seccion_pagina_informativa, parent, false);
        }

        SeccionPaginaInformativa seccionPagInfor = this.seccions.get(position);

        CardView crdSeccionPaginaInfo = convertView.findViewById(R.id.crdSeccionPaginaInfo);
        ImageView imgSeccionPagInf = convertView.findViewById(R.id.imgSeccionPagInf);
        TextView txtTituloPagInf = convertView.findViewById(R.id.txtTituloPagInf);


        crdSeccionPaginaInfo.setBackgroundColor(Color.parseColor(seccionPagInfor.getBackground()));
        imgSeccionPagInf.setImageResource(seccionPagInfor.getIdImagen());
        txtTituloPagInf.setText(seccionPagInfor.getTitulo());
        txtTituloPagInf.setBackgroundColor(Color.parseColor(seccionPagInfor.getColor()));

        return convertView;
    }

}
