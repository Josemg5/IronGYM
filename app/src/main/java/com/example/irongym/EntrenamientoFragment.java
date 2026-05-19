package com.example.irongym;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EntrenamientoFragment extends Fragment {

    private EntrenamientoViewModel viewModel;
    private RutinaAdapter rutinaAdapter;

    public EntrenamientoFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entrenamiento, container, false);

        TextView tvToolbarTitle = view.findViewById(R.id.tvToolbarTitle);
        if (tvToolbarTitle != null) {
            tvToolbarTitle.setText("Entrenamiento");
        }

        RecyclerView rvRutinas = view.findViewById(R.id.rvRutinas);
        Button btnNuevaRutina = view.findViewById(R.id.btnNuevaRutina);

        rutinaAdapter = new RutinaAdapter();
        rvRutinas.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRutinas.setAdapter(rutinaAdapter);

        viewModel = new ViewModelProvider(this).get(EntrenamientoViewModel.class);

        viewModel.getRutinas().observe(getViewLifecycleOwner(), rutinas -> {
            if (rutinas != null) {
                rutinaAdapter.setRutinas(rutinas);
            }
        });

        btnNuevaRutina.setOnClickListener(v -> mostrarDialogoRutina(null));

        rutinaAdapter.setOnItemClickListener(rutina -> {
            android.content.Intent intent = new android.content.Intent(getContext(), DetalleRutinaActivity.class);
            intent.putExtra("RUTINA_ID", rutina.id);
            intent.putExtra("RUTINA_NOMBRE", rutina.nombre);
            startActivity(intent);
        });

        rutinaAdapter.setOnItemLongClickListener(rutina -> {
            String[] opciones = {"Modificar nombre", "Eliminar"};
            new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Opciones")
                    .setItems(opciones, (dialog, which) -> {
                        if (which == 0) {
                            mostrarDialogoRutina(rutina);
                        } else if (which == 1) {
                            viewModel.eliminarRutina(rutina);
                        }
                    })
                    .show();
        });

        return view;
    }

    private void mostrarDialogoRutina(Rutina rutinaExistente) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_rutina, null);
        TextView tvDialogTitle = dialogView.findViewById(R.id.tvDialogTitle);
        EditText etDialogRutinaNombre = dialogView.findViewById(R.id.etDialogRutinaNombre);
        Button btnDialogCancel = dialogView.findViewById(R.id.btnDialogCancel);
        Button btnDialogSave = dialogView.findViewById(R.id.btnDialogSave);

        if (rutinaExistente != null) {
            tvDialogTitle.setText("Modificar Rutina");
            etDialogRutinaNombre.setText(rutinaExistente.nombre);
        } else {
            tvDialogTitle.setText("Nueva Rutina");
        }

        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create();

        btnDialogCancel.setOnClickListener(v -> dialog.dismiss());

        btnDialogSave.setOnClickListener(v -> {
            String nombre = etDialogRutinaNombre.getText().toString().trim();
            if (!nombre.isEmpty()) {
                if (rutinaExistente != null) {
                    rutinaExistente.nombre = nombre;
                    viewModel.actualizarRutina(rutinaExistente);
                } else {
                    Rutina nuevaRutina = new Rutina();
                    nuevaRutina.nombre = nombre;
                    viewModel.insertarRutina(nuevaRutina);
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
