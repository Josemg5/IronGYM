package com.example.irongym;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EjercicioAdapter extends RecyclerView.Adapter<EjercicioAdapter.EjercicioViewHolder> {

    private List<Ejercicio> listaEjercicios = new ArrayList<>();
    private DetalleRutinaViewModel viewModel;
    private boolean isEntrenoActivo = false;

    public void setEntrenoActivo(boolean activo) {
        this.isEntrenoActivo = activo;
        notifyDataSetChanged();
    }

    public void setViewModel(DetalleRutinaViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public EjercicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ejercicio, parent, false);
        return new EjercicioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EjercicioViewHolder holder, int position) {
        Ejercicio ejercicio = listaEjercicios.get(position);
        holder.tvNombreEjercicio.setText(ejercicio.nombre);

        holder.tvNombreEjercicio.setOnLongClickListener(v -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Eliminar ejercicio")
                    .setMessage("¿Deseas eliminar este ejercicio de la rutina?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        AppDatabase.databaseWriteExecutor.execute(() -> {
                            AppDatabase.getDatabase(holder.itemView.getContext()).gimnasioDao().eliminarEjercicio(ejercicio);
                        });
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });

        holder.llSeriesContainer.removeAllViews();
        
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Serie> series = AppDatabase.getDatabase(holder.itemView.getContext()).gimnasioDao().getSeriesPorEjercicio(ejercicio.id);
            holder.itemView.post(() -> {
                holder.llSeriesContainer.removeAllViews();
                int numeroSerie = 1;
                for (Serie serie : series) {
                    agregarFilaSerie(holder, ejercicio.nombre, ejercicio.id, numeroSerie, serie);
                    numeroSerie++;
                }
            });
        });

        holder.btnAddSerie.setOnClickListener(v -> {
            int sigSerie = holder.llSeriesContainer.getChildCount() + 1;
            agregarFilaSerie(holder, ejercicio.nombre, ejercicio.id, sigSerie, null);
        });
    }

    private void agregarFilaSerie(EjercicioViewHolder holder, String ejercicioNombre, int ejercicioId, int numeroSerie, Serie serieExistente) {
        View fila = LayoutInflater.from(holder.itemView.getContext()).inflate(R.layout.item_serie_fila, holder.llSeriesContainer, false);
        
        TextView tvNumeroSerie = fila.findViewById(R.id.tvNumeroSerie);
        TextView tvAnteriorSerie = fila.findViewById(R.id.tvAnteriorSerie);
        EditText etKgSerie = fila.findViewById(R.id.etKgSerie);
        EditText etRepsSerie = fila.findViewById(R.id.etRepsSerie);
        CheckBox cbCompletadoSerie = fila.findViewById(R.id.cbCompletadoSerie);

        tvNumeroSerie.setText(String.valueOf(numeroSerie));

        if (viewModel != null) {
            viewModel.obtenerPR(ejercicioNombre, pr -> {
                holder.itemView.post(() -> {
                    if (pr != null && pr > 0) {
                        tvAnteriorSerie.setText("PR: " + pr + " kg");
                    } else {
                        tvAnteriorSerie.setText("-");
                    }
                });
            });
        }

        final Serie[] serieReferencia = new Serie[1];

        if (serieExistente != null) {
            serieReferencia[0] = serieExistente;
            etKgSerie.setText(String.valueOf(serieExistente.peso));
            etRepsSerie.setText(String.valueOf(serieExistente.repeticiones));
            cbCompletadoSerie.setChecked(serieExistente.completado);
        }

        if (!isEntrenoActivo) {
            cbCompletadoSerie.setVisibility(View.GONE);
            etKgSerie.setEnabled(false);
            etRepsSerie.setEnabled(false);
        } else {
            cbCompletadoSerie.setVisibility(View.VISIBLE);
            if (serieExistente != null && serieExistente.completado) {
                etKgSerie.setEnabled(false);
                etRepsSerie.setEnabled(false);
            } else {
                etKgSerie.setEnabled(true);
                etRepsSerie.setEnabled(true);
            }
        }

        cbCompletadoSerie.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (serieReferencia[0] == null) {
                    String pesoStr = etKgSerie.getText().toString().trim();
                    String repsStr = etRepsSerie.getText().toString().trim();

                    if (!pesoStr.isEmpty() && !repsStr.isEmpty()) {
                        Serie nuevaSerie = new Serie();
                        nuevaSerie.ejercicioId = ejercicioId;
                        nuevaSerie.peso = Double.parseDouble(pesoStr);
                        nuevaSerie.repeticiones = Integer.parseInt(repsStr);
                        nuevaSerie.completado = true;

                        AppDatabase.databaseWriteExecutor.execute(() -> {
                            long idGenerado = AppDatabase.getDatabase(holder.itemView.getContext()).gimnasioDao().insertSerie(nuevaSerie);
                            nuevaSerie.id = (int) idGenerado;
                            serieReferencia[0] = nuevaSerie;
                        });

                        etKgSerie.setEnabled(false);
                        etRepsSerie.setEnabled(false);
                    } else {
                        cbCompletadoSerie.setChecked(false);
                    }
                } else {
                    String pesoStr = etKgSerie.getText().toString().trim();
                    String repsStr = etRepsSerie.getText().toString().trim();

                    if (!pesoStr.isEmpty() && !repsStr.isEmpty()) {
                        serieReferencia[0].peso = Double.parseDouble(pesoStr);
                        serieReferencia[0].repeticiones = Integer.parseInt(repsStr);
                        serieReferencia[0].completado = true;

                        if (viewModel != null) {
                            viewModel.actualizarSerie(serieReferencia[0]);
                        } else {
                            AppDatabase.databaseWriteExecutor.execute(() -> {
                                AppDatabase.getDatabase(holder.itemView.getContext()).gimnasioDao().actualizarSerie(serieReferencia[0]);
                            });
                        }

                        etKgSerie.setEnabled(false);
                        etRepsSerie.setEnabled(false);
                    } else {
                        cbCompletadoSerie.setChecked(false);
                    }
                }
            } else {
                if (serieReferencia[0] != null) {
                    final Serie serieABorrar = serieReferencia[0];
                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        AppDatabase.getDatabase(holder.itemView.getContext()).gimnasioDao().eliminarSerie(serieABorrar);
                    });
                    serieReferencia[0] = null;
                }
                if (isEntrenoActivo) {
                    etKgSerie.setEnabled(true);
                    etRepsSerie.setEnabled(true);
                }
            }
        });

        fila.setOnLongClickListener(v -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Eliminar serie")
                    .setMessage("¿Deseas eliminar esta serie?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        if (serieReferencia[0] != null) {
                            final Serie serieABorrar = serieReferencia[0];
                            AppDatabase.databaseWriteExecutor.execute(() -> {
                                AppDatabase.getDatabase(holder.itemView.getContext()).gimnasioDao().eliminarSerie(serieABorrar);
                            });
                        }
                        holder.llSeriesContainer.removeView(fila);
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });

        holder.llSeriesContainer.addView(fila);
    }

    @Override
    public int getItemCount() {
        return listaEjercicios.size();
    }

    public void setEjercicios(List<Ejercicio> ejercicios) {
        this.listaEjercicios = ejercicios;
        notifyDataSetChanged();
    }

    static class EjercicioViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreEjercicio;
        android.widget.LinearLayout llSeriesContainer;
        android.widget.Button btnAddSerie;

        public EjercicioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreEjercicio = itemView.findViewById(R.id.tvNombreEjercicio);
            llSeriesContainer = itemView.findViewById(R.id.llSeriesContainer);
            btnAddSerie = itemView.findViewById(R.id.btnAddSerie);
        }
    }
}
