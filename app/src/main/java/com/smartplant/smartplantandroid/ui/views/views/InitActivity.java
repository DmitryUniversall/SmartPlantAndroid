package com.smartplant.smartplantandroid.ui.views.views;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.smartplant.smartplantandroid.ui.viewmodels.InitViewModel;
import com.smartplant.smartplantandroid.ui.views.views.main.MainActivity;
import com.smartplant.smartplantandroid.utils.AppLogger;
import com.smartplant.smartplantandroid.utils.ui.CustomAppCompatActivity;


public class InitActivity extends CustomAppCompatActivity {
    private InitViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(InitViewModel.class);

        observerAuthenticated();
        checkAuthentication();
    }

    private void checkAuthentication() {
        AppLogger.debug("Checking for user authentication");
        viewModel.checkAuthentication();
    }

    private void observerAuthenticated() {
        AppLogger.debug("Waiting for user authentication");
        viewModel.getIsAuthenticated().observe(this, isAuthenticated -> {
            if (isAuthenticated) {
                startNewActivity(MainActivity.class);
            } else {
                startNewActivity(StartActivity.class);
            }
        });
    }
}
