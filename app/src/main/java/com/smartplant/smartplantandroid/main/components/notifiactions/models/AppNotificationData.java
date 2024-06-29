package com.smartplant.smartplantandroid.main.components.notifiactions.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AppNotificationData {
    private @Nullable Long _id;
    private final int _deviceId;
    private final int _notificationType;
    private boolean _isChecked;
    private @Nullable String _title;
    private @Nullable String _description;

    public AppNotificationData(@Nullable Long id, int deviceId, int notificationType, boolean isChecked, @Nullable String title, @Nullable String description) {
        this._id = id;
        this._deviceId = deviceId;
        this._notificationType = notificationType;
        this._isChecked = isChecked;
        this._title = title;
        this._description = description;
    }

    @Nullable
    public Long getId() {
        return _id;
    }

    public void setId(@Nullable Long id) {
        this._id = id;
    }

    public int getDeviceId() {
        return _deviceId;
    }

    public int getNotificationType() {
        return _notificationType;
    }

    public boolean isChecked() {
        return _isChecked;
    }

    public void setChecked(boolean checked) {
        this._isChecked = checked;
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

    public static class Builder {
        private @Nullable Long id;
        private int deviceId;
        private int notificationType;
        private boolean isChecked;
        private @Nullable String title;
        private @Nullable String description;

        public Builder() {
        }

        public Builder setId(@Nullable Long id) {
            this.id = id;
            return this;
        }

        public Builder setDeviceId(int deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public Builder setNotificationType(int notificationType) {
            this.notificationType = notificationType;
            return this;
        }

        public Builder setChecked(boolean isChecked) {
            this.isChecked = isChecked;
            return this;
        }

        public Builder setTitle(@Nullable String title) {
            this.title = title;
            return this;
        }

        public Builder setDescription(@Nullable String description) {
            this.description = description;
            return this;
        }

        public AppNotificationData build() {
            assert deviceId != 0;
            assert notificationType != 0;

            return new AppNotificationData(id, deviceId, notificationType, isChecked, title, description);
        }
    }
}
