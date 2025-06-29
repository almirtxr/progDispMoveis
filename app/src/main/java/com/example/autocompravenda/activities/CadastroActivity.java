package com.example.autocompravenda.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.autocompravenda.R;
import com.example.autocompravenda.dao.UsuarioDao;
import com.example.autocompravenda.database.AppDatabase;
import com.example.autocompravenda.models.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private EditText etNome, etEmail, etSenha;
    private Button btnCadastrar;
    private Button btnFazerLogin;
    private UsuarioDao usuarioDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        etNome = findViewById(R.id.etNome);
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        btnFazerLogin = findViewById(R.id.btnFazerLogin);

        usuarioDao = AppDatabase.getDatabase(this).usuarioDao();

        btnCadastrar.setOnClickListener(view -> {
            String nome = etNome.getText().toString();
            String email = etEmail.getText().toString();
            String senha = etSenha.getText().toString();

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()){
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (usuarioDao.buscarPorEmail(email) != null){
                Toast.makeText(this, "Esse e-mail já está sendo usado", Toast.LENGTH_SHORT).show();
                return;
            }

            usuarioDao.inserir(new Usuario(nome, email, senha));
            Toast.makeText(this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(this, LoginActivity.class));
            // volta para o Login
        });

        btnFazerLogin.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
        });
    }
}