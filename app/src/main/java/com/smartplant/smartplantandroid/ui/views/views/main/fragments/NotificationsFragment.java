package com.smartplant.smartplantandroid.ui.views.views.main.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.utils.ui.CustomFragment;

public class NotificationsFragment extends CustomFragment {
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_fragment_notifications, container, false);
        return root;
    }
}
