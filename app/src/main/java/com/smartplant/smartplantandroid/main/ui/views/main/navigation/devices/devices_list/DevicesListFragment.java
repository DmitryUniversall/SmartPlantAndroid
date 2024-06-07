package com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.devices_list;

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
import com.smartplant.smartplantandroid.core.ui.CustomFragment;

public class DevicesListFragment extends CustomFragment {
    private DevicesListViewModel devicesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        devicesViewModel = new ViewModelProvider(this).get(DevicesListViewModel.class);
        View root = inflater.inflate(R.layout.main_fragment_devices, container, false);

        CardView cardView = root.findViewById(R.id.deviceCard1);
        cardView.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.action_nav_devices_to_nav_device_detail);
        });

        return root;
    }
}
