package com.example.autocompravenda.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.autocompravenda.R;
import com.example.autocompravenda.database.AppDatabase;
import com.example.autocompravenda.models.Veiculo;

public class CadastroVeiculoActivity extends AppCompatActivity {

    private EditText etMarca, etModelo, etAno, etPreco;
    private Button btnCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_veiculo);

        etMarca = findViewById(R.id.etMarca);
        etModelo = findViewById(R.id.etModelo);
        etAno = findViewById(R.id.etModelo);
        etPreco = findViewById(R.id.etPreco);
        btnCadastrar = findViewById(R.id.btnCadastrarVeiculo);

        btnCadastrar.setOnClickListener(v -> {
            String marca = etMarca.getText().toString();
            String modelo = etModelo.getText().toString();
            int ano = Integer.parseInt(etAno.getText().toString());
            double preco = Double.parseDouble(etPreco.getText().toString());

            // pegar o ID do usuário logado
            SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
            int usuarioId = prefs.getInt("usuario_Id", -1);
            if (usuarioId == -1){
                Toast.makeText(this, "Usuário não fez login", Toast.LENGTH_SHORT).show();
                return;
            }

            Veiculo veiculo = new Veiculo(marca, modelo, ano, preco, "", usuarioId);
            AppDatabase.getDatabase(this).veiculoDao().inserir(veiculo);

            Toast.makeText(this, "Veiculo cadastrado!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}