package com.smartplant.smartplantandroid.main.ui.views.main.navigation.notifications.items.notifications;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.main.components.notifiactions.models.AppNotificationData;

public abstract class BaseAppNotificationItem extends LinearLayout {
    // UI
    protected TextView titleView;
    protected TextView descriptionView;
    protected ImageView iconView;
    protected LinearLayout actionContainer;

    // Data
    protected AppNotificationData notificationData;

    // Other
    protected final @NonNull Context context;

    protected abstract @DrawableRes int getIconId();

    protected abstract @Nullable View createActionView();

    protected abstract @NonNull String getDefaultTitle();

    protected abstract @NonNull String getDefaultDescription();

    public BaseAppNotificationItem(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        init();
    }

    public BaseAppNotificationItem(@NonNull Context context) {
        super(context);
        this.context = context;

        init();
    }

    public void bind(AppNotificationData notification) {
        this.notificationData = notification;
        this.updateUI();
    }

    protected void init() {
        View root = this.createRootView(LayoutInflater.from(this.context));

        this.titleView = root.findViewById(R.id.notification_title);
        this.descriptionView = root.findViewById(R.id.notification_description);
        this.iconView = root.findViewById(R.id.notification_icon);
        this.actionContainer = root.findViewById(R.id.notification_actions_container);

        View actionView = this.createActionView();
        if (actionView != null) actionContainer.addView(actionView);
    }

    protected View createRootView(@NonNull LayoutInflater inflater) {
        View root = inflater.inflate(R.layout.main_item_notification_card, this, true);
        root.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        return root;
    }

    protected @NonNull String getTitle() {
        assert this.notificationData != null;
        String title = this.notificationData.getTitle();
        return title != null ? title : getDefaultTitle();
    }

    protected @NonNull String getDescription() {
        assert this.notificationData != null;
        String description = this.notificationData.getDescription();
        return description != null ? description : getDefaultDescription();
    }

    private void updateUI() {
        this.setTitle(getTitle());
        this.setDescription(getDescription());
        this.setIcon(this.getIconId());
    }

    public void setTitle(String title) {
        titleView.setText(title);
    }

    public void setDescription(String description) {
        descriptionView.setText(description);
    }

    public void setIcon(int resId) {
        iconView.setImageResource(resId);
    }
}
