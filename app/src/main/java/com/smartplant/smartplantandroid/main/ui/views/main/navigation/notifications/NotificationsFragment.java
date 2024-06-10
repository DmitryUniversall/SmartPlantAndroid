package com.smartplant.smartplantandroid.main.ui.views.main.navigation.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.core.ui.CustomFragment;
import com.smartplant.smartplantandroid.main.ui.views.main.navigation.notifications.items.NotificationCardItemAdapter;

public class NotificationsFragment extends CustomFragment {
    private NotificationsViewModel _notificationsViewModel;
    private NotificationCardItemAdapter _notificationsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_fragment_notifications, container, false);
        _notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);

        RecyclerView notificationsContainer = root.findViewById(R.id.notifications_container);
        notificationsContainer.setLayoutManager(new LinearLayoutManager(getContext()));

        this._notificationsAdapter = new NotificationCardItemAdapter(getContext());
        notificationsContainer.setAdapter(_notificationsAdapter);
        return root;
    }
}
