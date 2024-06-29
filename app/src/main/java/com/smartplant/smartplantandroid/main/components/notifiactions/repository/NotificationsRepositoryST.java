package com.smartplant.smartplantandroid.main.components.notifiactions.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.smartplant.smartplantandroid.core.async.background_task.BackgroundTask;
import com.smartplant.smartplantandroid.core.data.observers.ObservableData;
import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.main.components.notifiactions.internal_utils.android.AndroidNotificationUtils;
import com.smartplant.smartplantandroid.main.components.notifiactions.internal_utils.db.NotificationsDBService;
import com.smartplant.smartplantandroid.main.components.notifiactions.models.AppNotificationData;

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
    private final @NonNull Set<AppNotificationData> _notifications = new HashSet<>();
    private boolean _isLoaded = false;

    // Observers
    private final @NonNull ObservableData<Set<AppNotificationData>> _observableNotifications = new ObservableData<>();

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

    private void _addNotifications(@NonNull Collection<AppNotificationData> notifications) {
        this._notifications.addAll(notifications);
        this._observableNotifications.notifyObservers(this._notifications);
    }

    private void _addNotification(@NonNull AppNotificationData notification) {
        this._notifications.add(notification);
        this._observableNotifications.notifyObservers(this._notifications);
    }

    private BackgroundTask<Void> _insertNotification(@NonNull AppNotificationData notification) {
        return this._dbService.insertNotification(notification);
    }

    private BackgroundTask<Void> _updateNotificationInDB(@NonNull AppNotificationData notification) {
        return this._dbService.updateNotification(notification);
    }

    public boolean isLoaded() {
        return this._isLoaded;
    }

    public BackgroundTask<List<AppNotificationData>> fetchAllNotifications() {
        this._isLoaded = true;
        return _dbService.getAllNotifications().onSuccess(this::_addNotifications);
    }

    public BackgroundTask<List<AppNotificationData>> fetchUncheckedNotifications() {
        return _dbService.getUncheckedNotifications().onSuccess(this::_addNotifications);
    }

    public Set<AppNotificationData> getNotifications() {
        return this._notifications;
    }

    public ObservableData<Set<AppNotificationData>> getObservableNotifications() {
        return this._observableNotifications;
    }

    public Set<AppNotificationData> getUncheckedNotifications() {
        Set<AppNotificationData> result = new HashSet<>();

        for (AppNotificationData notification : this._notifications) {
            if (!notification.isChecked()) result.add(notification);
        }

        return result;
    }

    public BackgroundTask<Void> sendAppNotification(@NonNull AppNotificationData notification) {
        return this._insertNotification(notification)
                .onSuccess(result -> {
                    AndroidNotificationUtils.sendAndroidNotification(notification);
                    this._addNotification(notification);
                    AppLogger.info("Successfully send notification");
                })
                .onFailure(error -> AppLogger.error("Failed to send notification", error));
    }

    public BackgroundTask<Void> updateNotification(@NonNull AppNotificationData notification) {
        return this._updateNotificationInDB(notification)
                .onSuccess(result -> {
                    AppLogger.info("Notification id=%d successfully updated", notification.getId());
                    if (_notifications.contains(notification))
                        this._observableNotifications.notifyObservers(this._notifications);  // TODO: Add if not contains?
                })
                .onFailure(error -> AppLogger.error(error, "Failed to update notification id=%d", notification.getId()));
    }
}
