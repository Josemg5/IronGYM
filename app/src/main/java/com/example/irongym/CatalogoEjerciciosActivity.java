package com.example.irongym;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CatalogoEjerciciosActivity extends AppCompatActivity {

    private int rutinaId;
    private CatalogoAdapter catalogoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo_ejercicios);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        rutinaId = getIntent().getIntExtra("RUTINA_ID", -1);

        RecyclerView rvCatalogoEjercicios = findViewById(R.id.rvCatalogoEjercicios);
        
        catalogoAdapter = new CatalogoAdapter();
        rvCatalogoEjercicios.setLayoutManager(new LinearLayoutManager(this));
        rvCatalogoEjercicios.setAdapter(catalogoAdapter);

        catalogoAdapter.setEjercicios(CatalogoProvider.obtenerCatalogo());

        catalogoAdapter.setOnItemClickListener(ejercicioCatalogo -> {
            if (rutinaId != -1) {
                Ejercicio nuevoEjercicio = new Ejercicio();
                nuevoEjercicio.rutinaId = rutinaId;
                nuevoEjercicio.nombre = ejercicioCatalogo.nombre;

                AppDatabase.databaseWriteExecutor.execute(() -> {
                    AppDatabase.getDatabase(getApplicationContext()).gimnasioDao().insertEjercicio(nuevoEjercicio);
                    runOnUiThread(this::finish);
                });
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
