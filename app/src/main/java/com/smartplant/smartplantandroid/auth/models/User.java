package com.smartplant.smartplantandroid.auth.models;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;

import java.util.Date;

public class User {
    @SerializedName("id")
    private final int _id;

    @SerializedName("created_at")
    private final @NonNull Date _createdAt;

    @SerializedName("username")
    private @NonNull String _username;

    public User(int id, @NonNull String username, @NonNull Date created_at) {
        this._id = id;
        this._username = username;
        this._createdAt = created_at;
    }

    public int getId() {
        return _id;
    }

    @NonNull
    public Date getCreated_at() {
        return _createdAt;
    }

    @NonNull
    public String getUsername() {
        return _username;
    }

    public void setUsername(@NonNull String username) {
        this._username = username;
    }
}
