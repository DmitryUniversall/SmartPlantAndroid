package com.smartplant.smartplantandroid.ui.views.views.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.ui.views.views.auth.fragments.LoginFragment;
import com.smartplant.smartplantandroid.ui.views.views.auth.fragments.RegisterFragment;
import com.smartplant.smartplantandroid.utils.ui.CustomAppCompatActivity;
import com.smartplant.smartplantandroid.utils.ui.components.CustomButton;

import java.util.Objects;

public class AuthActivity extends CustomAppCompatActivity {
    private CustomButton loginButton;
    private CustomButton registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_activity);

        Intent intent = getIntent();
        String selectedAuthType = intent.getStringExtra("authType");
        if (selectedAuthType == null) selectedAuthType = "login";

        loginButton = findViewById(R.id.buttonLogin);
        registerButton = findViewById(R.id.buttonRegister);

        loginButton.setOutline(!Objects.equals(selectedAuthType, "login"));
        registerButton.setOutline(!Objects.equals(selectedAuthType, "register"));

        loginButton.setOnClickListener(this::onLoginBtnClick);
        registerButton.setOnClickListener(this::onRegisterBtnClick);

        setFragment(Objects.equals(selectedAuthType, "login") ? new LoginFragment() : new RegisterFragment());

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                AuthActivity.super.onBackPressed();
            }
        });
    }

    private void setFragment(Fragment fragment) {
        replaceFragment(R.id.fragment_container_view, fragment);
    }

    private void onLoginBtnClick(View view) {
        setFragment(new LoginFragment());
        loginButton.setOutline(false);
        registerButton.setOutline(true);
    }

    private void onRegisterBtnClick(View view) {
        setFragment(new RegisterFragment());
        loginButton.setOutline(true);
        registerButton.setOutline(false);
    }
}
