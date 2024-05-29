package com.smartplant.smartplantandroid.ui.viewmodels.main.devices;

import static com.smartplant.smartplantandroid.utils.network.ApiHelper.getGson;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.smartplant.smartplantandroid.storage.models.SensorsData;
import com.smartplant.smartplantandroid.storage.models.StorageAction;
import com.smartplant.smartplantandroid.storage.network.actions.ActionProcessor;
import com.smartplant.smartplantandroid.storage.network.actions.request.ActionRequest;
import com.smartplant.smartplantandroid.storage.repository.StorageRepositoryST;
import com.smartplant.smartplantandroid.utils.AppLogger;

public class DeviceDetailViewModel extends ViewModel {
    private static final Gson _gson = getGson();

    private final StorageRepositoryST storageRepository;
    private final MutableLiveData<SensorsData> _sensorsData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _lampState = new MutableLiveData<>();
    private static boolean dataUpdateActionRegistered = false;  // TODO: Refactor this

    public DeviceDetailViewModel() {
        this.storageRepository = StorageRepositoryST.getInstance();
    }

    public MutableLiveData<SensorsData> getSensorsDataLive() {
        return _sensorsData;
    }

    public MutableLiveData<Boolean> getLampStateLive() {
        return _lampState;
    }

    public ActionProcessor getProcessor() {
        ActionProcessor actionProcessor = storageRepository.getProcessor();
        if (actionProcessor == null)
            throw new IllegalStateException("Unable to set lamp state: not connected");

        return actionProcessor;
    }

    private void registerSensorDataAction() {
        if (dataUpdateActionRegistered) return;

//        dataUpdateActionRegistered = true;  // TODO
        ActionProcessor actionProcessor = getProcessor();
        actionProcessor.onAction(StorageAction.DeviceActionType.SENSORS_DATA_UPDATE.getValue(), (dataMessage, storageAction) -> {
            SensorsData sensorsData = _gson.fromJson(storageAction.getData(), SensorsData.class);
            this._sensorsData.postValue(sensorsData);
        });
    }

    public ActionRequest requestDataUpdate() {
        ActionProcessor actionProcessor = getProcessor();
        registerSensorDataAction();

        StorageAction action = new StorageAction(StorageAction.ApplicationActionType.REQUEST_SENSORS_DATA_UPDATE.getValue(), null);
        return actionProcessor.createActionRequest(action, "smartplant")  // TODO: To config
                .onSuccess(((response, dataMessage) -> AppLogger.info("ACTION REQUEST requestDataUpdate SUCCESS")))
                .onFailure((error -> AppLogger.error("ACTION REQUEST requestDataUpdate FAIL", error)));
    }

    public ActionRequest setLampState(boolean newState) {
        ActionProcessor actionProcessor = getProcessor();

        JsonObject data = new JsonObject();
        data.addProperty("mode", newState ? 1 : 0);

        StorageAction action = new StorageAction(StorageAction.ApplicationActionType.COMMAND_LAMP.getValue(), data);

        return actionProcessor.createActionRequest(action, "smartplant")  // TODO: To config
                .onSuccess(((response, dataMessage) -> {
                    _lampState.postValue(newState);
                    AppLogger.info("ACTION REQUEST setLampState SUCCESS");
                }))
                .onFailure((error -> AppLogger.error("ACTION REQUEST setLampState FAIL", error)));
    }

    public ActionRequest water() {
        ActionProcessor actionProcessor = getProcessor();

        StorageAction action = new StorageAction(StorageAction.ApplicationActionType.COMMAND_WATER.getValue(), null);
        return actionProcessor.createActionRequest(action, "smartplant")  // TODO: To config
                .onSuccess(((response, dataMessage) -> AppLogger.info("ACTION REQUEST water SUCCESS")))
                .onFailure((error -> AppLogger.error("ACTION REQUEST water FAIL", error)));
    }
}
