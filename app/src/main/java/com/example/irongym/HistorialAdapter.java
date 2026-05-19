package com.example.irongym;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.HistorialViewHolder> {

    private List<HistorialEntreno> historialList = new ArrayList<>();

    @NonNull
    @Override
    public HistorialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historial, parent, false);
        return new HistorialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistorialViewHolder holder, int position) {
        HistorialEntreno historial = historialList.get(position);
        holder.tvHistorialRutina.setText(historial.nombreRutina);
        holder.tvHistorialResumen.setText(historial.resumenEjercicios != null ? historial.resumenEjercicios : "");
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        holder.tvHistorialFecha.setText("Fecha: " + sdf.format(new Date(historial.fecha)));
        
        holder.tvHistorialDuracion.setText("Duración: " + historial.duracionFormateada);
        holder.tvHistorialVolumen.setText("Volumen Total: " + historial.volumenTotal + " kg");
    }

    @Override
    public int getItemCount() {
        return historialList.size();
    }

    public void setHistoriales(List<HistorialEntreno> historiales) {
        this.historialList = historiales;
        notifyDataSetChanged();
    }

    static class HistorialViewHolder extends RecyclerView.ViewHolder {
        TextView tvHistorialRutina;
        TextView tvHistorialResumen;
        TextView tvHistorialFecha;
        TextView tvHistorialDuracion;
        TextView tvHistorialVolumen;

        public HistorialViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHistorialRutina = itemView.findViewById(R.id.tvHistorialRutina);
            tvHistorialResumen = itemView.findViewById(R.id.tvHistorialResumen);
            tvHistorialFecha = itemView.findViewById(R.id.tvHistorialFecha);
            tvHistorialDuracion = itemView.findViewById(R.id.tvHistorialDuracion);
            tvHistorialVolumen = itemView.findViewById(R.id.tvHistorialVolumen);
        }
    }
}
