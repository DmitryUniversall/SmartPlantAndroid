package com.smartplant.smartplantandroid.main.ui.views.main.navigation.notifications;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.smartplant.smartplantandroid.core.async.background_task.BackgroundTask;
import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.main.components.notifiactions.models.AbstractAppNotification;
import com.smartplant.smartplantandroid.main.components.notifiactions.repository.NotificationsRepositoryST;

import java.util.List;
import java.util.Set;

public class NotificationsViewModel extends ViewModel {
    private final NotificationsRepositoryST _notificationsRepository;
    private final MutableLiveData<Set<AbstractAppNotification>> _notificationsLiveData = new MutableLiveData<>();

    public NotificationsViewModel() {
        this._notificationsRepository = NotificationsRepositoryST.getInstance();
        this._observerNotifications();
    }

    private void _observerNotifications() {
        this._notificationsRepository.getObservableNotifications().addObserver(obj -> {
            Set<AbstractAppNotification> notifications = this._notificationsRepository.getUncheckedNotifications();
            AppLogger.info("Updating notifications live data; Found unchecked notifications: %d", notifications.size());
            this._notificationsLiveData.postValue(notifications);
        });
    }

    public boolean isLoaded() {
        return this._notificationsRepository.isLoaded();
    }

    public MutableLiveData<Set<AbstractAppNotification>> getNotificationsLiveData() {
        return this._notificationsLiveData;
    }

    public BackgroundTask<List<AbstractAppNotification>> fetchNotifications() {
        return this._notificationsRepository.fetchAllNotifications();
    }

    public Set<AbstractAppNotification> getUncheckedNotifications() {
        return this._notificationsRepository.getUncheckedNotifications();
    }

    public BackgroundTask<Void> updateNotification(@NonNull AbstractAppNotification notification) {
        return this._notificationsRepository.updateNotification(notification);
    }
}
