package com.smartplant.smartplantandroid.main.ui.views.main.navigation.notifications.items.notifications.humidity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.main.ui.views.main.navigation.notifications.items.notifications.BaseAppNotificationItem;

public class LowHumidityAppNotificationItem extends BaseAppNotificationItem {
    public LowHumidityAppNotificationItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LowHumidityAppNotificationItem(Context context) {
        super(context);
    }

    @Override
    @DrawableRes
    protected int getIconId() {
        return R.drawable.icon_wind;
    }

    @Nullable
    @Override
    protected View createActionView() {
        return null;
    }

    @NonNull
    @Override
    protected String getDefaultTitle() {
        return this.context.getString(R.string.low_humidity_default_title);
    }

    @NonNull
    @Override
    protected String getDefaultDescription() {
        return this.context.getString(R.string.low_humidity_default_description);
    }
}
