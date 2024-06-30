package com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.devices_list;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.core.network.http.http_api_request.HTTPApiRequest;
import com.smartplant.smartplantandroid.core.ui.CustomFragment;
import com.smartplant.smartplantandroid.main.components.auth.models.User;
import com.smartplant.smartplantandroid.main.components.notifiactions.models.AbstractAppNotification;
import com.smartplant.smartplantandroid.main.components.notifiactions.repository.NotificationsRepositoryST;
import com.smartplant.smartplantandroid.main.components.notifiactions.utils.generics.AppNotificationFactory;
import com.smartplant.smartplantandroid.main.components.sensors_data.repository.SensorsDataRepositoryST;
import com.smartplant.smartplantandroid.main.ui.items.button.CustomButton;
import com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.devices_list.dialogs.AddDeviceDialog;
import com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.devices_list.items.DeviceCardItemAdapter;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class DevicesListFragment extends CustomFragment {
    // UI
    private DeviceCardItemAdapter _deviceAdapter;
    private SwipeRefreshLayout _swipeRefreshLayout;
    private CustomButton _addDeviceButton;

    // Utils
    private DevicesListViewModel _devicesViewModel;
    private SensorsDataRepositoryST _sensorsDataRepository;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_fragment_devices, container, false);
        _devicesViewModel = new ViewModelProvider(this).get(DevicesListViewModel.class);
        _sensorsDataRepository = SensorsDataRepositoryST.getInstance();

        RecyclerView devicesContainer = root.findViewById(R.id.devices_list_container);
        devicesContainer.setLayoutManager(new LinearLayoutManager(getContext()));

        this._deviceAdapter = new DeviceCardItemAdapter(getContext());
        devicesContainer.setAdapter(_deviceAdapter);

        _swipeRefreshLayout = root.findViewById(R.id.devices_swipe_refresh_layout);
        _swipeRefreshLayout.setOnRefreshListener(this::_refreshCards);

        _addDeviceButton = root.findViewById(R.id.addDeviceButton);
        _addDeviceButton.setOnClickListener(this::_onAddButtonClick);

        this._initializeCards();
        return root;
    }

    private void _initializeCards() {
        this._observeDevices();

        if (this._devicesViewModel.isLoaded()) {  // TODO: isLoaded: is it bad solution?
            this._deviceAdapter.setDeviceList(new ArrayList<>(this._devicesViewModel.getDevices().values()));
        } else {
            this._fetchDevices().send();
        }
    }

    private void _observeDevices() {
        this._devicesViewModel.getDevicesLiveData().observe(
                getViewLifecycleOwner(),
                devices -> {
                    this._deviceAdapter.setDeviceList(new ArrayList<>(devices.values()));
                }
        );
    }

    private HTTPApiRequest<Map<Integer, User>> _fetchDevices() {
        return this._devicesViewModel.fetchMyDevices();
    }

    private void _refreshCards() {
        _sensorsDataRepository.clearCache();
        this._fetchDevices().after(() -> this._swipeRefreshLayout.setRefreshing(false)).send();
    }

    private void _onAddButtonClick(View view) {
        Context context = getContext();
        assert context != null;

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        assert activity != null;

        new AddDeviceDialog(context, activity).show();
    }
}
