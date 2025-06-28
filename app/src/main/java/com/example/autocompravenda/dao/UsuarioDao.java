package com.example.autocompravenda.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.autocompravenda.models.Usuario;

@Dao
public abstract class UsuarioDao {
    public int id;

    @Insert
    public void inserir(Usuario usuario) {

    }

    @Query("SELECT * FROM Usuario WHERE email = :email AND senha = :senha LIMIT 1")
    public Usuario login(String email, String senha) {
        return null;
    }

    @Query("SELECT * FROM Usuario WHERE email = :email LIMIT 1")
    public Usuario buscarPorEmail(String email) {
        return null;
    }
}
