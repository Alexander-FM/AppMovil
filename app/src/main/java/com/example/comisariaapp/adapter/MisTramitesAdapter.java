package com.example.comisariaapp.adapter;

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
import com.example.comisariaapp.entity.service.Denuncia;
import com.example.comisariaapp.entity.service.Tramites;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class MisTramitesAdapter extends ArrayAdapter<Tramites> {

    public MisTramitesAdapter(@NonNull Context context, int resource, @NonNull List<Tramites> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.item_mis_tramites_activity, parent, false);
        }
        Tramites t = getItem(position);
        TextView txtNombreTipoTramite = convertView.findViewById(R.id.txtNombreTipoTramite),
                txtNombreCodTramite = convertView.findViewById(R.id.txtNombreCodTramite),
                txtNombrePoliciaTramite = convertView.findViewById(R.id.txtNombrePoliciaTramite),
                txtNombreFechaDenunciaTramite = convertView.findViewById(R.id.txtNombreFechaDenunciaTramite);
        ImageView imgEstadoTramite = convertView.findViewById(R.id.imgEstadoTramite);
        imgEstadoTramite.setImageResource(t.isEstadoTramite() ? R.drawable.cheque : R.drawable.reloj);
        txtNombreTipoTramite.setText(t.getTipoTramite().getTipoTramite());
        txtNombrePoliciaTramite.setText(t.getPolicia().getNombres() + " " + t.getPolicia().getApellidoPaterno() + " " + t.getPolicia().getApellidoMaterno());
        txtNombreCodTramite.setText(t.getCodTramite());
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = dateFormat.format(t.getFechaDenuncia());
        txtNombreFechaDenunciaTramite.setText(strDate);

        return convertView;
    }
}
