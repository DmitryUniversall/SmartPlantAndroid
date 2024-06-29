package com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.device_detail.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.data.Entry;
import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.main.components.auth.models.User;
import com.smartplant.smartplantandroid.main.components.sensors_data.models.SensorsData;
import com.smartplant.smartplantandroid.main.ui.items.button.CustomButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SoilMoistureStatFragment extends ChartCustomFragment {
    protected final @NonNull List<Entry> _chartData = new ArrayList<>();

    private CustomButton _irrigationSettingsButton;
    private CustomButton _irrigateButton;

    public SoilMoistureStatFragment(User device) {
        super(device);
    }

    @Override
    public ImageView getIconView(View deviceDetailRoot) {
        return deviceDetailRoot.findViewById(R.id.soil_moisture_card_icon);
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
                calendar.get(Calendar.SECOND) * 1000L, (int) Math.round((sensorsData.getSoilMoisture() / 4096d) * 100d)));
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
        _irrigateButton.setEnabled(false);
        _irrigateButton.setText("Полив...");
        _viewModel.commandIrrigate(_device.getId(), 30)  // TODO: timeout should depends on irrigate time
                .after(() -> {
                    getActivity().runOnUiThread(() -> {
                        _irrigateButton.setEnabled(true);
                        _irrigateButton.setText("Полить");
                    });
                })
                .send();
    }

    private void onIrrigationSettingsClick(View view) {
    }
}
