package com.example.irongym;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UsuarioDao {

    @Insert
    void insertUsuario(Usuario usuario);

    @Query("SELECT * FROM usuarios WHERE usuario = :usuario AND password = :password LIMIT 1")
    Usuario login(String usuario, String password);

    @Query("SELECT COUNT(*) FROM usuarios WHERE usuario = :username")
    int comprobarUsuarioExiste(String username);
}
