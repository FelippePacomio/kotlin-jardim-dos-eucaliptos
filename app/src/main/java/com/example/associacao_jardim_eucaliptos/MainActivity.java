package com.example.associacao_jardim_eucaliptos;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
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
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ImageView userIcon;
    private TextView welcomeText, userIdForHeader;
    private boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProgressUtils.showProgressDialog(this);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                ProgressUtils.hideProgressDialog();
            }
        }, 3000);

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.navigation_drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        View headerView = navigationView.getHeaderView(0);
        userIcon = headerView.findViewById(R.id.userIcon);
        welcomeText = headerView.findViewById(R.id.welcomeText);
        userIdForHeader = headerView.findViewById(R.id.userId);

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

        auth = FirebaseAuth.getInstance();
        authStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Log.d("MainActivity", "User logged in: " + user.getUid());
                updateUI(user);
            } else {
                Log.d("MainActivity", "User not logged in");
                updateNavigationMenu(false, false);
            }
        };
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (auth != null && authStateListener != null) {
            auth.removeAuthStateListener(authStateListener);

        }
    }

    public void updateUI(FirebaseUser user) {

        Log.d("MainActivity", "updateUI called for user: " + user.getUid());
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                HelperClass userData = snapshot.getValue(HelperClass.class);
                if (userData != null) {
                    Log.d("MainActivity", "User data retrieved: " + userData.toString());
                    isAdmin = userData.admin;
                    updateNavigationMenu(true, isAdmin);
                    userIcon.setVisibility(View.VISIBLE);
                    welcomeText.setVisibility(View.VISIBLE);
                    userIdForHeader.setVisibility(View.VISIBLE);
                    userIdForHeader.setText(userData.getName());
                } else {
                    Log.d("MainActivity", "User data is null");
                    updateNavigationMenu(true, false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.w("MainActivity", "loadUser:onCancelled", error.toException());
            }
        });
    }

    public void updateNavigationMenu(boolean isLoggedIn, boolean isAdmin) {
        Log.d("MainActivity", "Updating navigation menu. isLoggedIn: " + isLoggedIn + ", isAdmin: " + isAdmin);
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_login).setVisible(!isLoggedIn);
        menu.findItem(R.id.nav_gevents).setVisible(isAdmin);
        menu.findItem(R.id.nav_gnews).setVisible(isAdmin);
        menu.findItem(R.id.nav_signout).setVisible(isLoggedIn || isAdmin);
        userIcon.setVisibility(isLoggedIn ? View.VISIBLE : View.GONE);
        welcomeText.setVisibility(isLoggedIn ? View.VISIBLE : View.GONE);
        userIdForHeader.setVisibility(isLoggedIn ? View.VISIBLE : View.GONE);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d("MainActivity", "onNavigationItemSelected: item selected");
        int itemId = item.getItemId();
        if (itemId == R.id.nav_login) {
            replaceFragment(new LoginFragment());
        } else if (itemId == R.id.nav_gevents) {
            replaceFragment(new ManageEventFragment());
        } else if (itemId == R.id.nav_gnews) {
            replaceFragment(new ManageNewsFragment());
        } else if (itemId == R.id.nav_about) {
            replaceFragment(new AboutUsFragment());
        } else if (itemId == R.id.nav_signout) {
            showLogoutConfirmationDialog();
        } else {
            Log.e("MainActivity", "Item desconhecido selecionado com o ID: " + itemId);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Sair")
                .setMessage("Tem certeza que deseja sair da conta?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    ProgressUtils.showProgressDialog(MainActivity.this);
                    FirebaseAuth.getInstance().signOut();
                    replaceFragment(new HomeFragment());
                    updateNavigationMenu(false, false);
                    ProgressUtils.hideProgressDialog();
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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
        } else if (fragment instanceof AboutUsFragment) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        } else if (fragment instanceof ManageNewsFragment) {
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
}
