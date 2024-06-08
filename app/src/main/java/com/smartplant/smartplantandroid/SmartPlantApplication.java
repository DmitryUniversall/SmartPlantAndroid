package com.smartplant.smartplantandroid;

import android.app.Application;

import com.smartplant.smartplantandroid.core.network.ApplicationStatusCodes;
import com.smartplant.smartplantandroid.main.components.devices.repository.DevicesRepositoryST;
import com.smartplant.smartplantandroid.main.components.devices.utils.DevicesLocalDataManagerST;
import com.smartplant.smartplantandroid.main.components.sensors_data.internal_utils.db.SensorsDataDBManagerST;
import com.smartplant.smartplantandroid.main.state.settings.NetworkSettingsST;
import com.smartplant.smartplantandroid.main.components.auth.repository.AuthRepositoryST;
import com.smartplant.smartplantandroid.main.components.storage.repository.StorageRepositoryST;

public class SmartPlantApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        initializeUtils();
        initializeDB();
        initializeRepositories();
    }

    private void initializeDB() {
        SensorsDataDBManagerST.createInstance(this);
    }

    private void initializeUtils() {
        ApplicationStatusCodes.initialize(this);
        DevicesLocalDataManagerST.createInstance(this);
        NetworkSettingsST.createInstance(this);
    }

    private void initializeRepositories() {
        AuthRepositoryST.createInstance(this);
        StorageRepositoryST.createInstance();
        DevicesRepositoryST.createInstance();
    }
}
