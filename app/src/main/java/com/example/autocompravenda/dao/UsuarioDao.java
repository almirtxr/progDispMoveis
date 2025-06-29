package com.example.autocompravenda.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.autocompravenda.models.Usuario;

import java.util.List;

@Dao
public interface UsuarioDao {

    @Insert
    void inserir(Usuario usuario);

    @Query("SELECT * FROM Usuario WHERE email = :email AND senha = :senha LIMIT 1")
    Usuario login(String email, String senha);

    @Query("SELECT * FROM Usuario WHERE email = :email LIMIT 1")
    Usuario buscarPorEmail(String email);

    @Query("SELECT * FROM Usuario")
    List<Usuario> listarTodos();
}
