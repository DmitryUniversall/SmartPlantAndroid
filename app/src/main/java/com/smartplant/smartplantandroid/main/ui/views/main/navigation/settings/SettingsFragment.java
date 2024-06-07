package com.smartplant.smartplantandroid.main.ui.views.main.navigation.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.core.ui.CustomFragment;

public class SettingsFragment extends CustomFragment {
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_fragment_settings, container, false);
        return root;
    }
}
