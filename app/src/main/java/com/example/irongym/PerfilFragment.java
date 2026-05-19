package com.example.irongym;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PerfilFragment extends Fragment {

    public PerfilFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        TextView tvToolbarTitle = view.findViewById(R.id.tvToolbarTitle);
        if (tvToolbarTitle != null) {
            tvToolbarTitle.setText("Mi Perfil");
        }

        TextView tvPerfilNombre = view.findViewById(R.id.tvPerfilNombre);
        TextView tvStatEntrenos = view.findViewById(R.id.tvStatEntrenos);
        TextView tvStatVolumen = view.findViewById(R.id.tvStatVolumen);
        TextView tvStatRutinas = view.findViewById(R.id.tvStatRutinas);
        TextView tvStatUltimo = view.findViewById(R.id.tvStatUltimo);
        Button btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);

        SharedPreferences prefs = requireActivity().getSharedPreferences("IronGymPrefs", Context.MODE_PRIVATE);
        String username = prefs.getString("nombreUsuario", "Usuario");
        int usuarioId = prefs.getInt("usuarioId", -1);

        tvPerfilNombre.setText(username);

        AppDatabase.databaseWriteExecutor.execute(() -> {
            GimnasioDao dao = AppDatabase.getDatabase(requireContext()).gimnasioDao();
            int totalEntrenos = dao.contarEntrenos(usuarioId);
            double volumenTotal = dao.sumarVolumenTotal(usuarioId);
            int totalRutinas = dao.contarRutinas(usuarioId);
            long ultimaFecha = dao.obtenerUltimaFecha(usuarioId);

            String fechaStr = "-";
            if (ultimaFecha > 0) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
                fechaStr = sdf.format(new java.util.Date(ultimaFecha));
            }

            final String finalFechaStr = fechaStr;

            requireActivity().runOnUiThread(() -> {
                tvStatEntrenos.setText(String.valueOf(totalEntrenos));
                tvStatVolumen.setText(volumenTotal + " kg");
                tvStatRutinas.setText(String.valueOf(totalRutinas));
                tvStatUltimo.setText(finalFechaStr);
            });
        });

        btnCerrarSesion.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return view;
    }
}
