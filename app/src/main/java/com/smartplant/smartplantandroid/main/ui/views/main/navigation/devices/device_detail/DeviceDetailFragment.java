package com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.device_detail;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.data.Entry;
import com.google.android.flexbox.FlexboxLayout;
import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.core.ui.CustomFragment;
import com.smartplant.smartplantandroid.main.components.auth.models.User;
import com.smartplant.smartplantandroid.main.components.sensors_data.models.SensorsData;
import com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.device_detail.fragments.ChartCustomFragment;
import com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.device_detail.fragments.HumidityStatFragment;
import com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.device_detail.fragments.IlluminationStatFragment;
import com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.device_detail.fragments.SoilMoistureStatFragment;
import com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.device_detail.fragments.TemperatureStatFragment;

import java.util.Locale;

public class DeviceDetailFragment extends CustomFragment {
    private enum StatFragmentState {
        TEMPERATURE(1),
        SOIL_MOISTURE(2),
        HUMIDITY(3),
        ILLUMINATION(4);

        private final int _value;

        StatFragmentState(int value) {
            this._value = value;
        }

        public int getValue() {
            return _value;
        }
    }

    // Current stat-fragment
    private StatFragmentState _statFragmentState = StatFragmentState.TEMPERATURE;
    private ChartCustomFragment _currentStatFragment;

    // Stat-Fragments
    private TemperatureStatFragment _temperatureStatFragment;
    private SoilMoistureStatFragment _soilMoistureStatFragment;
    private HumidityStatFragment _humidityStatFragment;
    private IlluminationStatFragment _illuminationStatFragment;
    private ChartCustomFragment _defaultStatFragment;

    // Animations
    private static final int[] _animationOutRightInLeft = {R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left};
    private static final int[] _animationOutLeftInRight = {R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right};

    // Stat-Cards
    private FlexboxLayout _temperatureStatCard;
    private FlexboxLayout _soilMoistureStatCard;
    private FlexboxLayout _humidityStatCard;
    private FlexboxLayout _illuminationStatCard;

    // Data views
    private TextView _temperatureTextView;
    private TextView _soilMoistureTextView;
    private TextView _humidityTextView;
    private TextView _illuminationTextView;
    private ProgressBar _waterLevelProgressBar;

    // Utils
    private DeviceDetailViewModel _viewModel;

    // Data
    private User _device;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this._viewModel = new ViewModelProvider(this).get(DeviceDetailViewModel.class);

        if (getArguments() == null)
            throw new IllegalArgumentException("Device detail fragment requires deviceId argument");
        int deviceId = getArguments().getInt("deviceId");
        this._device = this._viewModel.getDeviceById(deviceId);
        if (this._device == null)
            throw new IllegalArgumentException(String.format("Unable to get device by id=%d", deviceId));

        _temperatureStatFragment = new TemperatureStatFragment(_device);
        _soilMoistureStatFragment = new SoilMoistureStatFragment(_device);
        _humidityStatFragment = new HumidityStatFragment(_device);
        _illuminationStatFragment = new IlluminationStatFragment(_device);
        _defaultStatFragment = _temperatureStatFragment;

        View root = inflater.inflate(R.layout.main_fragment_device_detail, container, false);
        this._waterLevelProgressBar = root.findViewById(R.id.water_level_progress_bar);

        this._setupCards(root);
        this._setupObservers();
        this._getSensorsData();

