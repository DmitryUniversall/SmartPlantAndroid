package com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.device_detail;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.flexbox.FlexboxLayout;
import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.core.ui.CustomFragment;
import com.smartplant.smartplantandroid.main.components.auth.models.User;
import com.smartplant.smartplantandroid.main.components.devices.utils.DevicesLocalDataManagerST;
import com.smartplant.smartplantandroid.main.components.sensors_data.models.SensorsData;
import com.smartplant.smartplantandroid.main.components.sensors_data.repository.SensorsDataRepositoryST;
import com.smartplant.smartplantandroid.main.state.settings.DevicesSettingsST;
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

    // Root
    private View _root;

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

    // Device info
    private TextView _deviceNameTextView;
    private TextView _deviceDescriprionTextView;

    // Utils
    private DeviceDetailViewModel _viewModel;
    private SensorsDataRepositoryST _sensorsDataRepository;
    private DevicesSettingsST _devicesSettings;
    private DevicesLocalDataManagerST _devicesLocalDataManager;

    // Data
    private User _device;

    // Other
    private final String sensorsDataHandlerName = "DeviceDetail";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this._viewModel = new ViewModelProvider(this).get(DeviceDetailViewModel.class);
        this._sensorsDataRepository = SensorsDataRepositoryST.getInstance();
        this._devicesSettings = DevicesSettingsST.getInstance();
        this._devicesLocalDataManager = DevicesLocalDataManagerST.getInstance();

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

        this._root = inflater.inflate(R.layout.main_fragment_device_detail, container, false);
        this._waterLevelProgressBar = this._root.findViewById(R.id.water_level_progress_bar);

        _deviceNameTextView = _root.findViewById(R.id.device_name_text);
        _deviceDescriprionTextView = _root.findViewById(R.id.device_description_text);

        this._setupDeviceInfo();
        this._setupCards(this._root);
        this._setupObservers();
        this._getSensorsData();

        return this._root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        _sensorsDataRepository.removeNewSensorsDataHandler(sensorsDataHandlerName);
    }

    private void _setupDeviceInfo() {
        _deviceNameTextView.setText(_devicesLocalDataManager.getDeviceName(this._device.getId()));
        _deviceDescriprionTextView.setText(_devicesLocalDataManager.getDeviceDescription(this._device.getId()));
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

        if (_currentStatFragment != null) _currentStatFragment.setActive(this._root, false);
        newFragment.setActive(this._root, true);
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
        _sensorsDataRepository.onNewSensorsData(sensorsDataHandlerName, this::_onNewSensorsData);  // TODO: Move to ViewModel
    }

    private void _onNewSensorsData(@NonNull SensorsData sensorsData) {
        this._setSensorsDataToUI(sensorsData);
        this._addSensorsDataToChart(sensorsData);
    }

    private void _getSensorsData() {
        this._viewModel.requestSensorsData(this._device.getId(), 5).onSuccess((result, dataMessage, response) -> this._onNewSensorsData(result));
        this._viewModel.getDailySensorsData(_device.getId())
                .onSuccess(dataArray -> {
                    AppLogger.info("Fetched last sensors data for device id=%d (size=%d)", this._device.getId(), dataArray.size());
                    dataArray.forEach(this::_addSensorsDataToChart);
                })
                .onFailure(error -> AppLogger.warning("Failed to get daily sensors data"))
                .execute();

        // Device should ignore it (respond with 1003 - NotChanged), if it already received this request before in active session
        this._viewModel.requestSensorsDataUpdate(this._device.getId(), _devicesSettings.getDataUpdateInterval(), 5).onSuccess((result, dataMessage, response) ->
                AppLogger.info("Sensors data update request success for device id=%d (code %s)", response.getStatusCode())
        ).send();
    }

    protected void _addSensorsDataToChart(@NonNull SensorsData sensorsData) {
        Activity activity = getActivity();
        assert activity != null;

        activity.runOnUiThread(() -> {
            _temperatureStatFragment.addSensorsData(sensorsData);
            _soilMoistureStatFragment.addSensorsData(sensorsData);
            _humidityStatFragment.addSensorsData(sensorsData);
            _illuminationStatFragment.addSensorsData(sensorsData);
            if (_currentStatFragment != null) this._currentStatFragment.updateChartData();
        });
    }

    private void _setSensorsDataToUI(@NonNull SensorsData sensorsData) {
        Activity activity = getActivity();
        assert activity != null;

        activity.runOnUiThread(() -> {
            this._setHumidityData((int) Math.round(sensorsData.getHumidity()));
            this._setTemperatureData((int) Math.round(sensorsData.getTemperature()));
            this._setSoilMoistureData((int) Math.round((sensorsData.getSoilMoisture() / 4096d) * 100d));
            this._setIlluminationData((int) Math.round((sensorsData.getIllumination() / 4096d) * 100d));
            this._setWaterLevelData((int) Math.round((sensorsData.getWaterLevel() / 4096d) * 100d));
        });
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
