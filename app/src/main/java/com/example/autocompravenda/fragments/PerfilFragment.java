package com.example.autocompravenda.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.autocompravenda.R;
import com.example.autocompravenda.database.AppDatabase;
import com.example.autocompravenda.models.Usuario;
import java.io.File;
import java.io.IOException;


public class PerfilFragment extends Fragment {
    private static final int REQUEST_CAMERA = 1;
    private Uri fotoUri;
    private ImageView ivFotoPerfil;
    private File fotoFile;
    private TextView tvNomeUsuario;
    private AppDatabase db;
    public PerfilFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        ivFotoPerfil = view.findViewById(R.id.ivFotoPerfil);
        Button btnFotoCamera = view.findViewById(R.id.btnFotoCamera);
        tvNomeUsuario = view.findViewById(R.id.tvNomeUsuario);

        btnFotoCamera.setOnClickListener(v -> abrirCamera());

        return view;
    }

    private void abrirCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            try {
                fotoFile = criarArquivoImagem();
                if (fotoFile != null) {
                    fotoUri = FileProvider.getUriForFile(
                            getContext(),
                            getActivity().getPackageName() + ".provider",
                            fotoFile
                    );
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
                    startActivityForResult(intent, REQUEST_CAMERA);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private File criarArquivoImagem() throws IOException {
        String nomeArquivo = "foto_perfil";
        File diretorio = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(nomeArquivo, ".jpg", diretorio);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            ivFotoPerfil.setImageURI(fotoUri);

            SharedPreferences prefs = getContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
            int usuarioId = prefs.getInt("usuario_id", -1);

            if (usuarioId != -1) {
                AppDatabase.getDatabase(getContext())
                        .usuarioDao()
                        .atualizarFotoPerfil(usuarioId, fotoUri.toString());
            }
        }
    }



    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences prefs = getContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        int usuarioId = prefs.getInt("usuario_id", -1);

        if (usuarioId != -1) {
            Usuario usuario = AppDatabase.getDatabase(getContext()).usuarioDao().buscarPorId(usuarioId);

            if (usuario != null) {
                tvNomeUsuario.setText("Olá, " + usuario.nome);

                if (usuario.fotoPerfilUri != null) {
                    ivFotoPerfil.setImageURI(Uri.parse(usuario.fotoPerfilUri));
                }
            }
        }
    }

}
