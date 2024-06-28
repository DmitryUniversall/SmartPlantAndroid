package com.smartplant.smartplantandroid.main.ui.views.main.navigation.settings.items.network;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.smartplant.smartplantandroid.main.state.settings.NetworkSettingsST;
import com.smartplant.smartplantandroid.main.ui.views.main.navigation.settings.items.BaseSettingsItem;

public abstract class NetworkSettingItem extends BaseSettingsItem {
    private @Nullable NetworkSettingsST networkSettings;

    public NetworkSettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NetworkSettingItem(Context context) {
        super(context);
    }

    protected NetworkSettingsST getNetworkSettings() {
        if (networkSettings != null) return networkSettings;
        networkSettings = NetworkSettingsST.getInstance();
        return networkSettings;
    }
}
