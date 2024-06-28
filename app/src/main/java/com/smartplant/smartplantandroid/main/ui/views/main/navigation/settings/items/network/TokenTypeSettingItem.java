package com.smartplant.smartplantandroid.main.ui.views.main.navigation.settings.items.network;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;

import androidx.annotation.DrawableRes;

import com.smartplant.smartplantandroid.R;

public class TokenTypeSettingItem extends NetworkSettingItem {
    public TokenTypeSettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TokenTypeSettingItem(Context context) {
        super(context);
    }

    @Override
    protected @DrawableRes int getSettingIcon() {
        return R.drawable.icon_key;
    }

    @Override
    protected String getSettingTitle() {
        return getContext().getString(R.string.transfer_server_token_type);
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
        return getNetworkSettings().getAuthTokenType();
    }

    @Override
    protected void setValue(String newValue) {
        getNetworkSettings().setAuthTokenType(newValue);
    }
}
