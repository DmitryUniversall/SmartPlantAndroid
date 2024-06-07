package com.smartplant.smartplantandroid.main.ui.views.auth.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;
import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.core.ui.CustomFragment;

public class AuthRegisterFragment extends CustomFragment {
    public static class RegisterData {
        public final String username;
        public final String password;
        public final String passwordConfirmation;

        public RegisterData(String username, String password, String passwordConfirmation) {
            this.username = username;
            this.password = password;
            this.passwordConfirmation = passwordConfirmation;
        }
    }

    public AuthRegisterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.auth_fragment_register, container, false);
    }

    @Nullable
    public RegisterData collectData() {
        View view = getView();
        if (view == null) return null;

        TextInputEditText usernameEditText = view.findViewById(R.id.usernameEdittext);
        TextInputEditText passwordEditText = view.findViewById(R.id.passwordEdittext);
        TextInputEditText passwordConfirmationEditText = view.findViewById(R.id.passwordConfirmationEdittext);

        Editable usernameText = usernameEditText.getText();
        Editable passwordText = passwordEditText.getText();
        Editable passwordConfirmationText = passwordConfirmationEditText.getText();

        return new RegisterData(
                usernameText == null ? "" : usernameText.toString(),
                passwordText == null ? "" : passwordText.toString(),
                passwordConfirmationText == null ? "" : passwordConfirmationText.toString()
        );
    }

    public void setErrorFor(@IdRes int id, String message) {  // TODO: move to base class
        TextInputEditText editText;
        if ((editText = findViewById(id)) == null) return;  // Not safe in case of bad id

        editText.setError(message);
    }
}
