package com.smartplant.smartplantandroid.main.ui.views.main.navigation.settings.items.network;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.smartplant.smartplantandroid.R;

public class TransferServerPortSettingItem extends NetworkSettingItem {
    public TransferServerPortSettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TransferServerPortSettingItem(Context context) {
        super(context);
    }

    @Override
    protected @DrawableRes int getSettingIcon() {
        return R.drawable.icon_arrows_right_left;
    }

    @Override
    protected String getSettingTitle() {
        return getContext().getString(R.string.transfer_server_port_setting_title);
    }

    @Override
    protected int getInputType() {
        return InputType.TYPE_CLASS_NUMBER;
    }

    @Override
    protected @Nullable String validateInput(String input) {
        return null;  // TODO
    }

    @Override
    protected String getValue() {
        return String.valueOf(getNetworkSettings().getTSPort());
    }

    @Override
    protected void setValue(String newValue) {
        getNetworkSettings().setTSPort(Integer.parseInt(newValue));
    }
}
