package com.smartplant.smartplantandroid.main.ui.views.init;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.core.ui.CustomAppCompactActivity;
import com.smartplant.smartplantandroid.main.ui.views.main.MainActivity;
import com.smartplant.smartplantandroid.main.ui.views.start.StartActivity;

public class InitActivity extends CustomAppCompactActivity {
    private InitViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.init_activity);

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
        viewModel.getAuthenticatedLive().observe(this, isAuthenticated -> {
            if (isAuthenticated) {
                startNewActivity(MainActivity.class);
            } else {
                startNewActivity(StartActivity.class);
            }
        });
    }
}
