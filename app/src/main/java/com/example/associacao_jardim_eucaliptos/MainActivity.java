package com.example.associacao_jardim_eucaliptos;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*--------------------- Mudar a cor da barra de notificações ---------------------*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.toolbarJd));
        }


        /*--------------------- Inicialização de variáveis ---------------------*/

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_drawer);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    replaceFragment(new HomeFragment());
                    return true;
                } else if (itemId == R.id.events) {
                    replaceFragment(new EventsFragment());
                    return true;
                } else if (itemId == R.id.donations) {
                    replaceFragment(new DonationsFragment());
                    return true;
                } else if (itemId == R.id.news) {
                    replaceFragment(new NewsFragment());
                    return true;
                }
                return false;
            }
        });


        fragmentManager = getSupportFragmentManager();
        replaceFragment(new HomeFragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d("MainActivity", "onNavigationItemSelected: item selected");
        int itemId = item.getItemId();
        if (itemId == R.id.nav_login) {
            replaceFragment(new LoginFragment());
        } else if (itemId == R.id.nav_about) {
            // replaceFragment(new LoginFragment());
            Toast.makeText(MainActivity.this, "About us!", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.nav_settings) {
            // replaceFragment(new LoginFragment());
            Toast.makeText(MainActivity.this, "Settings!", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.nav_share) {
            // replaceFragment(new LoginFragment());
            Toast.makeText(MainActivity.this, "Share!", Toast.LENGTH_SHORT).show();
        } else {
            // Log para capturar IDs desconhecidos
            Log.e("MainActivity", "Unknown item selected with ID: " + itemId);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof LoginFragment) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        } else if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    public void hideToolbarAndBottomNavigation() {
        toolbar.setVisibility(View.GONE);
        bottomNavigationView.setVisibility(View.GONE);
    }

    public void showToolbarAndBottomNavigation() {
        toolbar.setVisibility(View.VISIBLE);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }


}
