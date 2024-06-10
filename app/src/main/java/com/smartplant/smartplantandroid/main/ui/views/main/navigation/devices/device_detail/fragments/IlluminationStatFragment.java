package com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.device_detail.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.data.Entry;
import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.core.ui.CustomFragment;
import com.smartplant.smartplantandroid.main.ui.items.button.CustomButton;

import java.util.ArrayList;
import java.util.List;

public class IlluminationStatFragment extends ChartCustomFragment {
    protected static final @NonNull List<Entry> _chartData = new ArrayList<>();

    private CustomButton _illuminationSettingsButton;
    private CustomButton _toggleLampButton;

    @NonNull
    @Override
    public List<Entry> getDataSet() {
        return _chartData;
    }

    @NonNull
    @Override
    protected View inflate(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.device_detail_fragment_illumination, container, false);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        this._illuminationSettingsButton = root.findViewById(R.id.illumination_settings_button);
        this._toggleLampButton = root.findViewById(R.id.toggle_lamp_button);

        this._illuminationSettingsButton.setOnClickListener(this::onIlluminationSettingsClick);
        this._toggleLampButton.setOnClickListener(this::onToggleLampClick);
        return root;
    }

    private void onIlluminationSettingsClick(View view) {
    }

    private void onToggleLampClick(View view) {
    }
}
