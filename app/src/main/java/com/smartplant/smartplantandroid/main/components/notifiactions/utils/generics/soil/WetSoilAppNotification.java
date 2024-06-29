package com.smartplant.smartplantandroid.main.components.notifiactions.utils.generics.soil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.main.components.notifiactions.models.AbstractAppNotification;

import java.util.Date;

public class WetSoilAppNotification extends AbstractAppNotification {
    protected WetSoilAppNotification(
            @NonNull Context context,
            int deviceId,
            boolean isChecked,
            @Nullable Long id,
            @Nullable String title,
            @Nullable String description,
            @Nullable @DrawableRes Integer iconId,
            @Nullable Date _createdAt
    ) {
        super(context, deviceId, isChecked, id, title, description, iconId, _createdAt);
    }

    @Override
    public int getNotificationType() {
        return 7;
    }

    @Override
    @DrawableRes
    protected int getDefaultIconId() {
        return R.drawable.icon_water;
    }

    @NonNull
    @Override
    protected String getDefaultTitle() {
        return this.context.getString(R.string.wet_soil_default_title);
    }

    @NonNull
    @Override
    protected String getDefaultDescription() {
        return this.context.getString(R.string.wet_soil_default_description);
    }

    @Nullable
    @Override
    public View createActionView(@NonNull LayoutInflater inflater, @NonNull View root) {
        return null;
    }
}
