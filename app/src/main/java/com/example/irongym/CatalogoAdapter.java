package com.example.irongym;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CatalogoAdapter extends RecyclerView.Adapter<CatalogoAdapter.CatalogoViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Ejercicio ejercicio);
    }

    private List<Ejercicio> listaEjercicios = new ArrayList<>();
    private OnItemClickListener clickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public CatalogoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ejercicio, parent, false);
        return new CatalogoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogoViewHolder holder, int position) {
        Ejercicio ejercicio = listaEjercicios.get(position);
        holder.tvNombreEjercicio.setText(ejercicio.nombre);

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onItemClick(ejercicio);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaEjercicios.size();
    }

    public void setEjercicios(List<Ejercicio> ejercicios) {
        this.listaEjercicios = ejercicios;
        notifyDataSetChanged();
    }

    static class CatalogoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreEjercicio;

        public CatalogoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreEjercicio = itemView.findViewById(R.id.tvNombreEjercicio);
        }
    }
}
