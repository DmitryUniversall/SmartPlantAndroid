package com.smartplant.smartplantandroid.main.ui.views.main;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.core.ui.CustomAppCompactActivity;

public class MainActivity extends CustomAppCompactActivity {
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setupNavigation();

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        connectStorageWS();
    }

    private void connectStorageWS() {
        this.viewModel.connectStorageWS();
    }

    private void setupNavigation() {
        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the BottomNavigationView
        BottomNavigationView navView = findViewById(R.id.bottom_navigation);

        // Find the NavHostFragment and retrieve the NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_devices, R.id.nav_settings, R.id.nav_notifications
            ).build();
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(navView, navController);
        } else {
            throw new IllegalStateException("NavHostFragment is null");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}
