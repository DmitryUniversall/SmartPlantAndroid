package com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.device_detail.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.data.Entry;
import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.main.components.auth.models.User;
import com.smartplant.smartplantandroid.main.components.cultivation_rules.models.CultivationRules;
import com.smartplant.smartplantandroid.main.components.sensors_data.models.SensorsData;
import com.smartplant.smartplantandroid.main.ui.items.button.CustomButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class IlluminationStatFragment extends CultivationCustomFragment {
    // UI
    private CustomButton _toggleLampButton;

    // Data
    protected final @NonNull List<Entry> _chartData = new ArrayList<>();

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
    public ImageView getIconView(View deviceDetailRoot) {
        return deviceDetailRoot.findViewById(R.id.illumination_card_icon);
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

        this._toggleLampButton = root.findViewById(R.id.toggle_lamp_button);
        this._toggleLampButton.setOnClickListener(this::onToggleLampClick);

        return root;
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

    @Override
    protected int getSetMinButtonId() {
        return R.id.set_min_illumination;
    }

    @Override
    protected int getSetMaxButtonId() {
        return R.id.set_max_illumination;
    }

    @Override
    protected int getMinValue(@NonNull CultivationRules cultivationRules) {
        return cultivationRules.getMinIlluminationPercent();
    }

    @Override
    protected int getMaxValue(@NonNull CultivationRules cultivationRules) {
        return cultivationRules.getMaxIlluminationPercent();
    }

    @NonNull
    @Override
    protected String getMinTitle() {
        Context context = getContext();
        assert context != null;

        return context.getString(R.string.min_illumination) + " (%)";
    }

    @NonNull
    @Override
    protected String getMaxTitle() {
        Context context = getContext();
        assert context != null;

        return context.getString(R.string.max_illumination) + " (%)";
    }

    @Override
    protected void setMin(@NonNull CultivationRules rules, int value) {
        rules.setMinIlluminationPercent(value);
        this._cultivationRulesRepository.updateCultivationRules(rules).execute();
    }

    @Override
    protected void setMax(@NonNull CultivationRules rules, int value) {
        rules.setMaxIlluminationPercent(value);
        this._cultivationRulesRepository.updateCultivationRules(rules).execute();
    }
}
