package com.example.autocompravenda.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.autocompravenda.R;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.autocompravenda.databinding.ActivityMainBinding;
import com.example.autocompravenda.fragments.FavoritosFragment;
import com.example.autocompravenda.fragments.MeusAnunciosFragment;
import com.example.autocompravenda.fragments.PerfilFragment;
import com.example.autocompravenda.fragments.ProcurarFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Toolbar
        setSupportActionBar(binding.toolbar);

        // cadastrar veículo
        binding.fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CadastroVeiculoActivity.class);
            startActivity(intent);
        });

        // menu Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int id = item.getItemId();

            if (id == R.id.nav_anuncios) {
                fragment = new MeusAnunciosFragment();
            } else if (id == R.id.nav_procurar) {
                fragment = new ProcurarFragment();
            } else if (id == R.id.nav_favoritos) {
                fragment = new FavoritosFragment();
            } else if (id == R.id.nav_perfil) {
                fragment = new PerfilFragment();
            }

            if (fragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_fragment, fragment)
                        .commit();
            }

            return true;
        });
        bottomNav.setSelectedItemId(R.id.nav_procurar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            realizarLogout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void realizarLogout(){
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        prefs.edit().clear().apply();

        Toast.makeText(this, "Logout realizado", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}