        return root;
    }

    private ChartCustomFragment _getStateFragment(StatFragmentState state) {
        switch (state) {
            case TEMPERATURE:
                return this._temperatureStatFragment;
            case SOIL_MOISTURE:
                return this._soilMoistureStatFragment;
            case HUMIDITY:
                return this._humidityStatFragment;
            case ILLUMINATION:
                return this._illuminationStatFragment;
            default:
                return this._defaultStatFragment;
        }
    }

    private int[] _getAnimationForState(StatFragmentState currentState, StatFragmentState newState) {
        if (newState.getValue() > currentState.getValue()) {
            return DeviceDetailFragment._animationOutLeftInRight;
        } else {
            return DeviceDetailFragment._animationOutRightInLeft;
        }
    }

    private void _setStatActionState(StatFragmentState state) {
        this._setActiveStatFragment(_getStateFragment(state), this._getAnimationForState(this._statFragmentState, state));
        this._statFragmentState = state;
    }

    private void _setActiveStatFragment(ChartCustomFragment newFragment, int[] animation) {
        Context context = getContext();
        assert context != null;

//        this._replaceFragmentWithFullAnimation(this._currentStatFragment, newFragment, R.id.stat_action_container, R.anim.expand_in, R.anim.expand_out);
        this._replaceFragment(R.id.stat_action_container, newFragment, animation);
        this._currentStatFragment = newFragment;
    }

    private void _setupCards(View root) {
        // Set default opened stat-fragment
        this._setStatActionState(StatFragmentState.TEMPERATURE);

        // Setup cards and listeners
        this._temperatureStatCard = root.findViewById(R.id.temperature_stat_card);
        this._soilMoistureStatCard = root.findViewById(R.id.soil_moisture_stat_card);
        this._humidityStatCard = root.findViewById(R.id.humidity_stat_card);
        this._illuminationStatCard = root.findViewById(R.id.illumination_stat_card);
        this._setupCardsListeners();

        // Setup data views
        this._temperatureTextView = root.findViewById(R.id.temperatureValueView);
        this._soilMoistureTextView = root.findViewById(R.id.soilMoistureValueView);
        this._humidityTextView = root.findViewById(R.id.airHumidityValueView);
        this._illuminationTextView = root.findViewById(R.id.illuminationValueView);
    }

    private void _setupCardsListeners() {
        this._temperatureStatCard.setOnClickListener(v -> {
            AppLogger.debug("_temperatureStatCard");
            this._setStatActionState(StatFragmentState.TEMPERATURE);
        });

        this._soilMoistureStatCard.setOnClickListener(v -> {
            AppLogger.debug("_soilMoistureStatCard");
            this._setStatActionState(StatFragmentState.SOIL_MOISTURE);
        });

        this._humidityStatCard.setOnClickListener(v -> {
            AppLogger.debug("_humidityStatCard");
            this._setStatActionState(StatFragmentState.HUMIDITY);
        });

        this._illuminationStatCard.setOnClickListener(v -> {
            AppLogger.debug("_illuminationStatCard");
            this._setStatActionState(StatFragmentState.ILLUMINATION);
        });
    }

    private void _setupObservers() {
        this._observeSensorsData();
    }

    private void _observeSensorsData() {
        this._viewModel.getSensorsLiveData().observe(getViewLifecycleOwner(), data -> {
            this._setSensorsDataToUI(data);

            _temperatureStatFragment.addSensorsData(data);
            _soilMoistureStatFragment.addSensorsData(data);
            _humidityStatFragment.addSensorsData(data);
            _illuminationStatFragment.addSensorsData(data);

            this._currentStatFragment.updateChartData();
        });
    }

    private void _getSensorsData() {
        this._viewModel.updateSensorsData(this._device.getId(), 5);
//        this._viewModel.getDailySensorsData(_device.getId())
//                .onSuccess(result -> {
//                    List<Entry> entries = new ArrayList<>();
//                    result.forEach((element) -> entries.add(new Entry(element.getCreatedAt(), element.getIllumination())));
//                    _temperatureStatFragment.setData(new Entry(result));
//                    _soilMoistureStatFragment.setData(new Entry(result));
//                    _humidityStatFragment.setData(new Entry(result));
//                    _illuminationStatFragment.setData(new Entry(result));
//                })
//                .onFailure(error -> AppLogger.warning("Failed to get daily sensors data"))
//                .execute();

        // Device should ignore it (respond with 1003 - NotChanged), if it already received this request before in active session
        this._viewModel.requestSensorsDataUpdate(this._device.getId(), 5).send();
    }

    private void _setSensorsDataToUI(@NonNull SensorsData sensorsData) {
        this._setHumidityData((int) Math.round(sensorsData.getHumidity()));
        this._setTemperatureData((int) Math.round(sensorsData.getTemperature()));
        this._setSoilMoistureData((int) Math.round((sensorsData.getSoilMoisture() / 4096d) * 100d));
        this._setIlluminationData((int) Math.round((sensorsData.getIllumination() / 4096d) * 100d));
        this._setWaterLevelData((int) Math.round((sensorsData.getWaterLevel() / 4096d) * 100d));
    }

    private void _setTemperatureData(int temperature) {
        this._temperatureTextView.setText(String.format(Locale.US, "%d °C", temperature));
    }

    private void _setSoilMoistureData(int soilMoisturePercent) {
        this._soilMoistureTextView.setText(String.format(Locale.US, "%d%%", soilMoisturePercent));
    }

    private void _setHumidityData(int humidityPercent) {
        this._humidityTextView.setText(String.format(Locale.US, "%d%%", humidityPercent));
    }

    private void _setIlluminationData(int illuminationPercent) {
        this._illuminationTextView.setText(String.format(Locale.US, "%d%%", illuminationPercent));
    }

    private void _setWaterLevelData(int waterLevel) {
        this._waterLevelProgressBar.setProgress(waterLevel);
    }
}
