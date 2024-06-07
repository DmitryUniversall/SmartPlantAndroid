package com.smartplant.smartplantandroid.main.ui.views.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.core.ui.CustomAppCompactActivity;
import com.smartplant.smartplantandroid.main.ui.items.button.CustomButton;
import com.smartplant.smartplantandroid.main.ui.views.auth.fragments.AuthLoginFragment;
import com.smartplant.smartplantandroid.main.ui.views.auth.fragments.AuthRegisterFragment;
import com.smartplant.smartplantandroid.main.ui.views.main.MainActivity;

import java.util.Objects;

public class AuthActivity extends CustomAppCompactActivity {
    private enum ScreenState {
        LOGIN,
        REGISTER
    }

    private static final int[] loginAnimation = {R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left};
    private static final int[] registerAnimation = {R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right};

    private final AuthRegisterFragment registerFragment = new AuthRegisterFragment();
    private final AuthLoginFragment loginFragment = new AuthLoginFragment();

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

        loginButton = findViewById(R.id.login_button);
        registerButton = findViewById(R.id.register_button);
        submitButton = findViewById(R.id.submit_button);

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
        submitButton.setText(currentScreenState == ScreenState.LOGIN ? getString(R.string.login_action) : getString(R.string.register_action));
        setFragment(
                currentScreenState == ScreenState.LOGIN ? loginFragment : registerFragment,
                currentScreenState == ScreenState.LOGIN ? loginAnimation : registerAnimation
        );
    }

    void setFragment(Fragment fragment, int[] animation) {
        replaceFragment(R.id.auth_fragment_container, fragment, animation);
    }

    private void register() {
        submitButton.setEnabled(false);

        AuthRegisterFragment.RegisterData registerData = registerFragment.collectData();
        if (registerData == null) return;

        // TODO: Validation

        this.viewModel.sendRegister(registerData.username, registerData.password).observe(this, authResult -> {
            AppLogger.info("Register OK: %b", authResult.ok);
            AppLogger.info("Register STATUS: %d", authResult.statusCode.getCode());
            AppLogger.info("Register MESSAGE: %s", authResult.statusCode.getMessage());

            String translatedText = authResult.statusCode.getTranslatedMessage();
            Toast.makeText(this, translatedText, Toast.LENGTH_LONG).show();

            if (authResult.statusCode.getCode() == 2001) {  // Username already exists
                registerFragment.setErrorFor(R.id.usernameEdittext, translatedText);
            }

            submitButton.setEnabled(true);
            if (authResult.ok) startNewActivity(MainActivity.class);
        });
    }

    private void login() {
        submitButton.setEnabled(false);

        AuthLoginFragment.LoginData loginData = loginFragment.collectData();
        if (loginData == null) return;

        // TODO: Validation

        this.viewModel.sendLogin(loginData.username, loginData.password).observe(this, authResult -> {
            AppLogger.info("Login OK: %b", authResult.ok);
            AppLogger.info("Login STATUS: %d", authResult.statusCode.getCode());
            AppLogger.info("Login MESSAGE: %s", authResult.statusCode.getMessage());

            String translatedText = authResult.statusCode.getTranslatedMessage();  // TODO: Custom message
            Toast.makeText(this, translatedText, Toast.LENGTH_LONG).show();

            if (authResult.statusCode.getCode() == 3006) {  // Wrong auth credentials
                loginFragment.setErrorFor(R.id.usernameEdittext, translatedText);
                loginFragment.setErrorFor(R.id.passwordEdittext, translatedText);
            }

            submitButton.setEnabled(true);
            if (authResult.ok) startNewActivity(MainActivity.class);
        });
    }
}
