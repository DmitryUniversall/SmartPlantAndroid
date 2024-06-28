package com.smartplant.smartplantandroid.main.ui.views.main.navigation.settings.items.network;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;

import androidx.annotation.DrawableRes;

import com.smartplant.smartplantandroid.R;

public class ApiBaseUrlSettingItem extends NetworkSettingItem {
    public ApiBaseUrlSettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ApiBaseUrlSettingItem(Context context) {
        super(context);
    }

    @Override
    protected @DrawableRes int getSettingIcon() {
        return R.drawable.icon_globe;
    }

    @Override
    protected String getSettingTitle() {
        return getContext().getString(R.string.transfer_server_base_url);
    }

    @Override
    protected int getInputType() {
        return InputType.TYPE_CLASS_TEXT;
    }

    @Override
    protected String validateInput(String input) {
        return "";  // TODO
    }

    @Override
    protected String getValue() {
        return getNetworkSettings().getApiBaseUrl();
    }

    @Override
    protected void setValue(String newValue) {
        getNetworkSettings().setApiBaseUrl(newValue);
    }
}
