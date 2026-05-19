package com.example.irongym;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "historial_entrenos")
public class HistorialEntreno {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int usuarioId;
    public String nombreRutina;
    public long fecha;
    public String duracionFormateada;
    public double volumenTotal;
    public String resumenEjercicios;
}
