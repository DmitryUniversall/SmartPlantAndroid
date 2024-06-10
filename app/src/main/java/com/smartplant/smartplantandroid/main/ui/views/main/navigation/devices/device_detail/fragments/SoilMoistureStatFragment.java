package com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.device_detail.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.data.Entry;
import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.main.ui.items.button.CustomButton;

import java.util.ArrayList;
import java.util.List;

public class SoilMoistureStatFragment extends ChartCustomFragment {
    protected static final @NonNull List<Entry> _chartData = new ArrayList<>();

    private CustomButton _irrigationSettingsButton;
    private CustomButton _irrigateButton;

    @NonNull
    @Override
    public List<Entry> getDataSet() {
        return _chartData;
    }

    @NonNull
    @Override
    protected View inflate(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.device_detail_fragment_soil_moisture, container, false);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        this._irrigationSettingsButton = root.findViewById(R.id.irrigation_settings_button);
        this._irrigateButton = root.findViewById(R.id.irrigate_button);

        this._irrigationSettingsButton.setOnClickListener(this::onIrrigationSettingsClick);
        this._irrigateButton.setOnClickListener(this::onIrrigateClick);
        return root;
    }

    private void onIrrigateClick(View view) {
    }

    private void onIrrigationSettingsClick(View view) {
    }
}
