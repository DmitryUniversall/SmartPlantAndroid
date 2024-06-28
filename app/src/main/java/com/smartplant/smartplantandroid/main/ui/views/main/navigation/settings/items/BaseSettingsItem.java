package com.smartplant.smartplantandroid.main.ui.views.main.navigation.settings.items;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.smartplant.smartplantandroid.R;

public abstract class BaseSettingsItem extends LinearLayout {
    protected TextView settingValueView;
    protected TextView settingTitleView;
    protected ImageView settingIconView;

    protected abstract @DrawableRes int getSettingIcon();

    protected abstract String getSettingTitle();

    protected abstract int getInputType();

    protected abstract String validateInput(String input);

    protected abstract String getValue();

    protected abstract void setValue(String newValue);

    public BaseSettingsItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseSettingsItem(Context context) {
        super(context);
        init(context);
    }

    protected void init(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.main_settings_setting_item, this, true);
        root.setBackgroundResource(R.drawable.ripple_effect);
        root.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        root.setOnClickListener(v -> _showEditDialog(getSettingTitle(), getValue(), getInputType()));

        settingIconView = root.findViewById(R.id.setting_icon);
        settingTitleView = root.findViewById(R.id.setting_title);
        settingValueView = root.findViewById(R.id.setting_value);
        setupSettingCard();
    }

    private void setupSettingCard() {
        settingIconView.setImageResource(getSettingIcon());
        settingTitleView.setText(getSettingTitle());
        settingValueView.setText(getValue());
    }

    private void _showEditDialog(@NonNull String title, @NonNull String currentValue, int inputType) {
        Context context = getContext();
        assert context != null;

        // Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);

        // Input
        final EditText input = new EditText(context);  // TODO: from xml
        input.setInputType(inputType);
        input.setText(currentValue);
        builder.setView(input);

        // OK button
        builder.setPositiveButton("OK", (dialog, which) -> {
            String newValue = input.getText().toString();

            String errorMessage = validateInput(newValue);
            if (errorMessage.isEmpty()) {
                Toast.makeText(context, context.getString(R.string.saved_successfully), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                return;
            }

            setValue(newValue);
            settingValueView.setText(newValue);
        });

        // Cancel button
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        // Show
        builder.show();
    }
}
