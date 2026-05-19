package com.example.irongym;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "series",
        foreignKeys = @ForeignKey(entity = Ejercicio.class,
                parentColumns = "id",
                childColumns = "ejercicioId",
                onDelete = ForeignKey.CASCADE),
        indices = {@androidx.room.Index("ejercicioId")})
public class Serie {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int ejercicioId;
    public double peso;
    public int repeticiones;
    public boolean completado;
}
