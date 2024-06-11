package com.smartplant.smartplantandroid.main.components.notifiactions.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.smartplant.smartplantandroid.core.async.background_task.BackgroundTask;
import com.smartplant.smartplantandroid.core.data.observers.ObservableData;
import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.main.components.notifiactions.internal_utils.db.NotificationsDBService;
import com.smartplant.smartplantandroid.main.components.notifiactions.internal_utils.notifications.NotificationUtils;
import com.smartplant.smartplantandroid.main.components.notifiactions.models.AppNotification;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NotificationsRepositoryST {
    // Singleton
    private static @Nullable NotificationsRepositoryST _instance;

    // Services
    private final @NonNull NotificationsDBService _dbService;

    // Cache
    private final @NonNull Set<AppNotification> _notifications = new HashSet<>();
    private boolean _isLoaded = false;

    // Observers
    private final @NonNull ObservableData<Set<AppNotification>> _observableNotifications = new ObservableData<>();

    public static synchronized void createInstance() {
        if (_instance != null)
            throw new RuntimeException("NotificationsRepositoryST has already been initialized");
        _instance = new NotificationsRepositoryST();
    }

    public static synchronized NotificationsRepositoryST getInstance() {
        if (_instance == null)
            throw new RuntimeException("NotificationsRepositoryST has not been initialized");
        return _instance;
    }

    private NotificationsRepositoryST() {
        this._dbService = new NotificationsDBService();
    }

    private void _addNotifications(@NonNull Collection<AppNotification> notifications) {
        this._notifications.addAll(notifications);
        this._observableNotifications.notifyObservers(this._notifications);
    }

    private void _addNotification(@NonNull AppNotification notification) {
        this._notifications.add(notification);
        this._observableNotifications.notifyObservers(this._notifications);
    }

    private BackgroundTask<Void> _insertNotification(@NonNull AppNotification notification) {
        return this._dbService.insertNotification(notification);
    }

    public boolean isLoaded() {
        return this._isLoaded;
    }

    public BackgroundTask<List<AppNotification>> fetchAllNotifications() {
        this._isLoaded = true;
        return _dbService.getAllNotifications().onSuccess(this::_addNotifications);
    }

    public BackgroundTask<List<AppNotification>> fetchUncheckedNotifications() {
        return _dbService.getUncheckedNotifications().onSuccess(this::_addNotifications);
    }

    public Set<AppNotification> getNotifications() {
        return this._notifications;
    }

    public ObservableData<Set<AppNotification>> getObservableNotifications() {
        return this._observableNotifications;
    }

    public Set<AppNotification> getUncheckedNotifications() {
        Set<AppNotification> result = new HashSet<>();

        for (AppNotification notification : this._notifications) {
            if (!notification.isChecked()) result.add(notification);
        }

        return result;
    }

    public BackgroundTask<Void> sendAppNotification(@NonNull AppNotification notification) {
        return this._insertNotification(notification)
                .onSuccess(result -> {
                    NotificationUtils.sendNotification(notification);
                    this._addNotification(notification);
                    AppLogger.info("Successfully send notification");
                })
                .onFailure(error -> AppLogger.error("Failed to send(insert) notification", error));
    }
}
