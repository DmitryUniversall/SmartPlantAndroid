package com.smartplant.smartplantandroid.main.ui.views.init;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.core.ui.CustomAppCompactActivity;
import com.smartplant.smartplantandroid.main.components.notifiactions.utils.AndroidNotificationUtils;
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

        AndroidNotificationUtils.createNotificationChannel(this);
        _setupPermissions();
    }

    private void _setupPermissions() {
        AndroidNotificationUtils.requestNotificationPermissionIfNeeded(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == AndroidNotificationUtils.PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                AppLogger.info("Permission has been denied by user");
            } else {
                AppLogger.info("Permission has been granted by user");
            }
        }
    }

    private void checkAuthentication() {
        AppLogger.debug("Checking for user authentication");
        viewModel.checkAuthentication();
    }

    private void observerAuthenticated() {
        AppLogger.debug("Waiting for user authentication");
        viewModel.getAuthenticatedLive().observe(this, isAuthenticated -> {
            if (isAuthenticated) {
                _startNewActivity(MainActivity.class);
            } else {
                _startNewActivity(StartActivity.class);
            }
        });
    }
}
