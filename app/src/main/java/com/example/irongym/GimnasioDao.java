package com.example.irongym;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GimnasioDao {

    @Insert
    long insertRutina(Rutina rutina);

    @Insert
    void insertEjercicio(Ejercicio ejercicio);

    @Insert
    long insertSerie(Serie serie);

    @androidx.room.Update
    void actualizarRutina(Rutina rutina);

    @androidx.room.Delete
    void eliminarRutina(Rutina rutina);

    @androidx.room.Delete
    void eliminarEjercicio(Ejercicio ejercicio);

    @androidx.room.Delete
    void eliminarSerie(Serie serie);

    @Query("SELECT * FROM rutinas WHERE usuarioId = :usuarioId")
    List<Rutina> getTodasLasRutinas(int usuarioId);

    @Query("SELECT * FROM ejercicios WHERE rutinaId = :rutinaId")
    androidx.lifecycle.LiveData<java.util.List<Ejercicio>> getEjerciciosPorRutina(int rutinaId);

    @Query("SELECT * FROM ejercicios WHERE rutinaId = :rutinaId")
    List<Ejercicio> getEjerciciosPorRutinaSync(int rutinaId);

    @Query("SELECT * FROM series WHERE ejercicioId = :ejercicioId")
    List<Serie> getSeriesPorEjercicio(int ejercicioId);

    @Query("SELECT COUNT(id) FROM historial_entrenos WHERE usuarioId = :usuarioId")
    int contarEntrenos(int usuarioId);

    @Query("SELECT SUM(volumenTotal) FROM historial_entrenos WHERE usuarioId = :usuarioId")
    double sumarVolumenTotal(int usuarioId);

    @Query("SELECT COUNT(id) FROM rutinas WHERE usuarioId = :usuarioId")
    int contarRutinas(int usuarioId);

    @Query("SELECT MAX(fecha) FROM historial_entrenos WHERE usuarioId = :usuarioId")
    long obtenerUltimaFecha(int usuarioId);

    @androidx.room.Update
    void actualizarSerie(Serie serie);

    @Query("SELECT MAX(peso) FROM series INNER JOIN ejercicios ON series.ejercicioId = ejercicios.id WHERE ejercicios.nombre = :nombreEjercicio")
    Double getPR(String nombreEjercicio);

    @Query("SELECT s.* FROM series s INNER JOIN ejercicios e ON s.ejercicioId = e.id WHERE e.rutinaId = :rutinaId")
    List<Serie> getSeriesPorRutina(int rutinaId);

    @Insert
    void insertHistorial(HistorialEntreno historial);

    @Query("SELECT * FROM historial_entrenos WHERE usuarioId = :usuarioId ORDER BY fecha DESC")
    List<HistorialEntreno> getTodosLosHistoriales(int usuarioId);

    @Query("UPDATE series SET completado = 0 WHERE ejercicioId IN (SELECT id FROM ejercicios WHERE rutinaId = :rutinaId)")
    void reiniciarSeriesDeRutina(int rutinaId);
}
