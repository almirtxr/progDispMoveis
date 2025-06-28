package com.example.autocompravenda.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.autocompravenda.models.Veiculo;

import java.util.List;

@Dao
public class VeiculoDao {
    @Insert
    public void inserir(Veiculo veiculo) {

    }

    @Query("SELECT * FROM Veiculo WHERE usuarioId = :usuarioId")
    List<Veiculo> listarPorUsuario(int usuarioId) {
        return null;
    }

    @Query("SELECT * FROM Veiculo")
    List<Veiculo> listarTodos() {
        return null;
    }
}
