package com.example.comisariaapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comisariaapp.R;
import com.example.comisariaapp.communication.DenunciadoCommunication;
import com.example.comisariaapp.entity.service.Denunciado;

import java.util.List;

public class DenunciadoAdapter extends RecyclerView.Adapter<DenunciadoAdapter.ViewHolder> {
    private final List<Denunciado> denunciados;
    private DenunciadoCommunication c;

    public DenunciadoAdapter(@Nullable DenunciadoCommunication c, List<Denunciado> denunciados) {
        this.c = c;
        this.denunciados = denunciados;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.personas_carrito_viewholder, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setItem(denunciados.get(position));
    }

    @Override
    public int getItemCount() {
        return denunciados.size();
    }

    public void updateItems(List<Denunciado> denunciados) {
        this.denunciados.clear();
        this.denunciados.addAll(denunciados);
        this.notifyDataSetChanged();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setItem(final Denunciado d) {
            final TextView txtNombre = itemView.findViewById(R.id.txtItemNombres);
            final ImageView btnRemove = this.itemView.findViewById(R.id.btnEliminar);
            txtNombre.setText(d.getNombres() + " " + d.getApellidoPaterno() + " " + d.getApellidoMaterno());
            btnRemove.setOnClickListener(v -> c.deleteD(d.getNumeroIdentificacion()));
        }
    }
}
