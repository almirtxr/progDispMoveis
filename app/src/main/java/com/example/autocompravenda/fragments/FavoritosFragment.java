package com.example.autocompravenda.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.autocompravenda.R;
import com.example.autocompravenda.activities.DetalheVeiculoActivity;
import com.example.autocompravenda.adapters.VeiculoAdapter;
import com.example.autocompravenda.database.AppDatabase;
import com.example.autocompravenda.models.Veiculo;

import java.util.List;

public class FavoritosFragment extends Fragment {

    private RecyclerView recyclerView;
    private VeiculoAdapter veiculoAdapter;
    private List<Veiculo> listaVeiculos;
    private AppDatabase db;
    public FavoritosFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favoritos, container, false);
        recyclerView = view.findViewById(R.id.rvVeiculosFavoritos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        db = AppDatabase.getDatabase(getContext());
        carregarVeiculos();
        return view;
    }

    private void carregarVeiculos() {
        SharedPreferences prefs = getContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        int usuarioId = prefs.getInt("usuario_id", -1);

        listaVeiculos = db.veiculoDao().listarFavoritos(usuarioId);

        veiculoAdapter = new VeiculoAdapter(listaVeiculos, getContext(), veiculo -> {
            Intent intent = new Intent(getContext(), DetalheVeiculoActivity.class);
            intent.putExtra("veiculo_id", veiculo.id);
            startActivity(intent);
        });

        recyclerView.setAdapter(veiculoAdapter);
    }
    @Override
    public void onResume() {
        super.onResume();
        carregarVeiculos();
    }
}
