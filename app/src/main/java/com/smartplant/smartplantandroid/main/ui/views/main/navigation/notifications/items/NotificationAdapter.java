package com.smartplant.smartplantandroid.main.ui.views.main.navigation.notifications.items;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartplant.smartplantandroid.main.components.notifiactions.models.AppNotificationData;
import com.smartplant.smartplantandroid.main.ui.views.main.navigation.notifications.items.notifications.AppNotificationFactory;
import com.smartplant.smartplantandroid.main.ui.views.main.navigation.notifications.items.notifications.BaseAppNotificationItem;

import java.util.Collection;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private final List<AppNotificationData> _notifications;
    private final Context _context;

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        private final BaseAppNotificationItem notificationView;
        private AppNotificationData _notificationData;

        public NotificationViewHolder(@NonNull BaseAppNotificationItem itemView) {
            super(itemView);
            this.notificationView = itemView;
        }

        public void bind(AppNotificationData notification) {
            this._notificationData = notification;
            notificationView.bind(notification);
        }

        public AppNotificationData getNotificationData() {
            return _notificationData;
        }
    }

    public NotificationAdapter(Context context, List<AppNotificationData> notifications) {
        this._context = context;
        this._notifications = notifications;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setNotifications(@NonNull Collection<AppNotificationData> notifications) {
        this._notifications.clear();
        this._notifications.addAll(notifications);
        notifyDataSetChanged();
    }

    public void addNotification(AppNotificationData notification) {
        this._notifications.add(notification);
        notifyItemInserted(_notifications.size() - 1);
    }

    public void removeNotification(AppNotificationData notification) {
        int index = this._notifications.indexOf(notification);
        if (index == -1) return;

        this._notifications.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemViewType(int position) {
        AppNotificationData notification = _notifications.get(position);
        return notification.getNotificationType();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseAppNotificationItem view = AppNotificationFactory.createNotification(viewType, _context);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        AppNotificationData notification = _notifications.get(position);
        holder.bind(notification);
    }

    @Override
    public int getItemCount() {
        return _notifications.size();
    }
}
