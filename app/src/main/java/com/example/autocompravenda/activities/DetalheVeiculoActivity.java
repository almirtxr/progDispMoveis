package com.example.autocompravenda.activities;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.File;
import com.example.autocompravenda.R;
import com.example.autocompravenda.models.Veiculo;
import com.example.autocompravenda.models.DetalheVeiculoViewModel;

import java.io.InputStream;
import java.util.Locale;

public class DetalheVeiculoActivity extends AppCompatActivity {

    private TextView tvMarca, tvModelo, tvAno, tvPreco;
    private ImageView imgVeiculo;
    private Button btnFavoritar;

    private DetalheVeiculoViewModel viewModel;
    private boolean isFavoritoAtual; // Guarda o estado atual para o Toast

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_veiculo);

        // Inicializa o ViewModel
        viewModel = new ViewModelProvider(this).get(DetalheVeiculoViewModel.class);

        // Bind das views
        tvMarca = findViewById(R.id.tvMarca);
        tvModelo = findViewById(R.id.tvModelo);
        tvAno = findViewById(R.id.tvAno);
        tvPreco = findViewById(R.id.tvPreco);
        imgVeiculo = findViewById(R.id.imgVeiculo);
        btnFavoritar = findViewById(R.id.btnFavoritar);

        int veiculoId = getIntent().getIntExtra("veiculo_id", -1);
        if (veiculoId == -1) {
            Toast.makeText(this, "ID do veículo é inválido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Observa o LiveData do ViewModel
        viewModel.getVeiculoById(veiculoId).observe(this, veiculo -> {
            if (veiculo == null) {
                // Isso pode acontecer se o item for deletado enquanto a tela está aberta
                Toast.makeText(this, "Veículo não encontrado", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            // Quando os dados do veículo chegam, atualizamos a UI
            preencherDados(veiculo);
        });

        // Clique no botão para favoritar
        btnFavoritar.setOnClickListener(v -> {
            viewModel.alternarFavorito();
            // O Toast agora pode ser mais reativo
            String mensagem = !isFavoritoAtual ? "Adicionado aos favoritos" : "Removido dos favoritos";
            Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
        });
    }

    private void preencherDados(Veiculo veiculo) {
        this.isFavoritoAtual = veiculo.favorito;


        tvMarca.setText(getString(R.string.detalhe_marca, veiculo.marca));
        tvModelo.setText(getString(R.string.detalhe_modelo, veiculo.modelo));
        tvAno.setText(getString(R.string.detalhe_ano, veiculo.ano));

        tvPreco.setText(String.format(Locale.forLanguageTag("pt-BR"), "R$ %.2f", veiculo.preco));

        Log.d("DetalheVeiculo", "imagemUri: " + veiculo.imagemUri);
        if (veiculo.imagemUri != null && !veiculo.imagemUri.isEmpty()) {
            try {
                Uri uri = Uri.parse(veiculo.imagemUri);
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgVeiculo.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                imgVeiculo.setImageResource(R.drawable.ic_error_image);
            }
        } else {
            imgVeiculo.setImageResource(R.drawable.ic_auto_image);
        }


        atualizarTextoBotao();
    }

    private void atualizarTextoBotao() {
        if (isFavoritoAtual) {
            btnFavoritar.setText(R.string.btn_remover_favorito);
        } else {
            btnFavoritar.setText(R.string.btn_favoritar);
        }
    }
}