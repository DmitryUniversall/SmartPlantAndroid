package com.smartplant.smartplantandroid.ui.views.views;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.ui.views.views.auth.AuthActivity;
import com.smartplant.smartplantandroid.utils.ui.CustomAppCompatActivity;


public class StartActivity extends CustomAppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);

        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(this::onLoginBtnClick);
        registerButton.setOnClickListener(this::onRegisterBtnClick);
    }

    private void onLoginBtnClick(View view) {
        Bundle extras = new Bundle();
        extras.putString("authType", "login");
        startNewActivity(AuthActivity.class, extras);
    }

    private void onRegisterBtnClick(View view) {
        Bundle extras = new Bundle();
        extras.putString("authType", "register");
        startNewActivity(AuthActivity.class, extras);
    }
}
