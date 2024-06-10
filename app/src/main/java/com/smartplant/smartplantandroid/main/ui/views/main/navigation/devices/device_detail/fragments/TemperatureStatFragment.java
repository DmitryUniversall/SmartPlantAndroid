package com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.device_detail.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.data.Entry;
import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.core.ui.CustomFragment;

import java.util.ArrayList;
import java.util.List;

public class TemperatureStatFragment extends ChartCustomFragment {
    protected static final @NonNull List<Entry> _chartData = new ArrayList<>();

    @NonNull
    @Override
    public List<Entry> getDataSet() {
        return _chartData;
    }

    @NonNull
    @Override
    protected View inflate(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.device_detail_fragment_temperature, container, false);
    }
}
