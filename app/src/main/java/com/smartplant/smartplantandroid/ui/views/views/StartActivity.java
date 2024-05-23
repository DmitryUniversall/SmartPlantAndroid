package com.smartplant.smartplantandroid.ui.views.views;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.auth.exceptions.AuthFailedException;
import com.smartplant.smartplantandroid.auth.models.User;
import com.smartplant.smartplantandroid.auth.repository.AuthRepositoryST;
import com.smartplant.smartplantandroid.utils.AppLogger;
import com.smartplant.smartplantandroid.utils.network.TransferResponse;

public class StartActivity extends AppCompatActivity {
    private AuthRepositoryST authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        this.authRepository = AuthRepositoryST.getInstance();
        this.testFunction();
    }

    private void testFunction() {
        User user;
        try {
            user = authRepository.login("master", "master");
        } catch (AuthFailedException e) {
            TransferResponse transferResponse = e.getTransferResponse();

            if (transferResponse != null) {
                AppLogger.error("Got auth error message: %s", transferResponse.getMessage());
            } else {
                throw new RuntimeException(e);
            }

            return;
        }

        AppLogger.verbose("User: %s (id %d)", user.getUsername(), user.getId());
    }
}