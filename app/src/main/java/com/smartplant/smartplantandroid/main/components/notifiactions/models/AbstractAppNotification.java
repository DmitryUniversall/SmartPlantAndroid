package com.smartplant.smartplantandroid.main.components.notifiactions.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;

public abstract class AbstractAppNotification {
    protected final @NonNull Context context;
    protected final int _deviceId;
    protected boolean _isChecked;

    protected @Nullable Long _id;
    protected @Nullable String _title;
    protected @Nullable String _description;
    protected @Nullable Integer iconId;
    protected @NonNull Date _createdAt;

    public abstract int getNotificationType();

    public abstract @Nullable View createActionView(@NonNull LayoutInflater inflater, @NonNull View root);

    protected abstract @DrawableRes int getDefaultIconId();

    protected abstract @NonNull String getDefaultTitle();

    protected abstract @NonNull String getDefaultDescription();

    protected AbstractAppNotification(
            @NonNull Context context,
            int deviceId,
            boolean isChecked,
            @Nullable Long id,
            @Nullable String title,
            @Nullable String description,
            @Nullable @DrawableRes Integer iconId,
            @Nullable Date _createdAt
    ) {
        this.context = context;

        this._deviceId = deviceId;
        this._isChecked = isChecked;
        this._id = id;
        this._title = title;
        this._description = description;
        this.iconId = iconId;
        this._createdAt = _createdAt != null ? _createdAt : new Date();
    }

    @NonNull
    public Context getContext() {
        return context;
    }

    public int getDeviceId() {
        return _deviceId;
    }

    @Nullable
    public Long getId() {
        return _id;
    }

    public void setId(@Nullable Long id) {
        this._id = id;
    }

    public boolean isChecked() {
        return _isChecked;
    }

    public void setChecked(boolean checked) {
        this._isChecked = checked;
    }

    public String getTitle() {
        return this._title != null ? this._title : this.getDefaultTitle();
    }

    public void setTitle(@NonNull String title) {
        this._title = title;
    }

    public String getDescription() {
        return this._description != null ? this._description : this.getDefaultDescription();
    }

    public void setDescription(@NonNull String description) {
        this._description = description;
    }

    public int getIconId() {
        return this.iconId != null ? this.iconId : getDefaultIconId();
    }

    public void setIconId(@Nullable Integer iconId) {
        this.iconId = iconId;
    }
}
