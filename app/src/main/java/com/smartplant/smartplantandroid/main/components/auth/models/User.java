package com.smartplant.smartplantandroid.main.components.auth.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class User {
    @SerializedName("id")
    private final int _id;

    @SerializedName("created_at")
    private final Date _createdAt;

    @SerializedName("username")
    private final String _username;

    @SerializedName("is_device")
    private final boolean _isDevice;

    public User(int id, String username, Date created_at, boolean _isDevice) {
        this._id = id;
        this._username = username;
        this._createdAt = created_at;
        this._isDevice = _isDevice;
    }

    public int getId() {
        return _id;
    }

    public Date getCreatedAt() {
        return _createdAt;
    }

    public String getUsername() {
        return _username;
    }

    public boolean isDevice() {
        return this._isDevice;
    }
}
