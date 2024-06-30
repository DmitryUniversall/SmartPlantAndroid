package com.smartplant.smartplantandroid.main.components.cultivation_rules.utils;

import android.content.Context;

import androidx.annotation.NonNull;

import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.main.components.cultivation_rules.models.CultivationRules;
import com.smartplant.smartplantandroid.main.components.cultivation_rules.repository.CultivationRulesRepositoryST;
import com.smartplant.smartplantandroid.main.components.notifiactions.models.AbstractAppNotification;
import com.smartplant.smartplantandroid.main.components.notifiactions.repository.NotificationsRepositoryST;
import com.smartplant.smartplantandroid.main.components.notifiactions.utils.generics.AppNotificationFactory;
import com.smartplant.smartplantandroid.main.components.sensors_data.models.SensorsData;
import com.smartplant.smartplantandroid.main.components.sensors_data.repository.SensorsDataRepositoryST;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CultivationRulesObserverST {
    // Singleton
    protected static CultivationRulesObserverST _instance;

    // Utils
    private final NotificationsRepositoryST _notificationsRepository;
    private final SensorsDataRepositoryST _sensorsDataRepository;
    private final CultivationRulesRepositoryST _cultivationRulesRepository;

    // Other
    private final @NonNull Context _context;
    private final @NonNull Map<Integer, Date> _notificationsLastSendMap = new HashMap<>();

    public static synchronized void createInstance(Context context) {
        if (_instance != null)
            throw new RuntimeException("CultivationRulesObserverST has already been initialized");
        _instance = new CultivationRulesObserverST(context);
    }

    public static synchronized CultivationRulesObserverST getInstance() {
        if (_instance == null)
            throw new RuntimeException("CultivationRulesObserverST has not been initialized");
        return _instance;
    }

    private CultivationRulesObserverST(@NonNull Context context) {
        this._context = context;

        _cultivationRulesRepository = CultivationRulesRepositoryST.getInstance();
        _notificationsRepository = NotificationsRepositoryST.getInstance();
        _sensorsDataRepository = SensorsDataRepositoryST.getInstance();

        _sensorsDataRepository.onNewSensorsData("CultivationRulesObserver", this::onNewSensorsData);
        }

    public boolean _canSendNotification(@NonNull AbstractAppNotification notification) {
        if (!_notificationsLastSendMap.containsKey(notification.getNotificationType())) return true;

        Date lastSend = _notificationsLastSendMap.get(notification.getNotificationType());
        assert lastSend != null;
        Date now = new Date();

        return Math.abs(now.getTime() - lastSend.getTime()) > 3 * 60 * 1000; // > 3 minutes since last notification of this type
    }

    private void _sendNotification(@NonNull AbstractAppNotification notification) {
        if (!_canSendNotification(notification)) {
            AppLogger.debug("Ignoring notification type=%d", notification.getNotificationType());
            return;
        };

        _notificationsLastSendMap.put(notification.getNotificationType(), new Date());
        this._notificationsRepository.sendAppNotification(notification).execute();
    }

    private void _checkHumidity(int deviceId, int humidityPercent) {
        CultivationRules rules = _cultivationRulesRepository.getCultivationRules(deviceId);

        if (rules.getMinHumidityPercent() >= humidityPercent) {
            this._sendNotification(
                    AppNotificationFactory.createNotification(
                            2,  // LowHumidityAppNotification
                            this._context,
                            deviceId,
                            false,
                            null,
                            null,
                            null,
                            null,
                            null
                    ));
        } else if (rules.getMaxHumidityPercent() <= humidityPercent) {
            this._sendNotification(
                    AppNotificationFactory.createNotification(
                            1,  // HighHumidityAppNotification
                            this._context,
                            deviceId,
                            false,
                            null,
                            null,
                            null,
                            null,
                            null
                    ));
        }
    }

    private void _checkTemperature(int deviceId, int temperature) {
        CultivationRules rules = _cultivationRulesRepository.getCultivationRules(deviceId);

        AppLogger.info("temp max: %d, temp curr: %d", rules.getMaxTemperature(), temperature);

        if (rules.getMinTemperature() >= temperature) {
            this._sendNotification(
                    AppNotificationFactory.createNotification(
                            4,  // LowTemperatureAppNotification
                            this._context,
                            deviceId,
                            false,
                            null,
                            null,
                            null,
                            null,
                            null
                    ));
        } else if (rules.getMaxTemperature() <= temperature) {
            this._sendNotification(
                    AppNotificationFactory.createNotification(
                            3,  // HighTemperatureAppNotification
                            this._context,
                            deviceId,
                            false,
                            null,
                            null,
                            null,
                            null,
                            null
                    ));
        }
    }

    private void _checkSoilMoisture(int deviceId, int soilMoisturePercent) {
        // TODO
    }

    private void _checkIllumination(int deviceId, int illuminationPercent) {
        // TODO
    }

    private void _checkWaterLevel(int deviceId, int waterLevelPercent) {  // TODO: Add rule
        if (waterLevelPercent <= 10) {
            this._sendNotification(
                    AppNotificationFactory.createNotification(
                            9,  // NoWaterAppNotification
                            this._context,
                            deviceId,
                            false,
                            null,
                            null,
                            null,
                            null,
                            null
                    ));
        }
    }

    public void onNewSensorsData(@NonNull SensorsData sensorsData) {
        AppLogger.info("Processing sensors data for device id=%d", sensorsData.getDeviceId());

        _cultivationRulesRepository.getOrCreateCultivationRules(sensorsData.getDeviceId()).onSuccess(result -> {
            this._checkHumidity(sensorsData.getDeviceId(), (int) Math.round(sensorsData.getHumidity()));
            this._checkTemperature(sensorsData.getDeviceId(), (int) Math.round(sensorsData.getTemperature()));
            this._checkSoilMoisture(sensorsData.getDeviceId(), (int) Math.round((sensorsData.getSoilMoisture() / 4096d) * 100d));
            this._checkIllumination(sensorsData.getDeviceId(), (int) Math.round((sensorsData.getIllumination() / 4096d) * 100d));
            this._checkWaterLevel(sensorsData.getDeviceId(), (int) Math.round((sensorsData.getWaterLevel() / 4096d) * 100d));
        }).execute();
    }
}
