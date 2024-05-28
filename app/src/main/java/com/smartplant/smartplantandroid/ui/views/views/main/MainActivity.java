package com.smartplant.smartplantandroid.ui.views.views.main;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.utils.ui.CustomAppCompatActivity;

public class MainActivity extends CustomAppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

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
        NavController navController =
                Navigation.findNavController(this, R.id.nav_host_fragment);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}

//        setContentView(R.layout.main_activity);
//        test = findViewById(R.id.testbtn);
//
//        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
//
//        StorageRepositoryST storageRepository = StorageRepositoryST.getInstance();
//        ActionProcessor actionProcessor = storageRepository.connect()
//                .onServerApplicationResponse(response -> {
//                    AppLogger.info("Got server application response");
//                })
//                .onDeviceApplicationResponse((dataMessage, response) -> {
//                    AppLogger.info("Got device application response");
//                });
//
//        test.setOnClickListener(v -> {
//            actionProcessor.createActionRequest(new StorageAction(1, null), "smartplant").onSuccess(((response, dataMessage) -> {
//                AppLogger.info("ACTION REQUEST SUCCESS");
//            })).onFailure((error -> {
//                AppLogger.info("ACTION REQUEST FAIL");
//            })).send();
//        });
