package com.example.autocompravenda.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Veiculo {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String marca;
    public String modelo;
    public int ano;
    public double preco;
    public String imagemUri;
    public int usuarioId;

    public boolean favorito;

    public Veiculo(String marca, String modelo, int ano, double preco, String imagemUri, int usuarioId) {
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.preco = preco;
        this.imagemUri = imagemUri;
        this.usuarioId = usuarioId;
    }
}
