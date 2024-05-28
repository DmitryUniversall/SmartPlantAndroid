package com.smartplant.smartplantandroid.ui.views.views.main.fragments.devices;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.ui.viewmodels.main.DevicesViewModel;
import com.smartplant.smartplantandroid.utils.AppLogger;
import com.smartplant.smartplantandroid.utils.ui.CustomFragment;

public class DevicesFragment extends CustomFragment {
    private DevicesViewModel devicesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        devicesViewModel = new ViewModelProvider(this).get(DevicesViewModel.class);

        View root = inflater.inflate(R.layout.main_fragment_devices, container, false);
        return root;
    }
}
