package com.smartplant.smartplantandroid.main.ui.views.main.navigation.notifications;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.core.ui.CustomFragment;
import com.smartplant.smartplantandroid.main.components.notifiactions.models.AbstractAppNotification;
import com.smartplant.smartplantandroid.main.ui.views.main.navigation.notifications.items.NotificationAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NotificationsFragment extends CustomFragment {
    private NotificationsViewModel _notificationsViewModel;
    private NotificationAdapter _notificationsAdapter;
    private RecyclerView _notificationsContainer;
    private TextView _noNotificationsTextView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_fragment_notifications, container, false);
        _notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);

        this._noNotificationsTextView = root.findViewById(R.id.has_no_notifications_text);

        this._notificationsContainer = root.findViewById(R.id.notifications_container);
        this._notificationsContainer.setLayoutManager(new LinearLayoutManager(getContext()));
        ItemTouchHelper itemTouchHelper = this.setupItemTouchHandler();
        itemTouchHelper.attachToRecyclerView(this._notificationsContainer);

        this._notificationsAdapter = new NotificationAdapter(getContext(), new ArrayList<>());
        this._notificationsContainer.setAdapter(_notificationsAdapter);

        this._observeNotifications();
        this._getNotifications();
        this._noNotificationsCheck();
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
                AbstractAppNotification notification = ((NotificationAdapter.NotificationViewHolder) viewHolder).getNotificationData();
                assert notification != null;

                _notificationsAdapter.removeNotification(notification);  // Remove from UI
                notification.setChecked(true);
                _notificationsViewModel.updateNotification(notification)  // Remove from DB
                        .onSuccess(result -> AppLogger.info("Successfully set notification checked=true for id=%d", notification.getId()))
                        .onFailure(error -> AppLogger.error(error, "Failed to set notification checked=true for id=%d", notification.getId()))
                        .execute();
                _noNotificationsCheck();
            }
        };

        return new ItemTouchHelper(simpleCallback);
    }

    private List<AbstractAppNotification> _getUncheckedNotificationsList() {
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
        Activity activity = getActivity();
        assert activity != null;

        this._notificationsViewModel.getObservableNotifications().addObserver(obj -> {
            Set<AbstractAppNotification> notifications = this._notificationsViewModel.getUncheckedNotifications();
            AppLogger.info("Updating notifications; Found unchecked notifications: %d", notifications.size());
            activity.runOnUiThread(() -> {
                this._notificationsAdapter.setNotifications(new ArrayList<>(notifications));
                this._noNotificationsCheck();
            });
        });
    }

    private void _noNotificationsCheck() {
        if (this._notificationsAdapter.getItemCount() == 0) {
            this._notificationsContainer.setVisibility(View.GONE);
            this._noNotificationsTextView.setVisibility(View.VISIBLE);
        } else {
            this._notificationsContainer.setVisibility(View.VISIBLE);
            this._noNotificationsTextView.setVisibility(View.GONE);
        }
    }
}
