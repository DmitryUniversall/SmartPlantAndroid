package com.smartplant.smartplantandroid.main.ui.views.main.navigation.notifications.items.notifications.illumination;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.main.ui.views.main.navigation.notifications.items.notifications.BaseAppNotificationItem;

public class TooDarkAppNotificationItem extends BaseAppNotificationItem {
    public TooDarkAppNotificationItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TooDarkAppNotificationItem(Context context) {
        super(context);
    }

    @Override
    @DrawableRes
    protected int getIconId() {
        return R.drawable.icon_sun;
    }

    @Nullable
    @Override
    protected View createActionView() {
        return null;
    }

    @NonNull
    @Override
    protected String getDefaultTitle() {
        return this.context.getString(R.string.darkness_default_title);
    }

    @NonNull
    @Override
    protected String getDefaultDescription() {
        return this.context.getString(R.string.darkness_default_description);
    }
}
