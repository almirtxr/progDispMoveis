package com.example.autocompravenda.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.autocompravenda.R;
import com.example.autocompravenda.dao.UsuarioDao;
import com.example.autocompravenda.database.AppDatabase;
import com.example.autocompravenda.models.Usuario;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etSenha;
    private Button btnLogin;
    private Button btnCadastro;
    private TextView tvCadastro;
    private UsuarioDao usuarioDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        int usuarioId = prefs.getInt("usuario_id", -1);

        if (usuarioId != -1) {
            // Usuário já tá logado, ir direto pra MainActivity
            startActivity(new Intent(this, MainActivity.class));
            finish(); // fecha a tela de login
            return;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        btnLogin = findViewById(R.id.btnLogin);
        tvCadastro = findViewById(R.id.tvCadastro);
        btnCadastro = findViewById(R.id.btnCadastro);

        usuarioDao = AppDatabase.getDatabase(this).usuarioDao();

        btnLogin.setOnClickListener(view -> {
            String email = etEmail.getText().toString();
            String senha = etSenha.getText().toString();

            Usuario usuario = usuarioDao.login(email, senha);
            if (usuario != null) {
                prefs.edit().putInt("usuario_id", usuario.id).apply();
                Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish(); // evita voltar pra tela de login
            } else {
                Toast.makeText(this, "Email ou senha inválidos", Toast.LENGTH_SHORT).show();
            }
        });

        btnCadastro.setOnClickListener(view -> {
            startActivity(new Intent(this, CadastroActivity.class));
        });
    }
}
