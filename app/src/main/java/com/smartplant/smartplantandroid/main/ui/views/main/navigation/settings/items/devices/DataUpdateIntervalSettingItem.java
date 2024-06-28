package com.smartplant.smartplantandroid.main.ui.views.main.navigation.settings.items.devices;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.main.state.settings.DevicesSettingsST;
import com.smartplant.smartplantandroid.main.ui.views.main.navigation.settings.items.BaseSettingsItem;

public class DataUpdateIntervalSettingItem extends BaseSettingsItem {
    private @Nullable DevicesSettingsST _devicesSettings;

    public DataUpdateIntervalSettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DataUpdateIntervalSettingItem(Context context) {
        super(context);
    }

    private DevicesSettingsST getDevicesSettings() {
        if (_devicesSettings != null) return _devicesSettings;
        _devicesSettings = DevicesSettingsST.getInstance();
        return _devicesSettings;
    }

    @Override
    protected @DrawableRes int getSettingIcon() {
        return R.drawable.icon_clock;
    }

    @Override
    protected String getSettingTitle() {
        return getContext().getString(R.string.data_update_interval_setting);
    }

    @Override
    protected int getInputType() {
        return InputType.TYPE_CLASS_NUMBER;
    }

    @Override
    protected String validateInput(String input) {
        return "";  // TODO
    }

    @Override
    protected String getValue() {
        return String.valueOf(getDevicesSettings().getDataUpdateInterval());
    }

    @Override
    protected void setValue(String newValue) {
        getDevicesSettings().setDataUpdateInterval(Integer.parseInt(newValue));
    }
}
