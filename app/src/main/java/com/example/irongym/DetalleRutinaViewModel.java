package com.example.irongym;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class DetalleRutinaViewModel extends AndroidViewModel {

    private final GimnasioDao gimnasioDao;

    public DetalleRutinaViewModel(@NonNull Application application) {
        super(application);
        gimnasioDao = AppDatabase.getDatabase(application).gimnasioDao();
    }

    public LiveData<List<Ejercicio>> getEjercicios(int rutinaId) {
        return gimnasioDao.getEjerciciosPorRutina(rutinaId);
    }

    public void insertarEjercicio(Ejercicio ejercicio) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            gimnasioDao.insertEjercicio(ejercicio);
        });
    }

    public void eliminarEjercicio(Ejercicio ejercicio) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            gimnasioDao.eliminarEjercicio(ejercicio);
        });
    }

    public void insertarSerie(Serie serie, Runnable onComplete) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            gimnasioDao.insertSerie(serie);
            if (onComplete != null) {
                onComplete.run();
            }
        });
    }

    public interface OnPRCallback {
        void onResult(Double pr);
    }

    public void obtenerPR(String nombreEjercicio, OnPRCallback callback) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Double pr = gimnasioDao.getPR(nombreEjercicio);
            if (callback != null) {
                callback.onResult(pr);
            }
        });
    }

    public void actualizarSerie(Serie serie) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            gimnasioDao.actualizarSerie(serie);
        });
    }

    public void insertarHistorial(HistorialEntreno historial, Runnable onComplete) {
        int usuarioId = getApplication().getSharedPreferences("IronGymPrefs", android.content.Context.MODE_PRIVATE).getInt("usuarioId", -1);
        historial.usuarioId = usuarioId;
        AppDatabase.databaseWriteExecutor.execute(() -> {
            gimnasioDao.insertHistorial(historial);
            if (onComplete != null) {
                onComplete.run();
            }
        });
    }

    public void reiniciarSeriesDeRutina(int rutinaId, Runnable onComplete) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            gimnasioDao.reiniciarSeriesDeRutina(rutinaId);
            if (onComplete != null) {
                onComplete.run();
            }
        });
    }
}
