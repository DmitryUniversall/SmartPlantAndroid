package com.smartplant.smartplantandroid.ui.views.views;


import android.os.Bundle;

import androidx.annotation.Nullable;

import com.smartplant.smartplantandroid.auth.repository.AuthRepositoryST;
import com.smartplant.smartplantandroid.ui.views.views.main.MainActivity;
import com.smartplant.smartplantandroid.utils.ui.CustomAppCompatActivity;

public class InitActivity extends CustomAppCompatActivity {
    private AuthRepositoryST authRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.authRepository = AuthRepositoryST.getInstance();
        redirect();
    }

    private void redirect() {
        if (this.authRepository.isAuthenticated()) {
            startActivity(MainActivity.class);
        } else {
            startActivity(StartActivity.class);
        }
    }
}
