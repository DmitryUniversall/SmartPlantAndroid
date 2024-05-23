package com.smartplant.smartplantandroid;

import android.app.Application;

import com.smartplant.smartplantandroid.auth.repository.AuthRepositoryST;
import com.smartplant.smartplantandroid.utils.settings.ProjectSettingsST;

public class SmartPlantApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initializeSettings();
        initializeRepositories();
    }

    private void initializeSettings() {
        ProjectSettingsST.createInstance(this);
    }

    private void initializeRepositories() {
        AuthRepositoryST.createInstance(this);
    }
}
