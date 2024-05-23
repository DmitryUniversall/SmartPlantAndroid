package com.smartplant.smartplantandroid.auth.models;

import java.util.Date;

public class User {
    private final int id;
    private final Date created_at;
    private String username;

    public User(int id, String username, Date created_at) {
        this.id = id;
        this.username = username;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
