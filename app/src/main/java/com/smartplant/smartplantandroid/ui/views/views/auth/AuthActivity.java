package com.smartplant.smartplantandroid.ui.views.views.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.ui.viewmodels.AuthViewModel;
import com.smartplant.smartplantandroid.ui.views.views.auth.fragments.LoginFragment;
import com.smartplant.smartplantandroid.ui.views.views.auth.fragments.RegisterFragment;
import com.smartplant.smartplantandroid.ui.views.views.main.MainActivity;
import com.smartplant.smartplantandroid.utils.AppLogger;
import com.smartplant.smartplantandroid.utils.ui.CustomAppCompatActivity;
import com.smartplant.smartplantandroid.utils.ui.components.CustomButton;

import java.util.Objects;

public class AuthActivity extends CustomAppCompatActivity {
    private enum ScreenState {
        LOGIN,
        REGISTER
    }

    private static final int[] loginAnimation = {R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left};
    private static final int[] registerAnimation = {R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right};

    private final RegisterFragment registerFragment = new RegisterFragment();
    private final LoginFragment loginFragment = new LoginFragment();

    private CustomButton loginButton;
    private CustomButton registerButton;
    private CustomButton submitButton;

    private ScreenState currentScreenState;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_activity);

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

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
    }

    private void onLoginBtnClick(View view) {
        this.setState(ScreenState.LOGIN);
    }

    private void onRegisterBtnClick(View view) {
        this.setState(ScreenState.REGISTER);
    }

    private void onSubmitBtnClick(View view) {
        if (this.currentScreenState == ScreenState.LOGIN) {
            this.login();
        } else {
            this.register();
        }
    }

    private void setState(ScreenState screenState) {
        if (screenState == currentScreenState) return;
        this.currentScreenState = screenState;

        loginButton.setOutline(!(currentScreenState == ScreenState.LOGIN));
        registerButton.setOutline(!(currentScreenState == ScreenState.REGISTER));
        submitButton.setText(currentScreenState == ScreenState.LOGIN ? getString(R.string.loginAction) : getString(R.string.registerAction));
        setFragment(
                currentScreenState == ScreenState.LOGIN ? loginFragment : registerFragment,
                currentScreenState == ScreenState.LOGIN ? loginAnimation : registerAnimation
        );
    }

    void setFragment(Fragment fragment, int[] animation) {
        replaceFragment(R.id.fragmentContainerView, fragment, animation);
    }

    private void register() {
        submitButton.setEnabled(false);

        RegisterFragment.RegisterData registerData = registerFragment.collectData();
        if (registerData == null) return;

        // TODO: Validation

        this.viewModel.sendRegister(registerData.username, registerData.password).observe(this, authResult -> {
            AppLogger.info("Register OK: %b", authResult.ok);
            AppLogger.info("Register APPLICATION: %d", authResult.applicationStatusCode);
            AppLogger.info("Register MESSAGE: %d", authResult.message);
            Toast.makeText(this, authResult.message, Toast.LENGTH_LONG).show();

            if (authResult.applicationStatusCode == 2001) {  // Username already exists
                registerFragment.setErrorFor(R.id.usernameEdittext, getText(authResult.message).toString());
            }

            submitButton.setEnabled(true);
            if (authResult.ok) startNewActivity(MainActivity.class);
        });
    }

    private void login() {
        submitButton.setEnabled(false);

        LoginFragment.LoginData loginData = loginFragment.collectData();
        if (loginData == null) return;

        // TODO: Validation

        this.viewModel.sendLogin(loginData.username, loginData.password).observe(this, authResult -> {
            AppLogger.info("Login OK: %b", authResult.ok);
            AppLogger.info("Login APPLICATION: %d", authResult.applicationStatusCode);
            AppLogger.info("Login MESSAGE: %d", authResult.message);
            Toast.makeText(this, authResult.message, Toast.LENGTH_LONG).show();

            if (authResult.applicationStatusCode == 3006) {  // Wrong auth credentials
                loginFragment.setErrorFor(R.id.usernameEdittext, getText(authResult.message).toString());
                loginFragment.setErrorFor(R.id.passwordEdittext, getText(authResult.message).toString());
            }

            submitButton.setEnabled(true);
            if (authResult.ok) startNewActivity(MainActivity.class);
        });
    }
}
