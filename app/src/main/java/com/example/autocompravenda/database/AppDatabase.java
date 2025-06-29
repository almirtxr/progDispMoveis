package com.example.autocompravenda.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.autocompravenda.dao.UsuarioDao;
import com.example.autocompravenda.dao.VeiculoDao;
import com.example.autocompravenda.models.Usuario;
import com.example.autocompravenda.models.Veiculo;

@Database(entities = {Usuario.class, Veiculo.class}, version = 1)

public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instancia;
    public abstract UsuarioDao usuarioDao();
    public abstract VeiculoDao veiculoDao();

    Context context;

    public static AppDatabase getDatabase(final Context context) {
        if (instancia == null) {
            instancia = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "auto_venda_db"
            ).allowMainThreadQueries().build(); // permitir em thread principal
        }
        return instancia;
    }
}
