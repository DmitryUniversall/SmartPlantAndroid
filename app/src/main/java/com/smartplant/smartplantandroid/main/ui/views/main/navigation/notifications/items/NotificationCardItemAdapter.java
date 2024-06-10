package com.smartplant.smartplantandroid.main.ui.views.main.navigation.notifications.items;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartplant.smartplantandroid.main.components.notifiactions.models.AppNotification;

import java.util.ArrayList;
import java.util.List;

public class NotificationCardItemAdapter extends RecyclerView.Adapter<NotificationCardItemAdapter.NotificationCardViewHolder> {
    private final List<AppNotification> _notifications = new ArrayList<>();
    private final Context _context;

    public static class NotificationCardViewHolder extends RecyclerView.ViewHolder {
        private final NotificationCardItem _notificationCardItem;

        public NotificationCardViewHolder(@NonNull NotificationCardItem itemView) {
            super(itemView);
            this._notificationCardItem = itemView;
        }
    }

    public NotificationCardItemAdapter(Context context) {
        this._context = context;
    }

    public void addNotification(AppNotification notification) {
        this._notifications.add(notification);
        notifyItemInserted(_notifications.size() - 1);
    }

    public void removeNotification(AppNotification notification) {
        int index = this._notifications.indexOf(notification);
        if (index == -1) return;

        this._notifications.remove(index);
        notifyItemRemoved(index);
    }

    @NonNull
    @Override
    public NotificationCardItemAdapter.NotificationCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NotificationCardItem itemView = new NotificationCardItem(this._context);
        return new NotificationCardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotificationCardViewHolder holder, int position) {
        AppNotification notification = this._notifications.get(position);
        holder._notificationCardItem.bind(notification);
    }

    @Override
    public int getItemCount() {
        return this._notifications.size();
    }
}
