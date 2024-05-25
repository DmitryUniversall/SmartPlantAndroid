package com.smartplant.smartplantandroid.ui.views.views.auth.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;
import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.utils.ui.CustomFragment;

public class LoginFragment extends CustomFragment {
    public static class LoginData {
        public final String username;
        public final String password;

        public LoginData(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.auth_fragment_login, container, false);
    }

    @Nullable
    public LoginData collectData() {
        View view = getView();
        if (view == null) return null;

        TextInputEditText usernameEditText = view.findViewById(R.id.usernameEdittext);
        TextInputEditText passwordEditText = view.findViewById(R.id.passwordEdittext);

        Editable usernameText = usernameEditText.getText();
        Editable passwordText = passwordEditText.getText();

        return new LoginData(
                usernameText == null ? "" : usernameText.toString(),
                passwordText == null ? "" : passwordText.toString()
        );
    }

    public void setErrorFor(@IdRes int id, String message) {
        TextInputEditText editText;
        if ((editText = findViewById(id)) == null) return;  // Not safe in case of bad id

        editText.setError(message);
    }
}
