package com.smartplant.smartplantandroid.ui.views.views.main.fragments.devices;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.utils.ui.CustomFragment;

public class DeviceDetailFragment extends CustomFragment {
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment_device_detail, container, false);
    }
}
