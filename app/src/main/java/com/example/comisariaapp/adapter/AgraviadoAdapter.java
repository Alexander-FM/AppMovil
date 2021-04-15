package com.example.comisariaapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comisariaapp.R;
import com.example.comisariaapp.entity.service.Agraviado;

import java.util.List;

public class AgraviadoAdapter extends RecyclerView.Adapter<AgraviadoAdapter.ViewHolder> {
    private List<Agraviado> agraviados;

    public AgraviadoAdapter(List<Agraviado> agraviados) {
        this.agraviados = agraviados;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.personas_carrito_viewholder, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setItem(agraviados.get(position));
    }

    @Override
    public int getItemCount() {
        return agraviados.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setItem(Agraviado a) {
            final TextView txtNombre = itemView.findViewById(R.id.txtItemNombres);
            txtNombre.setText(a.getNombres() + " " + a.getApellidoPaterno() + "" + a.getApellidoMaterno());
        }
    }
}
