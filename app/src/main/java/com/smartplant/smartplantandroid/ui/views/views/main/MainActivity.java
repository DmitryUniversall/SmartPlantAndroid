package com.smartplant.smartplantandroid.ui.views.views.main;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.storage.models.StorageAction;
import com.smartplant.smartplantandroid.storage.network.actions.ActionProcessor;
import com.smartplant.smartplantandroid.storage.repository.StorageRepositoryST;
import com.smartplant.smartplantandroid.ui.viewmodels.main.MainViewModel;
import com.smartplant.smartplantandroid.utils.AppLogger;
import com.smartplant.smartplantandroid.utils.ui.CustomAppCompatActivity;

public class MainActivity extends CustomAppCompatActivity {
    private MainViewModel viewModel;
    Button test;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        test = findViewById(R.id.testbtn);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        StorageRepositoryST storageRepository = StorageRepositoryST.getInstance();
        ActionProcessor actionProcessor = storageRepository.connect()
                .onServerApplicationResponse(response -> {
                    AppLogger.info("Got server application response");
                })
                .onDeviceApplicationResponse((dataMessage, response) -> {
                    AppLogger.info("Got device application response");
                });

        test.setOnClickListener(v -> {
            actionProcessor.createActionRequest(new StorageAction(1, null), "smartplant").onSuccess(((response, dataMessage) -> {
                AppLogger.info("ACTION REQUEST SUCCESS");
            })).onFailure((error -> {
                AppLogger.info("ACTION REQUEST FAIL");
            })).send();
        });
    }
}
