package com.example.autocompravenda.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.autocompravenda.MainActivity;
import com.example.autocompravenda.R;
import com.example.autocompravenda.dao.UsuarioDao;
import com.example.autocompravenda.database.AppDatabase;
import com.example.autocompravenda.models.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etSenha;
    private Button btnLogin;
    private Button btnCadastro;
    private TextView tvCadastro;
    private UsuarioDao usuarioDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        // salva o ID do usuário para utilização posterior
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        prefs.edit().putInt("usuario_id", usuarioDao.id).apply();

    }
}
