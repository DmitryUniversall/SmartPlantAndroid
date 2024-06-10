package com.smartplant.smartplantandroid.main.components.notifiactions.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.smartplant.smartplantandroid.main.components.notifiactions.models.AppNotification;

import java.util.ArrayList;
import java.util.List;

public class NotificationsRepositoryST {
    private static @Nullable NotificationsRepositoryST _instance;
    private final @NonNull List<AppNotification> _notifications = new ArrayList<>();
    private boolean _isLoaded = false;

    public static synchronized void createInstance() {
        if (_instance != null)
            throw new RuntimeException("StorageRepositoryST has already been initialized");
        _instance = new NotificationsRepositoryST();
    }

    public static synchronized NotificationsRepositoryST getInstance() {
        if (_instance == null)
            throw new RuntimeException("StorageRepositoryST has not been initialized");
        return _instance;
    }

    private NotificationsRepositoryST() {}

//    private List<AppNotification> _fetchNotifications() {
//        this._isLoaded = true;
//    }
//
//    public List<AppNotification> getNotifications() {
//    }
//
//    public void addNotification(AppNotification notification) {
//
//    }
}
