package com.smartplant.smartplantandroid.ui.views.views.main.fragments.devices;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.ui.viewmodels.main.devices.DevicesViewModel;
import com.smartplant.smartplantandroid.utils.ui.CustomFragment;

public class DevicesFragment extends CustomFragment {
    private DevicesViewModel devicesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        devicesViewModel = new ViewModelProvider(this).get(DevicesViewModel.class);
        View root = inflater.inflate(R.layout.main_fragment_devices, container, false);

        CardView cardView = root.findViewById(R.id.deviceCard1);
        cardView.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.action_nav_devices_to_nav_device_detail);
        });

        return root;
    }
}
