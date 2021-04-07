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
import com.example.comisariaapp.entity.service.TipoDenuncia;

import java.util.List;

public class TipoDenunciaAdapter extends ArrayAdapter<TipoDenuncia> {
    public TipoDenunciaAdapter(@NonNull Context context, int resource, @NonNull List<TipoDenuncia> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.dropdown_item, parent, false);
        }
        TextView drop_text = convertView.findViewById(R.id.item_drop_text);
        drop_text.setText(this.getItem(position).getTipoDenuncia());
        return convertView;
    }
}
