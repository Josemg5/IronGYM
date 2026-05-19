package com.example.irongym;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "ejercicios",
        foreignKeys = @ForeignKey(entity = Rutina.class,
                parentColumns = "id",
                childColumns = "rutinaId",
                onDelete = ForeignKey.CASCADE),
        indices = {@androidx.room.Index("rutinaId")})
public class Ejercicio {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int rutinaId;
    public String nombre;
}
