package com.example.irongym;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class InicioFragment extends Fragment {

    private InicioViewModel viewModel;
    private HistorialAdapter historialAdapter;

    public InicioFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);

        android.widget.TextView tvToolbarTitle = view.findViewById(R.id.tvToolbarTitle);
        if (tvToolbarTitle != null) {
            tvToolbarTitle.setText("Historial");
        }

        RecyclerView rvHistorial = view.findViewById(R.id.rvHistorial);
        historialAdapter = new HistorialAdapter();
        rvHistorial.setLayoutManager(new LinearLayoutManager(getContext()));
        rvHistorial.setAdapter(historialAdapter);

        viewModel = new ViewModelProvider(this).get(InicioViewModel.class);
        
        viewModel.getHistoriales().observe(getViewLifecycleOwner(), historiales -> {
            if (historiales != null) {
                historialAdapter.setHistoriales(historiales);
            }
        });

        return view;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.cargarHistoriales();
        }
    }
}
