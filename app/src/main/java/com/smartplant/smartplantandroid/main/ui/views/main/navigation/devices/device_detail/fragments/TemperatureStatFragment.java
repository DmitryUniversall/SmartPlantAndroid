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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TemperatureStatFragment extends CultivationCustomFragment {
    // Data
    protected final @NonNull List<Entry> _chartData = new ArrayList<>();

    public TemperatureStatFragment(User device) {
        super(device);
    }

    @Override
    public ImageView getIconView(View deviceDetailRoot) {
        return deviceDetailRoot.findViewById(R.id.temperature_card_icon);
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

        this.addChartEntry(new Entry(
                calendar.get(Calendar.HOUR_OF_DAY) * 3600000L +
                        calendar.get(Calendar.MINUTE) * 60000L +
                        calendar.get(Calendar.SECOND) * 1000L,
                (float) Math.round(sensorsData.getTemperature() * 10.0f) / 10.0f)
        );
    }

    @NonNull
    @Override
    protected View inflate(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.device_detail_fragment_temperature, container, false);
    }

    @Override
    protected int getSetMinButtonId() {
        return R.id.set_min_temperature;
    }

    @Override
    protected int getSetMaxButtonId() {
        return R.id.set_max_temperature;
    }

    @Override
    protected int getMinValue(@NonNull CultivationRules cultivationRules) {
        return cultivationRules.getMinTemperature();
    }

    @Override
    protected int getMaxValue(@NonNull CultivationRules cultivationRules) {
        return cultivationRules.getMaxTemperature();
    }

    @NonNull
    @Override
    protected String getMinTitle() {
        Context context = getContext();
        assert context != null;

        return context.getString(R.string.min_temperature) + " (°C)";
    }

    @NonNull
    @Override
    protected String getMaxTitle() {
        Context context = getContext();
        assert context != null;

        return context.getString(R.string.max_temparature) + " (°C)";
    }

    @Override
    protected void setMin(@NonNull CultivationRules rules, int value) {
        rules.setMinTemperature(value);
        this._cultivationRulesRepository.updateCultivationRules(rules).execute();
    }

    @Override
    protected void setMax(@NonNull CultivationRules rules, int value) {
        rules.setMaxTemperature(value);
        this._cultivationRulesRepository.updateCultivationRules(rules).execute();
    }
}
