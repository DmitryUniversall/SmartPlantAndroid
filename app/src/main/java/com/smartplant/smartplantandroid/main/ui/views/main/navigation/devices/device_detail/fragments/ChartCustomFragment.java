package com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.device_detail.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.core.ui.CustomFragment;
import com.smartplant.smartplantandroid.core.ui.charts.formatters.TimeValueFormatter;
import com.smartplant.smartplantandroid.main.components.auth.models.User;
import com.smartplant.smartplantandroid.main.components.sensors_data.models.SensorsData;
import com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.device_detail.DeviceDetailViewModel;

import java.util.Collection;
import java.util.List;

public abstract class ChartCustomFragment extends CustomFragment {
    // Styles
    protected final @ColorRes int _noDataTextColorId = R.color.L1_primary;
    protected final @ColorRes int _backgroundColorId = R.color.L1_background_primary;

    // Texts
    protected final @StringRes int _dataLabel = R.string.sensor_data;

    // UI
    protected final @IdRes int _chartId = R.id.stat_data_line_chart;
    protected LineChart _lineChart;

    // Utils
    protected DeviceDetailViewModel _viewModel;

    // Data
    protected User _device;

    protected abstract @NonNull View inflate(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract @NonNull List<Entry> getDataSet();

    public abstract void setData(List<Entry> dataSet);

    public abstract void addSensorsData(SensorsData sensorsData);

    public ChartCustomFragment(User device) {
        super();
        this._device = device;
    }

    public @NonNull View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this._viewModel = new ViewModelProvider(this).get(DeviceDetailViewModel.class);

        View root = this.inflate(inflater, container, savedInstanceState);

        this._lineChart = root.findViewById(this._chartId);
        this._setupChart();

        return root;
    }

    protected void _setupChart() {
        Context context = getContext();
        assert context != null;

        _lineChart.getDescription().setEnabled(false);
        _lineChart.setTouchEnabled(true);
        _lineChart.setDragEnabled(true);
        _lineChart.setScaleEnabled(true);
        _lineChart.setPinchZoom(true);
        _lineChart.setDrawGridBackground(false);
        _lineChart.setBackgroundColor(ContextCompat.getColor(context, this._backgroundColorId));
        _lineChart.setNoDataTextColor(ContextCompat.getColor(context, this._noDataTextColorId));  // TODO: make color danger?

        this._setupLegend();
        this._setupXAxis();
        this.updateChartData();
    }

    protected void _setupLegend() {
        Legend legend = _lineChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
    }

    protected void _setupXAxis() {
        XAxis xAxis = _lineChart.getXAxis();
        xAxis.setValueFormatter(new TimeValueFormatter());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
    }

    protected @Nullable LineData _getLineDataSet() {
        List<Entry> entries = this.getDataSet();
        if (entries.isEmpty()) return null;

        LineDataSet dataSet = new LineDataSet(entries, getString(this._dataLabel));
        return new LineData(dataSet);
    }

    public void addChartEntry(Entry entry) {
        this.getDataSet().add(entry);
    }

    public void addAllEntries(Collection<Entry> entries) {
        this.getDataSet().addAll(entries);
    }

    public void removeEntry(Entry entry) {
        this.getDataSet().remove(entry);
    }

    public void updateChartData() {
        @Nullable LineData data = this._getLineDataSet();
        if (data != null) _lineChart.setData(data);
        _lineChart.invalidate();
    }
}
