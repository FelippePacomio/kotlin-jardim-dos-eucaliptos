package com.example.associacao_jardim_eucaliptos;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FragmentManager fragmentManager;
    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    private BottomAppBar bottomAppBar;
    private Toolbar toolbar;
    private NavigationView navigationView;
    FirebaseUser user;
    FirebaseAuth auth;
    private boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*--------------------- Initialize variables ---------------------*/
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.navigation_drawer);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomAppBar = findViewById(R.id.bottomAppBar);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {
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
        });

        fragmentManager = getSupportFragmentManager();
        replaceFragment(new HomeFragment());

        // Check if user is admin and then update the navigation view
        checkIfUserIsAdmin(isAdmin -> {
            MainActivity.this.isAdmin = isAdmin;
            Log.d("MainActivity", "isAdmin: " + isAdmin);
            updateNavigationView();
        });
        /* --------- Test: Is user logged? ---------
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null) {
            Log.d("MainActivity", "Usuario nao logado" + user);
        }
        else{
            Log.d("MainActivity", "Usuario logado" + user);
        } */
    }

    public void updateNavigationView() {
        Menu menu = navigationView.getMenu();
        MenuItem navGevents = menu.findItem(R.id.nav_gevents);
        MenuItem navGnews = menu.findItem(R.id.nav_gnews);
        MenuItem navLogin = menu.findItem(R.id.nav_login);

        if (isUserLoggedIn()) {
            navLogin.setVisible(false);
            navGevents.setVisible(isAdmin);
            navGnews.setVisible(isAdmin);
        } else {
            navLogin.setVisible(true);
            navGevents.setVisible(false);
            navGnews.setVisible(false);
        }
        Log.d("MainActivity", "Is UserLoggedIn? " + isUserLoggedIn());
        Log.d("MainActivity", "navLogin visible: " + navLogin.isVisible());
        Log.d("MainActivity", "navGevents visible: " + navGevents.isVisible());
        Log.d("MainActivity", "navGnews visible: " + navGnews.isVisible());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d("MainActivity", "onNavigationItemSelected: item selected");
        int itemId = item.getItemId();
        if (itemId == R.id.nav_login) {
            replaceFragment(new LoginFragment());
        } else if (itemId == R.id.nav_settings) {
            Toast.makeText(MainActivity.this, "Settings!", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.nav_share) {
            Toast.makeText(MainActivity.this, "Share!", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.nav_gevents) {
            replaceFragment(new ManageEventFragment());
        } else if (itemId == R.id.nav_gnews) {
            replaceFragment(new ManageNewsFragment());
        } else if (itemId == R.id.nav_about) {
            replaceFragment(new AboutUsFragment());
        }
        else {
            Log.e("MainActivity", "Unknown item selected with ID: " + itemId);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof LoginFragment) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        } else if (fragment instanceof ManageEventFragment) {
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
        bottomAppBar.setVisibility(View.GONE);
    }

    public void showToolbarAndBottomNavigation() {
        toolbar.setVisibility(View.VISIBLE);
        bottomAppBar.setVisibility(View.VISIBLE);
    }

    private boolean isUserLoggedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    private void checkIfUserIsAdmin(AdminCheckCallback callback) {
        if (isUserLoggedIn()) {
            Log.d("MainActivity", "admin: " + isAdmin);
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Boolean isAdmin = snapshot.child("admin").getValue(Boolean.class);
                        if (isAdmin == null) {
                            isAdmin = false;
                        }
                        Log.d("MainActivity", "isAdmin value from Firebase: " + isAdmin);
                        callback.onCheckComplete(isAdmin);
                    } else {
                        Log.d("MainActivity", "User snapshot does not exist");
                        callback.onCheckComplete(false);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.d("MainActivity", "Database error: " + error.getMessage());
                    callback.onCheckComplete(false);
                }
            });
        } else {
            callback.onCheckComplete(false);
        }
    }


    private interface AdminCheckCallback {
        void onCheckComplete(boolean isAdmin);
    }

   /* public void reloadFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.detach(fragment);
            fragmentTransaction.attach(fragment);
            fragmentTransaction.commit();
        }
    }*/
}
