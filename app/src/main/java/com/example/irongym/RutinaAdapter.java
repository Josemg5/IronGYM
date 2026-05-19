package com.example.irongym;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RutinaAdapter extends RecyclerView.Adapter<RutinaAdapter.RutinaViewHolder> {

    public interface OnItemLongClickListener {
        void onItemLongClick(Rutina rutina);
    }

    public interface OnItemClickListener {
        void onItemClick(Rutina rutina);
    }

    private List<Rutina> listaRutinas = new ArrayList<>();
    private OnItemLongClickListener longClickListener;
    private OnItemClickListener clickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public RutinaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rutina, parent, false);
        return new RutinaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RutinaViewHolder holder, int position) {
        Rutina rutina = listaRutinas.get(position);
        holder.tvNombreRutina.setText(rutina.nombre);

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onItemClick(rutina);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(rutina);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return listaRutinas.size();
    }

    public void setRutinas(List<Rutina> rutinas) {
        this.listaRutinas = rutinas;
        notifyDataSetChanged();
    }

    static class RutinaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreRutina;

        public RutinaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreRutina = itemView.findViewById(R.id.tvNombreRutina);
        }
    }
}
