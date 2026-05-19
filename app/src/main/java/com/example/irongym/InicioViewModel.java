package com.example.irongym;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class InicioViewModel extends AndroidViewModel {

    private final GimnasioDao gimnasioDao;
    private final MutableLiveData<List<HistorialEntreno>> historialesLiveData = new MutableLiveData<>();

    public InicioViewModel(@NonNull Application application) {
        super(application);
        gimnasioDao = AppDatabase.getDatabase(application).gimnasioDao();
        cargarHistoriales();
    }

    public LiveData<List<HistorialEntreno>> getHistoriales() {
        return historialesLiveData;
    }

    public void cargarHistoriales() {
        int usuarioId = getApplication().getSharedPreferences("IronGymPrefs", Context.MODE_PRIVATE).getInt("usuarioId", -1);
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<HistorialEntreno> historiales = gimnasioDao.getTodosLosHistoriales(usuarioId);
            historialesLiveData.postValue(historiales);
        });
    }
}
