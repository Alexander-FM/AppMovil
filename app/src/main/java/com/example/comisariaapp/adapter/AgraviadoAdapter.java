package com.example.comisariaapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comisariaapp.R;
import com.example.comisariaapp.communication.AgraviadoCommunication;
import com.example.comisariaapp.entity.service.Agraviado;

import java.util.List;

public class AgraviadoAdapter extends RecyclerView.Adapter<AgraviadoAdapter.ViewHolder> {
    private final AgraviadoCommunication c;
    private final List<Agraviado> agraviados;

    public AgraviadoAdapter(@Nullable AgraviadoCommunication c, List<Agraviado> agraviados) {
        this.c = c;
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
        holder.setItem(this.agraviados.get(position));
    }

    @Override
    public int getItemCount() {
        return this.agraviados.size();
    }

    public void updateItems(List<Agraviado> agraviados) {
        this.agraviados.clear();
        this.agraviados.addAll(agraviados);
        this.notifyDataSetChanged();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setItem(final Agraviado a) {
            final TextView txtNombre = this.itemView.findViewById(R.id.txtItemNombres);
            final ImageView btnEliminar = itemView.findViewById(R.id.btnEliminar);
            txtNombre.setText(a.getNombres() + " " + a.getApellidoPaterno() + " " + a.getApellidoMaterno());
            btnEliminar.setOnClickListener(v -> c.deleteA(a.getNumeroIdentificacion())
            );
        }
    }
}
