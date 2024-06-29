package com.smartplant.smartplantandroid.main.ui.views.main.navigation.notifications.items;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartplant.smartplantandroid.main.components.notifiactions.models.AbstractAppNotification;

import java.util.Collection;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private final List<AbstractAppNotification> _notifications;
    private final Context _context;

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        private final AppNotificationItem notificationView;
        private AbstractAppNotification _notificationData;

        public NotificationViewHolder(@NonNull AppNotificationItem itemView) {
            super(itemView);
            this.notificationView = itemView;
        }

        public void bind(AbstractAppNotification notification) {
            this._notificationData = notification;
            notificationView.bind(notification);
        }

        public AbstractAppNotification getNotificationData() {
            return _notificationData;
        }
    }

    public NotificationAdapter(Context context, List<AbstractAppNotification> notifications) {
        this._context = context;
        this._notifications = notifications;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setNotifications(@NonNull Collection<AbstractAppNotification> notifications) {
        this._notifications.clear();
        this._notifications.addAll(notifications);
        notifyDataSetChanged();
    }

    public void addNotification(AbstractAppNotification notification) {
        this._notifications.add(notification);
        notifyItemInserted(_notifications.size() - 1);
    }

    public void removeNotification(AbstractAppNotification notification) {
        int index = this._notifications.indexOf(notification);
        if (index == -1) return;

        this._notifications.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemViewType(int position) {
        AbstractAppNotification notification = _notifications.get(position);
        return notification.getNotificationType();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationViewHolder(new AppNotificationItem(_context));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        AbstractAppNotification notification = _notifications.get(position);
        holder.bind(notification);
    }

    @Override
    public int getItemCount() {
        return _notifications.size();
    }
}
