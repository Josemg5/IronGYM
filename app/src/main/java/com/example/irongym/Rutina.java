package com.example.irongym;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "rutinas")
public class Rutina {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int usuarioId;

    public String nombre;
}
