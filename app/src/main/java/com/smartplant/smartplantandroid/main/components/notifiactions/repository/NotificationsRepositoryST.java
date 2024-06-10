package com.smartplant.smartplantandroid.main.components.notifiactions.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.smartplant.smartplantandroid.core.async.background_task.BackgroundTask;
import com.smartplant.smartplantandroid.main.components.notifiactions.internal_utils.db.NotificationsDBService;
import com.smartplant.smartplantandroid.main.components.notifiactions.models.AppNotification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NotificationsRepositoryST {
    // Singleton
    private static @Nullable NotificationsRepositoryST _instance;

    // Services
    private final @NonNull NotificationsDBService _dbService;

    // Cache
    private final @NonNull Map<Integer, Set<AppNotification>> _notifications = new HashMap<>();
    private boolean _isLoaded = false;

    public static synchronized void createInstance(Context context) {
        if (_instance != null)
            throw new RuntimeException("StorageRepositoryST has already been initialized");
        _instance = new NotificationsRepositoryST(context);
    }

    public static synchronized NotificationsRepositoryST getInstance() {
        if (_instance == null)
            throw new RuntimeException("StorageRepositoryST has not been initialized");
        return _instance;
    }

    private NotificationsRepositoryST(Context context) {
        this._dbService = new NotificationsDBService(context);
    }

    private void _putNotifications(Collection<AppNotification> notifications) {  // TODO: Optimise it
        for (AppNotification notification : notifications) {
            Set<AppNotification> deviceNotifications = this._notifications.get(notification.getDeviceId());
            if (deviceNotifications == null) deviceNotifications = new HashSet<>();
            deviceNotifications.add(notification);
        }
    }

    private void _putDeviceNotifications(Collection<AppNotification> notifications, int deviceId) {
        Set<AppNotification> deviceNotifications = this._notifications.get(deviceId);
        if (deviceNotifications == null) deviceNotifications = new HashSet<>();
        deviceNotifications.addAll(notifications);
    }

    public boolean isLoaded() {
        return this._isLoaded;
    }

    public BackgroundTask<List<AppNotification>> fetchAllNotifications() {
        this._isLoaded = true;
        return _dbService.getAllNotifications().onSuccess(this::_putNotifications);
    }

    public BackgroundTask<List<AppNotification>> fetchUncheckedNotifications() {
        return _dbService.getUncheckedNotifications().onSuccess(this::_putNotifications);
    }

    public BackgroundTask<List<AppNotification>> fetchNotificationsForDevice(int deviceId) {
        return _dbService.getAllNotificationsForDevice(deviceId).onSuccess(result -> this._putDeviceNotifications(result, deviceId));
    }

    public BackgroundTask<List<AppNotification>> fetchUncheckedNotificationsForDevice(int deviceId) {
        return _dbService.getUncheckedNotificationsForDevice(deviceId).onSuccess(result -> this._putDeviceNotifications(result, deviceId));
    }

    public Map<Integer, Set<AppNotification>> getNotifications() {
        return this._notifications;
    }

    public @Nullable Set<AppNotification> getNotificationsForDevice(int deviceId) {
        return this._notifications.get(deviceId);
    }
}
