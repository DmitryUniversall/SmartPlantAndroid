package com.smartplant.smartplantandroid.main.ui.views.start;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.core.ui.CustomAppCompactActivity;
import com.smartplant.smartplantandroid.main.ui.views.auth.AuthActivity;

public class StartActivity extends CustomAppCompactActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);

        Button loginButton = findViewById(R.id.login_button);
        Button registerButton = findViewById(R.id.register_button);

        loginButton.setOnClickListener(this::onLoginBtnClick);
        registerButton.setOnClickListener(this::onRegisterBtnClick);
    }

    private void onLoginBtnClick(View view) {
        Bundle extras = new Bundle();
        extras.putString("authType", "login");
        _startNewActivity(AuthActivity.class, extras);
    }

    private void onRegisterBtnClick(View view) {
        Bundle extras = new Bundle();
        extras.putString("authType", "register");
        _startNewActivity(AuthActivity.class, extras);
    }
}
