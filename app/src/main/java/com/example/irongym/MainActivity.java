package com.example.irongym;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EntrenamientoFragment()).commit();
            bottomNav.setSelectedItemId(R.id.nav_entrenamiento);
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.nav_inicio) {
                selectedFragment = new InicioFragment();
            } else if (itemId == R.id.nav_entrenamiento) {
                selectedFragment = new EntrenamientoFragment();
            } else if (itemId == R.id.nav_perfil) {
                selectedFragment = new PerfilFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }

            return true;
        });
    }
}