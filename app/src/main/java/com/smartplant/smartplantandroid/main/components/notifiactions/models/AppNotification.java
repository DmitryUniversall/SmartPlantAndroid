package com.smartplant.smartplantandroid.main.components.notifiactions.models;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AppNotification {
    private @Nullable Long _id;
    private int _deviceId;
    private boolean _isChecked;
    private String _title;
    private String _description;
    private @DrawableRes int _imageResId;
    private final List<Integer> _actions;

    public AppNotification(@Nullable Long id, int deviceId, boolean isChecked, String title, String description, @DrawableRes int imageResId, @Nullable List<Integer> actions) {
        this._id = id;
        this._actions = actions != null ? actions : new ArrayList<>();
        this._deviceId = deviceId;
        this._isChecked = isChecked;
        this._title = title;
        this._description = description;
        this._imageResId = imageResId;
    }

    @Nullable
    public Long getId() {
        return _id;
    }

    public void setId(@Nullable Long id) {
        this._id = id;
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(@NonNull String title) {
        this._title = title;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(@NonNull String description) {
        this._description = description;
    }

    public int getImageResId() {
        return _imageResId;
    }

    public void setImageResId(@DrawableRes int imageResId) {
        this._imageResId = imageResId;
    }

    public List<Integer> getActions() {
        return _actions;
    }

    public int getDeviceId() {
        return _deviceId;
    }

    public void setDeviceId(int deviceId) {
        this._deviceId = deviceId;
    }

    public boolean isChecked() {
        return _isChecked;
    }

    public void setChecked(boolean checked) {
        this._isChecked = checked;
    }
}
