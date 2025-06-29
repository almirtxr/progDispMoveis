package com.example.autocompravenda.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.autocompravenda.R;
import com.example.autocompravenda.activities.DetalheVeiculoActivity;
import com.example.autocompravenda.adapters.VeiculoAdapter;
import com.example.autocompravenda.database.AppDatabase;
import com.example.autocompravenda.models.Veiculo;

import java.util.List;

public class ProcurarFragment extends Fragment {

    private RecyclerView recyclerView;
    private VeiculoAdapter veiculoAdapter;
    private List<Veiculo> listaVeiculos;
    private AppDatabase db;
    private EditText etMarca, etAno, etPreco;
    private Button btnFiltrar;

    public ProcurarFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_procurar, container, false);

        db = AppDatabase.getDatabase(getContext());

        etMarca = view.findViewById(R.id.etFiltroMarca);
        etAno = view.findViewById(R.id.etFiltroAno);
        etPreco = view.findViewById(R.id.etFiltroPreco);
        btnFiltrar = view.findViewById(R.id.btnAplicarFiltros);

        recyclerView = view.findViewById(R.id.rvVeiculosProcurar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        btnFiltrar.setOnClickListener(v -> aplicarFiltros());

        carregarTodos();

        return view;
    }

    private void carregarTodos() {
        listaVeiculos = db.veiculoDao().listarTodos();
        atualizarRecycler();
    }

    private void aplicarFiltros() {
        String marca = etMarca.getText().toString().trim();
        String anoStr = etAno.getText().toString().trim();
        String precoStr = etPreco.getText().toString().trim();

        int ano = anoStr.isEmpty() ? 0 : Integer.parseInt(anoStr);
        double preco = precoStr.isEmpty() ? 0.0 : Double.parseDouble(precoStr);

        if (marca.isEmpty()) marca = null;

        listaVeiculos = db.veiculoDao().filtrar(marca, ano, preco);
        atualizarRecycler();
    }
    private void atualizarRecycler() {
        veiculoAdapter = new VeiculoAdapter(listaVeiculos, getContext(), veiculo -> {
            Intent intent = new Intent(getContext(), DetalheVeiculoActivity.class);
            intent.putExtra("veiculo_id", veiculo.id);
            startActivity(intent);
        });

        recyclerView.setAdapter(veiculoAdapter);
    }

    private void carregarVeiculos() {
        listaVeiculos = db.veiculoDao().listarTodos();
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
