package com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.devices_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.core.ui.CustomFragment;
import com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.devices_list.items.DeviceItemAdapter;

import java.util.ArrayList;

public class DevicesListFragment extends CustomFragment {
    // UI
    private DeviceItemAdapter deviceAdapter;

    // Utils
    private DevicesListViewModel _devicesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_fragment_devices, container, false);
        _devicesViewModel = new ViewModelProvider(this).get(DevicesListViewModel.class);

        RecyclerView devicesContainer = root.findViewById(R.id.devices_list_container);
        devicesContainer.setLayoutManager(new LinearLayoutManager(getContext()));

        this.deviceAdapter = new DeviceItemAdapter(getContext());
        devicesContainer.setAdapter(deviceAdapter);

        this._initializeCards();
        return root;
    }

    private void _initializeCards() {
        this._observeDevices();

        if (this._devicesViewModel.isLoaded()) {
            this.deviceAdapter.setDeviceList(new ArrayList<>(this._devicesViewModel.getDevices().values()));
        } else {
            this._fetchDevices();
        }
    }

    private void _observeDevices() {
        this._devicesViewModel.getDevicesLiveData().observe(
                getViewLifecycleOwner(),
                devices -> this.deviceAdapter.setDeviceList(new ArrayList<>(devices.values()))
        );
    }

    private void _fetchDevices() {
        this._devicesViewModel.fetchMyDevices().send();
    }
}
