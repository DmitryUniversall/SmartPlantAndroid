package com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.device_detail.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.data.Entry;
import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.core.ui.CustomFragment;
import com.smartplant.smartplantandroid.main.components.auth.models.User;
import com.smartplant.smartplantandroid.main.components.sensors_data.models.SensorsData;
import com.smartplant.smartplantandroid.main.ui.items.button.CustomButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class IlluminationStatFragment extends ChartCustomFragment {
    protected final @NonNull List<Entry> _chartData = new ArrayList<>();

    private CustomButton _illuminationSettingsButton;
    private CustomButton _toggleLampButton;

    public IlluminationStatFragment(User device) {
        super(device);
    }

    @NonNull
    @Override
    public List<Entry> getDataSet() {
        return _chartData;
    }

    @Override
    public void setData(List<Entry> dataSet) {
        this._chartData.clear();
        this._chartData.addAll(dataSet);
    }

    @Override
    public void addSensorsData(SensorsData sensorsData) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sensorsData.getCreatedAt());

        this.addChartEntry(new Entry(calendar.get(Calendar.HOUR_OF_DAY) * 3600000L +
                calendar.get(Calendar.MINUTE) * 60000L +
                calendar.get(Calendar.SECOND) * 1000L, (int) Math.round((sensorsData.getIllumination() / 4096d) * 100d)));
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

        setupChartData();
        return root;
    }

    private void setupChartData() {

    }

    private void onToggleLampClick(View view) {
        _toggleLampButton.setEnabled(false);
        _toggleLampButton.setText("Запрос...");
        this._viewModel.commandLamp(_device.getId(), 30, true, true)
                .onSuccess((result, dataMessage, response) -> this._toggleLampButton.setText(result ? "Выключить освещение" : "Включить освещение"))
                .after(() -> {
                    _toggleLampButton.setEnabled(true);
                    _toggleLampButton.setText("Включить освещение");  // TODO
                })
                .send();
    }

    private void onIlluminationSettingsClick(View view) {

    }
}
