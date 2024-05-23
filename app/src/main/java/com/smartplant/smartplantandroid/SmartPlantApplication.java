package com.smartplant.smartplantandroid;

import android.app.Application;

import com.smartplant.smartplantandroid.auth.repository.AuthRepositoryST;

public class SmartPlantApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initializeRepositories();
    }

    private void initializeRepositories() {
        AuthRepositoryST.createInstance(this);
    }
}
