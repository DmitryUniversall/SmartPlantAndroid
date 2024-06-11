package com.smartplant.smartplantandroid.main.ui.views.main.navigation.notifications.items;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.main.components.notifiactions.models.AppNotification;

public class NotificationCardItem extends LinearLayout {
    // UI
    private TextView _titleTextView;
    private TextView _descriptionTextView;
    private ImageView _iconImageView;
    private LinearLayout _actionContainer;

    // Data
    private AppNotification _notification;

    public NotificationCardItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NotificationCardItem(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.main_item_notification_card, this, true);
        root.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        this._titleTextView = root.findViewById(R.id.notification_title);
        this._descriptionTextView = root.findViewById(R.id.notification_description);
        this._iconImageView = root.findViewById(R.id.notification_icon);
        this._actionContainer = root.findViewById(R.id.notification_actions_container);
    }

    private void setNotificationData() {
        this.setTitle(this._notification.getTitle());
        this.setDescription(this._notification.getDescription());
        this.setIcon(this._notification.getIconResId());
        this.setNotificationActions();
    }

    private void setNotificationActions() {
        // TODO
    }

    public void bind(AppNotification notification) {
        this._notification = notification;
        this.setNotificationData();
    }

    public void setTitle(String title) {
        _titleTextView.setText(title);
    }

    public void setDescription(String description) {
        _descriptionTextView.setText(description);
    }

    public void setIcon(int resId) {
        _iconImageView.setImageResource(resId);
    }
}