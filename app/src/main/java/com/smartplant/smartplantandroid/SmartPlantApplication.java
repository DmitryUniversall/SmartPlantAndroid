package com.smartplant.smartplantandroid;

import android.app.Application;

import com.smartplant.smartplantandroid.core.network.ApplicationStatusCodes;
import com.smartplant.smartplantandroid.main.components.auth.repository.AuthRepositoryST;
import com.smartplant.smartplantandroid.main.components.devices.repository.DevicesRepositoryST;
import com.smartplant.smartplantandroid.main.components.devices.utils.DevicesLocalDataManagerST;
import com.smartplant.smartplantandroid.main.components.sensors_data.repository.SensorsDataRepositoryST;
import com.smartplant.smartplantandroid.main.components.storage.repository.StorageRepositoryST;
import com.smartplant.smartplantandroid.main.state.settings.NetworkSettingsST;

public class SmartPlantApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        initializeUtils();
        initializeRepositories();
    }

    private void initializeUtils() {
        ApplicationStatusCodes.initialize(this);
        DevicesLocalDataManagerST.createInstance(this);
        NetworkSettingsST.createInstance(this);
    }

    private void initializeRepositories() {
        AuthRepositoryST.createInstance(this);
        SensorsDataRepositoryST.createInstance(this);
        StorageRepositoryST.createInstance();
        DevicesRepositoryST.createInstance();
    }
}
