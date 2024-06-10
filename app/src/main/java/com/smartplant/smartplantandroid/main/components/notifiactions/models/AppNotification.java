package com.smartplant.smartplantandroid.main.components.notifiactions.models;

import androidx.annotation.DrawableRes;
import androidx.annotation.ReturnThis;

import java.util.ArrayList;
import java.util.List;

public class AppNotification {
    private final String _title;
    private final String _description;
    private final int _imageResId;
    private final List<Integer> _actions = new ArrayList<>();

    public AppNotification(String title, String description, @DrawableRes int imageResId) {
        this._title = title;
        this._description = description;
        this._imageResId = imageResId;
    }

    @ReturnThis
    private AppNotification addActionView(int actionId) {
        this._actions.add(actionId);
        return this;
    }

    public List<Integer> getActions() {
        return _actions;
    }

    public String getTitle() {
        return _title;
    }

    public String getDescription() {
        return _description;
    }

    public int getImageResId() {
        return _imageResId;
    }
}
