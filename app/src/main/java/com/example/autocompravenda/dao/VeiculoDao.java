package com.example.autocompravenda.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.autocompravenda.models.Veiculo;
import java.util.List;

@Dao
public interface VeiculoDao {
    @Insert
    void inserir(Veiculo veiculo);

    @Query("SELECT * FROM Veiculo WHERE usuarioId = :usuarioId")
    List<Veiculo> listarPorUsuario(int usuarioId);

    @Query("SELECT * FROM Veiculo")
    List<Veiculo> listarTodos();

    @Query("SELECT * FROM Veiculo WHERE favorito = 1 AND usuarioId = :usuarioId")
    List<Veiculo> listarFavoritos(int usuarioId);

    @Query("UPDATE Veiculo SET favorito = :favorito WHERE id = :veiculoId")
    void atualizarFavorito(int veiculoId, boolean favorito);

    @Query("SELECT * FROM Veiculo WHERE id = :id")
    Veiculo buscarPorId(int id);

    @Query("SELECT * FROM Veiculo WHERE id = :id")
    LiveData<Veiculo> buscarPorIdLiveData(int id);

    @Query("SELECT * FROM Veiculo WHERE " +
            "(:marca IS NULL OR marca LIKE '%' || :marca || '%') AND " +
            "(:ano = 0 OR ano = :ano) AND " +
            "(:precoMax = 0 OR preco <= :precoMax)")
    List<Veiculo> filtrar(String marca, int ano, double precoMax);


}
