package com.smartplant.smartplantandroid.ui.views.views.main.fragments.devices;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.ui.viewmodels.main.devices.DeviceDetailViewModel;
import com.smartplant.smartplantandroid.utils.ui.CustomFragment;
import com.smartplant.smartplantandroid.utils.ui.components.CustomButton;

import java.util.Locale;

public class DeviceDetailFragment extends CustomFragment {
    private DeviceDetailViewModel viewModel;

    private TextView temperatureTextView;
    private TextView soilMoistureTextView;
    private TextView humidityTextView;
    private TextView illuminationTextView;

    private CustomButton lampToggleButton;
    private CustomButton waterButton;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(DeviceDetailViewModel.class);

        View root = inflater.inflate(R.layout.main_fragment_device_detail, container, false);

        this.temperatureTextView = root.findViewById(R.id.temperatureValueView);
        this.soilMoistureTextView = root.findViewById(R.id.soilMoistureValueView);
        this.humidityTextView = root.findViewById(R.id.airHumidityValueView);
        this.illuminationTextView = root.findViewById(R.id.illuminationValueView);

        this.lampToggleButton = root.findViewById(R.id.lampToggleButton);
        this.waterButton = root.findViewById(R.id.waterButton);

        this.lampToggleButton.setOnClickListener(this::onLampClick);
        this.waterButton.setOnClickListener(this::onWaterClick);

        this.observeSensorsData();
        this.observerLampStateChange();
        this.requestDataUpdate();

        return root;
    }

    private void setTemperature(int temperature) {
        this.temperatureTextView.setText(String.format(Locale.US, "%d °C", temperature));
    }

    private void setSoilMoisture(int soilMoisturePercent) {
        this.soilMoistureTextView.setText(String.format(Locale.US, "%d%%", soilMoisturePercent));
    }

    private void setHumidity(int humidityPercent) {
        this.humidityTextView.setText(String.format(Locale.US, "%d%%", humidityPercent));
    }

    private void setIllumination(int illuminationPercent) {
        this.illuminationTextView.setText(String.format(Locale.US, "%d%%", illuminationPercent));
    }

    private void observeSensorsData() {
        this.viewModel.getSensorsDataLive().observe(getViewLifecycleOwner(), sensorsData -> {
            this.setHumidity((int) Math.round(sensorsData.getHumidity()));
            this.setTemperature((int) Math.round(sensorsData.getTemperature()));
            this.setSoilMoisture((int) Math.round((sensorsData.getSoilMoisture() / 4096d) * 100d));
            this.setIllumination((int) Math.round((sensorsData.getIllumination() / 4096d) * 100d));
        });
    }

    private void observerLampStateChange() {
        this.viewModel.getLampStateLive().observe(
                getViewLifecycleOwner(), newState -> this.lampToggleButton.setText(newState ? R.string.DeviceOFFAction : R.string.DeviceONAction)
        );
    }

    public void requestDataUpdate() {
        this.viewModel.requestDataUpdate().send();
    }

    private void onLampClick(View v) {
        Boolean value = this.viewModel.getLampStateLive().getValue();
        if (value == null) value = false;

        this.viewModel.setLampState(!value).send();
    }

    private void onWaterClick(View v) {
        this.viewModel.water().send();
        // TODO: disable button
    }
}
