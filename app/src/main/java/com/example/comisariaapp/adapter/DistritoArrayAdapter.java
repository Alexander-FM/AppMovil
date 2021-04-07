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
import com.example.comisariaapp.entity.service.Distrito;

import java.util.List;

public class DistritoArrayAdapter<T> extends ArrayAdapter<Distrito> {

    public DistritoArrayAdapter(@NonNull Context context, int resource, @NonNull List<Distrito> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.dropdown_item, parent, false);
        }
        TextView drop_text = convertView.findViewById(R.id.item_drop_text);
        drop_text.setText(getItem(position).getDistrito());
        return convertView;
    }
}
