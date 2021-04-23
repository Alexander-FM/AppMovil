package com.example.comisariaapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.comisariaapp.R;
import com.example.comisariaapp.communication.MisDenunciasCommunication;
import com.example.comisariaapp.entity.service.Denuncia;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class MisDenunciasAdapter extends ArrayAdapter<Denuncia> {
    private MisDenunciasCommunication communication;

    public MisDenunciasAdapter(@NonNull Context context, int resource, @NonNull List<Denuncia> objects, MisDenunciasCommunication communication) {
        super(context, resource, objects);
        this.communication = communication;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.item_mis_denuncias_activity, parent, false);
        }
        Denuncia d = getItem(position);
        TextView txtNombreTipoDenuncia = convertView.findViewById(R.id.txtNombreTipoDenuncia),
                txtNombreCodDenuncia = convertView.findViewById(R.id.txtNombreCodDenuncia),
                txtNombrePolicia = convertView.findViewById(R.id.txtNombrePolicia),
                txtNombreVPD = convertView.findViewById(R.id.txtNombreVPD),
                txtNombreFechaDenuncia = convertView.findViewById(R.id.txtNombreFechaDenuncia);
        ImageView imgEstado = convertView.findViewById(R.id.imgEstado);
        imgEstado.setImageResource(d.isEstadoDenuncia() ? R.drawable.cheque : R.drawable.reloj);
        txtNombreTipoDenuncia.setText(d.getTipoDenuncia().getTipoDenuncia());
        txtNombrePolicia.setText(d.getPolicia().getNombres() + " " + d.getPolicia().getApellidoPaterno() + " " + d.getPolicia().getApellidoMaterno());
        txtNombreCodDenuncia.setText(d.getCod_denuncia());
        txtNombreVPD.setText(d.getVinculoParteDenunciada().getNombre());
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = dateFormat.format(d.getFechaDenuncia());
        txtNombreFechaDenuncia.setText(strDate);
        convertView.setOnClickListener(v -> communication.showDetalles(position));
        return convertView;
    }
}
