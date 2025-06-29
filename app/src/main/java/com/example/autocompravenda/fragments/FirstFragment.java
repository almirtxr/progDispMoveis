package com.example.autocompravenda.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.*;

import com.example.autocompravenda.R;
import com.example.autocompravenda.activities.DetalheVeiculoActivity;
import com.example.autocompravenda.adapters.VeiculoAdapter;
import com.example.autocompravenda.database.AppDatabase;
import com.example.autocompravenda.models.Veiculo;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment implements VeiculoAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private VeiculoAdapter adapter;

    @Override
    public void onItemClick(Veiculo veiculo) {
        // Cria uma Intent para abrir a tela de detalhes
        Intent intent = new Intent(getActivity(), DetalheVeiculoActivity.class);
        // Passa o ID do veículo clicado para a próxima tela
        intent.putExtra("veiculo_id", veiculo.id);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_first, container, false);
        recyclerView = root.findViewById(R.id.recyclerViewVeiculos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        carregarVeiculos();

        return root;
    }

    private void carregarVeiculos() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("app_prefs", getContext().MODE_PRIVATE);
        int usuarioId = prefs.getInt("usuario_id", -1);

        List<Veiculo> veiculos = AppDatabase.getDatabase(getContext())
                .veiculoDao()
                .listarTodos();
        if (adapter == null) {
            adapter = new VeiculoAdapter(new ArrayList<>(veiculos), getContext(), this);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.getVeiculos().clear();
            adapter.getVeiculos().addAll(veiculos);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        carregarVeiculos();
    }
}
