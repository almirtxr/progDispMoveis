package com.example.autocompravenda.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;

import com.example.autocompravenda.R;
import com.example.autocompravenda.database.AppDatabase;
import com.example.autocompravenda.models.Veiculo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CadastroVeiculoActivity extends AppCompatActivity {

    private EditText etMarca, etModelo, etAno, etPreco;
    private Button btnCadastrar;
    private Button btnSelecionarImagem;
    private ImageView ivFoto;
    private String imagemUri = "";

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        imagemUri = uri.toString();
                        ivFoto.setImageURI(uri);
                    }
                }
            });

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null && result.getData().getExtras() != null) {
                    Bitmap foto = (Bitmap) result.getData().getExtras().get("data");
                    if (foto != null) {
                        Uri uri = salvarImagemEmArquivo(foto);
                        imagemUri = uri.toString();
                        ivFoto.setImageURI(uri);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_veiculo);


        etMarca = findViewById(R.id.etMarca);
        etModelo = findViewById(R.id.etModelo);
        etAno = findViewById(R.id.etAno);
        etPreco = findViewById(R.id.etPreco);
        btnCadastrar = findViewById(R.id.btnCadastrarVeiculo);
        ivFoto = findViewById(R.id.ivFoto);
        btnSelecionarImagem = findViewById(R.id.btnSelecionarImagem);


        btnSelecionarImagem.setOnClickListener(v -> mostrarDialogoSelecaoImagem());


        btnCadastrar.setOnClickListener(v -> cadastrarVeiculo());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "canal_anuncios",
                    "Notificações de Anúncios",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void mostrarDialogoSelecaoImagem() {
        String[] opcoes = {"Galeria", "Câmera"};
        new AlertDialog.Builder(this)
                .setTitle("Selecionar imagem de")
                .setItems(opcoes, (dialog, which) -> {
                    if (which == 0) {
                        abrirGaleria();
                    } else {
                        abrirCamera();
                    }
                })
                .show();
    }

    private void cadastrarVeiculo() {
        String marca = etMarca.getText().toString().trim();
        String modelo = etModelo.getText().toString().trim();
        String anoStr = etAno.getText().toString().trim();
        String precoStr = etPreco.getText().toString().trim();

        if (marca.isEmpty() || modelo.isEmpty() || anoStr.isEmpty() || precoStr.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        int usuarioId = prefs.getInt("usuario_id", -1);
        if (usuarioId == -1) {
            Toast.makeText(this, "Erro: Usuário não autenticado.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int ano = Integer.parseInt(anoStr);
            double preco = Double.parseDouble(precoStr);

            Veiculo veiculo = new Veiculo(marca, modelo, ano, preco, imagemUri, usuarioId);

            AppDatabase.getDatabase(this).veiculoDao().inserir(veiculo);
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.confirm_sound);
            mediaPlayer.start();

            Toast.makeText(this, "Veículo cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "canal_anuncios")
                    .setSmallIcon(R.drawable.ic_complete)
                    .setContentTitle("Anúncio Publicado")
                    .setContentText("Seu veículo foi cadastrado com sucesso!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            notificationManager.notify(1, builder.build());


        } catch (NumberFormatException e) {
            Toast.makeText(this, "Por favor, insira valores numéricos válidos para ano e preço.", Toast.LENGTH_LONG).show();
        }
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }

    private void abrirCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            cameraLauncher.launch(intent);
        } else {
            Toast.makeText(this, "Nenhum app de câmera encontrado.", Toast.LENGTH_SHORT).show();
        }
    }

    private Uri salvarImagemEmArquivo(Bitmap foto) {
        File diretorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String nomeArquivo = "veiculo_" + System.currentTimeMillis() + ".jpg";
        File arquivo = new File(diretorio, nomeArquivo);

        try (FileOutputStream out = new FileOutputStream(arquivo)) {
            foto.compress(Bitmap.CompressFormat.JPEG, 90, out);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao salvar imagem.", Toast.LENGTH_SHORT).show();
        }

        return FileProvider.getUriForFile(
                this,
                getApplicationContext().getPackageName() + ".provider",
                arquivo
        );
    }
}