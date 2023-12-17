package com.example.budgetkuapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    //The Fragment
    private DashboardFragment dashboardFragment;
    private PengeluaranFragment pengeluaranFragment;
    private BudgetFragment budgetFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar=findViewById(R.id.app_toolbar);
        toolbar.setTitle("BudgetKu");
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle drawer_toggle=new ActionBarDrawerToggle(
                this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(drawer_toggle);
        drawer_toggle.syncState();

        bottomNavigationView=findViewById(R.id.bottomNavBar);
        frameLayout=findViewById(R.id.main_frame);
        NavigationView navigationView=findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);

        dashboardFragment=new DashboardFragment();
        pengeluaranFragment=new PengeluaranFragment();
        budgetFragment=new BudgetFragment();

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.dashboard) {
                    setFragment(dashboardFragment);
                    return true;
                } else if (itemId == R.id.addpengeluaran) {
                    setFragment(pengeluaranFragment);
                    return true;
                } else if (itemId == R.id.budget) {
                    setFragment(budgetFragment);
                    return true;
                }
                return false;
            }

        });
        bottomNavigationView.setSelectedItemId(R.id.dashboard);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        if(drawerLayout.isDrawerOpen(GravityCompat.END)){
            drawerLayout.closeDrawer(GravityCompat.END);
        }else{
            super.onBackPressed();
        }
    }

    public void displaySelectedListener(int itemId) {
        Fragment fragment = null;

        if (itemId == R.id.dashboard) {
            setFragment(dashboardFragment);
            return;
        } else if (itemId == R.id.pengeluaran) {
            setFragment(pengeluaranFragment);
            return;
        } else if (itemId == R.id.logout) {
            performLogout();
            return;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void performLogout() {
        // Clear user session or perform any necessary cleanup
        clearUserSession();

        DataHelper dataHelper = new DataHelper(this);
        dataHelper.logout();
        Intent logoutIntent = new Intent(this, MainActivity.class);
        startActivity(logoutIntent);

        finish();
    }

    private void clearUserSession() {
        SharedPreferences preferences = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.clear();
        editor.apply();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displaySelectedListener(item.getItemId());
        return true;
    }
}