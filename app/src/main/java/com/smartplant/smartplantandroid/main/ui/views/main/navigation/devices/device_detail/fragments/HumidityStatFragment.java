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

public class HumidityStatFragment extends CultivationCustomFragment {
    // Data
    private final @NonNull List<Entry> _chartData = new ArrayList<>();

    public HumidityStatFragment(User device) {
        super(device);
    }

    @Override
    public ImageView getIconView(View deviceDetailRoot) {
        return deviceDetailRoot.findViewById(R.id.humidity_card_icon);
    }

    @NonNull
    @Override
    public List<Entry> getDataSet() {
        return _chartData;
    }

    @Override
    public void addSensorsData(SensorsData sensorsData) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sensorsData.getCreatedAt());

        this.addChartEntry(new Entry(calendar.get(Calendar.HOUR_OF_DAY) * 3600000L +
                calendar.get(Calendar.MINUTE) * 60000L +
                calendar.get(Calendar.SECOND) * 1000L, (float) Math.round(sensorsData.getHumidity() * 10.0f) / 10.0f));
    }

    @Override
    public void setData(List<Entry> dataSet) {
        this._chartData.clear();
        this._chartData.addAll(dataSet);
    }

    @NonNull
    @Override
    protected View inflate(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.device_detail_fragment_humidity, container, false);
    }

    @Override
    protected int getSetMinButtonId() {
        return R.id.set_min_humidity;
    }

    @Override
    protected int getSetMaxButtonId() {
        return R.id.set_max_humidity;
    }

    @Override
    protected int getMinValue(@NonNull CultivationRules cultivationRules) {
        return cultivationRules.getMinHumidityPercent();
    }

    @Override
    protected int getMaxValue(@NonNull CultivationRules cultivationRules) {
        return cultivationRules.getMaxHumidityPercent();
    }

    @NonNull
    @Override
    protected String getMinTitle() {
        Context context = getContext();
        assert context != null;

        return context.getString(R.string.min_humidity) + " (%)";
    }

    @NonNull
    @Override
    protected String getMaxTitle() {
        Context context = getContext();
        assert context != null;

        return context.getString(R.string.max_humidity) + " (%)";
    }

    @Override
    protected void setMin(@NonNull CultivationRules rules, int value) {
        rules.setMinHumidityPercent(value);
        this._cultivationRulesRepository.updateCultivationRules(rules).execute();
    }

    @Override
    protected void setMax(@NonNull CultivationRules rules, int value) {
        rules.setMaxHumidityPercent(value);
        this._cultivationRulesRepository.updateCultivationRules(rules).execute();
    }
}
