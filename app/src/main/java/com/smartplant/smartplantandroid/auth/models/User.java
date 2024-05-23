package com.smartplant.smartplantandroid.auth.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class User {
    @SerializedName("id")
    private final int _id;

    @SerializedName("created_at")
    private final Date _createdAt;

    @SerializedName("username")
    private String _username;

    public User(int id, String username, Date created_at) {
        this._id = id;
        this._username = username;
        this._createdAt = created_at;
    }

    public int getId() {
        return _id;
    }

    public Date getCreated_at() {
        return _createdAt;
    }

    public String getUsername() {
        return _username;
    }

    public void setUsername(String username) {
        this._username = username;
    }
}
