package com.smartplant.smartplantandroid.main.ui.views.main.navigation.notifications;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.smartplant.smartplantandroid.core.async.background_task.BackgroundTask;
import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.main.components.notifiactions.models.AppNotification;
import com.smartplant.smartplantandroid.main.components.notifiactions.repository.NotificationsRepositoryST;

import java.util.List;
import java.util.Set;

public class NotificationsViewModel extends ViewModel {
    private final NotificationsRepositoryST _notificationsRepository;
    private final MutableLiveData<Set<AppNotification>> _notificationsLiveData = new MutableLiveData<>();

    public NotificationsViewModel() {
        this._notificationsRepository = NotificationsRepositoryST.getInstance();
        this._observerNotifications();
    }

    private void _observerNotifications() {
        this._notificationsRepository.getObservableNotifications().addObserver(obj -> {
            AppLogger.info("UPDATING IN LIVE DATA");
            AppLogger.info("SIZE %d", this._notificationsRepository.getUncheckedNotifications().size());
            this._notificationsLiveData.postValue(this._notificationsRepository.getUncheckedNotifications());
        });
    }

    public boolean isLoaded() {
        return this._notificationsRepository.isLoaded();
    }

    public MutableLiveData<Set<AppNotification>> getNotificationsLiveData() {
        return this._notificationsLiveData;
    }

    public BackgroundTask<List<AppNotification>> fetchNotifications() {
        return this._notificationsRepository.fetchAllNotifications();
    }

    public Set<AppNotification> getUncheckedNotifications() {
        return this._notificationsRepository.getUncheckedNotifications();
    }
}
