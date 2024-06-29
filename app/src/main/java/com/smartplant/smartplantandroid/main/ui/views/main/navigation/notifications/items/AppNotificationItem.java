package com.smartplant.smartplantandroid.main.ui.views.main.navigation.notifications.items;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.main.components.devices.utils.DevicesLocalDataManagerST;
import com.smartplant.smartplantandroid.main.components.notifiactions.models.AbstractAppNotification;

import java.util.Locale;

public class AppNotificationItem extends LinearLayout {
    // UI
    private TextView _titleView;
    private TextView _descriptionView;
    private ImageView _iconView;
    private LinearLayout _actionContainer;
    
    private View _root;

    // Data
    private AbstractAppNotification _notification;

    // Utils
    private final DevicesLocalDataManagerST _devicesLocalDataManager;

    // Other
    private final @NonNull Context context;

    public AppNotificationItem(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this._devicesLocalDataManager = DevicesLocalDataManagerST.getInstance();

        init();
    }

    public AppNotificationItem(@NonNull Context context) {
        super(context);
        this.context = context;
        this._devicesLocalDataManager = DevicesLocalDataManagerST.getInstance();

        init();
    }

    public void bind(AbstractAppNotification notification) {
        this._notification = notification;
        
        View actionView = this._notification.createActionView(LayoutInflater.from(this.context), this._root);
        if (actionView != null) _actionContainer.addView(actionView);
        
        this.updateUI();
    }

    private void init() {
        this._root = this.createRootView(LayoutInflater.from(this.context));

        this._titleView = this._root.findViewById(R.id.notification_title);
        this._descriptionView = this._root.findViewById(R.id.notification_description);
        this._iconView = this._root.findViewById(R.id.notification_icon);
        this._actionContainer = this._root.findViewById(R.id.notification_actions_container);
    }

    private View createRootView(@NonNull LayoutInflater inflater) {
        View root = inflater.inflate(R.layout.main_item_notification_card, this, true);
        root.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        return root;
    }

    private void updateUI() {
        assert this._notification != null;

        String deviceName = this._devicesLocalDataManager.getDeviceName(_notification.getDeviceId());

        if (deviceName == null) {
            this._root.setVisibility(View.GONE);
            throw new IllegalArgumentException(String.format(Locale.getDefault(), "Unknown device id=%d", this._notification.getDeviceId()));
        }

        this.setTitle(String.format(Locale.getDefault(), "%s: %s", deviceName, this._notification.getTitle()));
        this.setDescription(this._notification.getDescription());
        this.setIcon(this._notification.getIconId());
    }

    public void setTitle(String title) {
        _titleView.setText(title);
    }

    public void setDescription(String description) {
        _descriptionView.setText(description);
    }

    public void setIcon(int resId) {
        _iconView.setImageResource(resId);
    }
}
