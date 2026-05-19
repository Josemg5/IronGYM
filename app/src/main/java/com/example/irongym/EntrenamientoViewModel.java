package com.example.irongym;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class EntrenamientoViewModel extends AndroidViewModel {

    private final GimnasioDao gimnasioDao;
    private final MutableLiveData<List<Rutina>> rutinasLiveData = new MutableLiveData<>();

    public EntrenamientoViewModel(@NonNull Application application) {
        super(application);
        gimnasioDao = AppDatabase.getDatabase(application).gimnasioDao();
        cargarRutinas();
    }

    public LiveData<List<Rutina>> getRutinas() {
        return rutinasLiveData;
    }

    public void cargarRutinas() {
        int usuarioId = getApplication().getSharedPreferences("IronGymPrefs", android.content.Context.MODE_PRIVATE).getInt("usuarioId", -1);
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Rutina> rutinas = gimnasioDao.getTodasLasRutinas(usuarioId);
            rutinasLiveData.postValue(rutinas);
        });
    }

    public void insertarRutina(Rutina rutina) {
        int usuarioId = getApplication().getSharedPreferences("IronGymPrefs", android.content.Context.MODE_PRIVATE).getInt("usuarioId", -1);
        rutina.usuarioId = usuarioId;
        AppDatabase.databaseWriteExecutor.execute(() -> {
            gimnasioDao.insertRutina(rutina);
            cargarRutinas();
        });
    }

    public void actualizarRutina(Rutina rutina) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            gimnasioDao.actualizarRutina(rutina);
            cargarRutinas();
        });
    }

    public void eliminarRutina(Rutina rutina) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            gimnasioDao.eliminarRutina(rutina);
            cargarRutinas();
        });
    }
}
