package com.smartplant.smartplantandroid;

import android.app.Application;

import com.smartplant.smartplantandroid.core.data.db.CoreSqliteDBHelper;
import com.smartplant.smartplantandroid.core.network.ApplicationStatusCodes;
import com.smartplant.smartplantandroid.main.components.auth.repository.AuthRepositoryST;
import com.smartplant.smartplantandroid.main.components.devices.repository.DevicesRepositoryST;
import com.smartplant.smartplantandroid.main.components.devices.utils.DevicesLocalDataManagerST;
import com.smartplant.smartplantandroid.main.components.notifiactions.internal_utils.db.NotificationsDBTable;
import com.smartplant.smartplantandroid.main.components.notifiactions.repository.NotificationsRepositoryST;
import com.smartplant.smartplantandroid.main.components.sensors_data.internal_utils.db.SensorsDataDBTable;
import com.smartplant.smartplantandroid.main.components.sensors_data.repository.SensorsDataRepositoryST;
import com.smartplant.smartplantandroid.main.components.storage.repository.StorageRepositoryST;
import com.smartplant.smartplantandroid.main.state.settings.DevicesSettingsST;
import com.smartplant.smartplantandroid.main.state.settings.NetworkSettingsST;

public class SmartPlantApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        _initializeUtils();
        _initializeDB();
        _initializeRepositories();
    }

    private void _initializeDB() {
        CoreSqliteDBHelper.registerTable(new SensorsDataDBTable());
        CoreSqliteDBHelper.registerTable(new NotificationsDBTable());
        CoreSqliteDBHelper.createInstance(this);
    }

    private void _initializeUtils() {
        NetworkSettingsST.createInstance(this);
        DevicesSettingsST.createInstance(this);
        ApplicationStatusCodes.initialize(this);
        DevicesLocalDataManagerST.createInstance(this);
    }

    private void _initializeRepositories() {
        AuthRepositoryST.createInstance(this);
        StorageRepositoryST.createInstance();
        SensorsDataRepositoryST.createInstance();
        DevicesRepositoryST.createInstance();
        NotificationsRepositoryST.createInstance();
    }
}
