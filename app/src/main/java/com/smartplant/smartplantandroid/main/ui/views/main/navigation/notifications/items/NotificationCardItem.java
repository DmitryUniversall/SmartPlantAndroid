package com.smartplant.smartplantandroid.main.ui.views.main.navigation.notifications.items;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.main.components.notifiactions.models.AppNotification;

public class NotificationCardItem extends LinearLayout {
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
    }

    public void bind(AppNotification notification) {
        this._notification = notification;

        if (!this._notification.getActions().isEmpty()) {

        }
    }
}