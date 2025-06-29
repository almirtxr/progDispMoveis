package com.example.autocompravenda.models;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.autocompravenda.database.AppDatabase;
import com.example.autocompravenda.models.Veiculo;
import com.example.autocompravenda.dao.VeiculoDao;

public class DetalheVeiculoViewModel extends AndroidViewModel {

    private final VeiculoDao veiculoDao;
    private LiveData<Veiculo> veiculo;

    public DetalheVeiculoViewModel(@NonNull Application application) {
        super(application);
        veiculoDao = AppDatabase.getDatabase(application).veiculoDao();
    }

    public LiveData<Veiculo> getVeiculoById(int id) {
        if (veiculo == null) {
            veiculo = veiculoDao.buscarPorIdLiveData(id); // O DAO precisa retornar LiveData
        }
        return veiculo;
    }

    public void alternarFavorito() {
        Veiculo veiculoAtual = veiculo.getValue();
        if (veiculoAtual != null) {
            boolean novoStatus = !veiculoAtual.favorito;
            AppDatabase.getDatabase(getApplication())
                    .veiculoDao()
                    .atualizarFavorito(veiculoAtual.id, novoStatus);
        }
    }
}
