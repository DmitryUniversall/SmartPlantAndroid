package com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.device_detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.core.exceptions.ApplicationResponseException;
import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.core.ui.CustomFragment;
import com.smartplant.smartplantandroid.main.ui.items.button.CustomButton;

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

    private void observeSensorsData() {}

    private void onLampClick(View v) {
//        this.viewModel.setLampState(true, 30).observe(this, result -> {
//            if (result.success) {
//                AppLogger.debug("Success lamp state changed");
//            } else {
//                AppLogger.error("Lamp stage change failed", result.error);
//            }
//        });
    }

    private void onWaterClick(View v) {}
}
