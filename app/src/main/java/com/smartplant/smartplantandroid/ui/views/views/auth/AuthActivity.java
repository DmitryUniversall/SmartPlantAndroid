package com.smartplant.smartplantandroid.ui.views.views.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.ui.views.views.auth.fragments.LoginFragment;
import com.smartplant.smartplantandroid.ui.views.views.auth.fragments.RegisterFragment;
import com.smartplant.smartplantandroid.utils.AppLogger;
import com.smartplant.smartplantandroid.utils.ui.CustomAppCompatActivity;
import com.smartplant.smartplantandroid.utils.ui.components.CustomButton;

import java.util.Objects;

public class AuthActivity extends CustomAppCompatActivity {
    private enum ScreenState {
        LOGIN,
        REGISTER;
    }

    private static final int[] loginAnimation = {R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left};
    private static final int[] registerAnimation = {R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right};

    private final RegisterFragment registerFragment = new RegisterFragment();
    private final LoginFragment loginFragment = new LoginFragment();

    private CustomButton loginButton;
    private CustomButton registerButton;
    private CustomButton submitButton;

    private ScreenState currentScreenState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_activity);

        Intent intent = getIntent();
        String selectedAuthType = intent.getStringExtra("authType");
        if (selectedAuthType == null) selectedAuthType = "login";

        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        submitButton = findViewById(R.id.submitButton);

        loginButton.setOnClickListener(this::onLoginBtnClick);
        registerButton.setOnClickListener(this::onRegisterBtnClick);
        submitButton.setOnClickListener(this::onSubmitBtnClick);

        this.setState(Objects.equals(selectedAuthType, "login") ? ScreenState.LOGIN : ScreenState.REGISTER);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                AuthActivity.super.onBackPressed();
            }
        });
    }

    private void onLoginBtnClick(View view) {
        this.setState(ScreenState.LOGIN);
    }

    private void onRegisterBtnClick(View view) {
        this.setState(ScreenState.REGISTER);
    }

    private void onSubmitBtnClick(View view) {
        AppLogger.debug("Submit");
    }

    private void setState(ScreenState screenState) {
        if (screenState == currentScreenState) return;
        this.currentScreenState = screenState;

        loginButton.setOutline(currentScreenState == ScreenState.LOGIN);
        registerButton.setOutline(currentScreenState == ScreenState.REGISTER);
        submitButton.setText(currentScreenState == ScreenState.LOGIN ? getString(R.string.loginAction) : getString(R.string.registerAction));
        setFragment(
                currentScreenState == ScreenState.LOGIN ? loginFragment : registerFragment,
                currentScreenState == ScreenState.LOGIN ? loginAnimation : registerAnimation
        );
    }

    void setFragment(Fragment fragment, int[] animation) {
        replaceFragment(R.id.fragmentContainerView, fragment, animation);
    }

    private void register(String username, String password) {
    }

    private void login(String username, String password) {
    }
}
