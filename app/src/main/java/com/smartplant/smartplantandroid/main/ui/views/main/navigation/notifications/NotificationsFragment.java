package com.smartplant.smartplantandroid.main.ui.views.main.navigation.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.core.ui.CustomFragment;
import com.smartplant.smartplantandroid.main.components.notifiactions.models.AppNotification;
import com.smartplant.smartplantandroid.main.ui.views.main.navigation.notifications.items.NotificationCardItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends CustomFragment {
    private NotificationsViewModel _notificationsViewModel;
    private NotificationCardItemAdapter _notificationsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_fragment_notifications, container, false);
        _notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);

        // TODO: Handle 0 notifications
        RecyclerView notificationsContainer = root.findViewById(R.id.notifications_container);
        notificationsContainer.setLayoutManager(new LinearLayoutManager(getContext()));
        ItemTouchHelper itemTouchHelper = this.setupItemTouchHandler();
        itemTouchHelper.attachToRecyclerView(notificationsContainer);

        this._notificationsAdapter = new NotificationCardItemAdapter(getContext());
        notificationsContainer.setAdapter(_notificationsAdapter);

        this._observeNotifications();
        this._getNotifications();
        return root;
    }

    @NonNull
    private ItemTouchHelper setupItemTouchHandler() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // We are not moving items, just swiping them away
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                AppNotification notification = ((NotificationCardItemAdapter.NotificationCardViewHolder) viewHolder).getNotification();
                assert notification != null;

                _notificationsAdapter.removeNotification(notification);  // Remove from UI
                notification.setChecked(true);
                _notificationsViewModel.updateNotification(notification)  // Remove from DB
                        .onSuccess(result -> AppLogger.info("Successfully set notification checked=true for id=%d", notification.getId()))
                        .onFailure(error -> AppLogger.error(error, "Failed to set notification checked=true for id=%d", notification.getId()))
                        .execute();
            }
        };

        return new ItemTouchHelper(simpleCallback);
    }

    private List<AppNotification> _getUncheckedNotificationsList() {
        return new ArrayList<>(this._notificationsViewModel.getUncheckedNotifications());
    }

    private void _getNotifications() {
        if (this._notificationsViewModel.isLoaded()) {
            this._notificationsAdapter.setNotifications(this._getUncheckedNotificationsList());
        } else {
            this._notificationsViewModel.fetchNotifications().execute();  // Will be updated automatically by LiveData observer
        }
    }

    private void _observeNotifications() {
        this._notificationsViewModel.getNotificationsLiveData()
                .observe(getViewLifecycleOwner(), notifications -> this._notificationsAdapter.setNotifications(new ArrayList<>(notifications)));
    }
}
