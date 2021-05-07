package com.example.comisariaapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comisariaapp.R;
import com.example.comisariaapp.entity.service.Agraviado;
import com.example.comisariaapp.entity.service.Denunciado;
import com.example.comisariaapp.entity.service.Persona;

import java.util.List;

public class DetalleMiDenunciaAdapter extends RecyclerView.Adapter<DetalleMiDenunciaAdapter.ViewHolder> {
    private final List<Persona> personas;

    public DetalleMiDenunciaAdapter(List<Persona> personas) {
        this.personas = personas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_detalle_denuncias_ad_viewholder, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setItem(personas.get(position));
    }

    @Override
    public int getItemCount() {
        return personas.size();
    }

    public void updateItems(List<Persona> personas) {
        this.personas.clear();
        this.personas.addAll(personas);
        this.notifyDataSetChanged();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtNombresCompletos, txtIdent, txtInfAdicional, txtDistrito, txtDomicilio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.txtNombresCompletos = itemView.findViewById(R.id.txtNombreCompleto);
            this.txtInfAdicional = itemView.findViewById(R.id.txtInfAdic);
            this.txtIdent = itemView.findViewById(R.id.txtNumeroIden);
            this.txtDistrito = itemView.findViewById(R.id.txtDistrito);
            this.txtDomicilio = itemView.findViewById(R.id.txtDomicilio);
        }

        public void setItem(Persona p) {
            this.txtNombresCompletos.setText(p.getNombres() + " " + p.getApellidoPaterno() + " " + p.getApellidoMaterno());
            this.txtIdent.setText(p.getTipoIdentificacion().getTipoIdentificacion() + " - " + p.getNumeroIdentificacion());
            this.txtInfAdicional.setText(p instanceof Agraviado ? ((Agraviado) p).getInformacionAdicional().getNombre() :
                    ((Denunciado) p).getInformacionAdicional().getNombre());
            //this.txtDistrito.setText(p.getDistrito().getDistrito());
            //this.txtDomicilio.setText(p.getDireccion());
        }
    }
}
