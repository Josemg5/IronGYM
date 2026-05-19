package com.example.irongym;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DetalleRutinaActivity extends AppCompatActivity {

    private DetalleRutinaViewModel viewModel;
    private EjercicioAdapter ejercicioAdapter;
    private int rutinaId;
    private boolean entrenoIniciado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_rutina);

        TextView tvDetalleRutinaTitle = findViewById(R.id.tvDetalleRutinaTitle);
        Button btnNuevoEjercicio = findViewById(R.id.btnNuevoEjercicio);
        RecyclerView rvEjercicios = findViewById(R.id.rvEjercicios);
        
        android.widget.Chronometer chronometerSesion = findViewById(R.id.chronometerSesion);
        Button btnToggleSesion = findViewById(R.id.btnToggleSesion);

        Intent intent = getIntent();
        rutinaId = intent.getIntExtra("RUTINA_ID", -1);
        String rutinaNombre = intent.getStringExtra("RUTINA_NOMBRE");

        if (rutinaNombre != null) {
            tvDetalleRutinaTitle.setText(rutinaNombre);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ejercicioAdapter = new EjercicioAdapter();
        rvEjercicios.setLayoutManager(new LinearLayoutManager(this));
        rvEjercicios.setAdapter(ejercicioAdapter);

        viewModel = new ViewModelProvider(this).get(DetalleRutinaViewModel.class);
        ejercicioAdapter.setViewModel(viewModel);

        viewModel.getEjercicios(rutinaId).observe(this, ejercicios -> {
            if (ejercicios != null) {
                ejercicioAdapter.setEjercicios(ejercicios);
            }
        });

        btnToggleSesion.setOnClickListener(v -> {
            if (!entrenoIniciado) {
                entrenoIniciado = true;
                ejercicioAdapter.setEntrenoActivo(true);
                chronometerSesion.setBase(android.os.SystemClock.elapsedRealtime());
                chronometerSesion.setVisibility(android.view.View.VISIBLE);
                chronometerSesion.start();
                btnToggleSesion.setText("Terminar Entreno");
                btnToggleSesion.setBackgroundColor(android.graphics.Color.parseColor("#CF6679"));
            } else {
                entrenoIniciado = false;
                chronometerSesion.stop();
                long elapsedMillis = android.os.SystemClock.elapsedRealtime() - chronometerSesion.getBase();
                int minutes = (int) (elapsedMillis / 60000);
                int seconds = (int) (elapsedMillis % 60000 / 1000);
                String duracion = String.format("%02d:%02d", minutes, seconds);

                AppDatabase.databaseWriteExecutor.execute(() -> {
                    java.util.List<Serie> seriesActuales = AppDatabase.getDatabase(getApplicationContext()).gimnasioDao().getSeriesPorRutina(rutinaId);
                    double volumenTotal = 0;
                    for (Serie s : seriesActuales) {
                        volumenTotal += (s.peso * s.repeticiones);
                    }

                    java.util.List<Ejercicio> ejerciciosActuales = AppDatabase.getDatabase(getApplicationContext()).gimnasioDao().getEjerciciosPorRutinaSync(rutinaId);
                    StringBuilder resumen = new StringBuilder();
                    for (int i = 0; i < ejerciciosActuales.size(); i++) {
                        resumen.append(ejerciciosActuales.get(i).nombre);
                        if (i < ejerciciosActuales.size() - 1) {
                            resumen.append(", ");
                        }
                    }

                    HistorialEntreno historial = new HistorialEntreno();
                    historial.nombreRutina = rutinaNombre;
                    historial.fecha = System.currentTimeMillis();
                    historial.duracionFormateada = duracion;
                    historial.volumenTotal = volumenTotal;
                    historial.resumenEjercicios = resumen.toString();

                    viewModel.insertarHistorial(historial, () -> {
                        viewModel.reiniciarSeriesDeRutina(rutinaId, () -> {
                            runOnUiThread(() -> {
                                ejercicioAdapter.setEntrenoActivo(false);
                                android.widget.Toast.makeText(DetalleRutinaActivity.this, "Entreno guardado", android.widget.Toast.LENGTH_SHORT).show();
                                chronometerSesion.setVisibility(android.view.View.GONE);
                                btnToggleSesion.setText("Iniciar Entreno");
                                btnToggleSesion.setBackgroundColor(android.graphics.Color.parseColor("#BB86FC"));
                            });
                        });
                    });
                });
            }
        });

        btnNuevoEjercicio.setOnClickListener(v -> {
            Intent intentCatalogo = new Intent(DetalleRutinaActivity.this, CatalogoEjerciciosActivity.class);
            intentCatalogo.putExtra("RUTINA_ID", rutinaId);
            startActivity(intentCatalogo);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
