package com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.devices_list.items;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.core.exceptions.TimeOutException;
import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.main.components.auth.models.User;
import com.smartplant.smartplantandroid.main.components.devices.utils.DevicesLocalDataManagerST;
import com.smartplant.smartplantandroid.main.components.sensors_data.models.SensorsData;
import com.smartplant.smartplantandroid.main.components.sensors_data.repository.SensorsDataRepositoryST;
import com.smartplant.smartplantandroid.main.ui.views.main.MainActivity;
import com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.device_detail.DeviceDetailViewModel;

@SuppressLint("ViewConstructor")
public class DeviceCardItem extends LinearLayout {
    private TextView _titleTextView;
    private TextView _descriptionTextView;
    private ImageView _iconImageView;
    private ProgressBar _progressBar;
    private TextView _statusTextView;
    private View _statusIndicatorView;
    private CardView _itemCard;

    private User _device;
    private SensorsData _sensorsData;
    private DeviceCardStatus _status = DeviceCardStatus.LOADING;

    private DevicesLocalDataManagerST _devicesLocalDataManager;
    private SensorsDataRepositoryST _sensorsDataRepository;
    private DeviceDetailViewModel _deviceDetailViewModel;

    public enum DeviceCardStatus {
        LOADING,
        ON,
        OFF
    }

    public DeviceCardItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DeviceCardItem(Context context) {
        super(context);
        init();
    }

    private void init() {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.main_item_device_card, this, true);
        root.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        _titleTextView = root.findViewById(R.id.title);
        _descriptionTextView = root.findViewById(R.id.description);
        _iconImageView = root.findViewById(R.id.icon);
        _progressBar = root.findViewById(R.id.progress_bar);
        _statusTextView = root.findViewById(R.id.status);
        _statusIndicatorView = root.findViewById(R.id.status_indicator);
        _itemCard = root.findViewById(R.id.item_card);

        _devicesLocalDataManager = DevicesLocalDataManagerST.getInstance();
        _sensorsDataRepository = SensorsDataRepositoryST.getInstance();
        _deviceDetailViewModel = new ViewModelProvider((MainActivity) getContext()).get(DeviceDetailViewModel.class);
    }

    public void bind(@NonNull User device) {
        this._device = device;
        _setCardInfo();

        SensorsData sensorsData = _sensorsDataRepository.getCachedSensorsData(device.getId());
        if (sensorsData != null) {  // Get form cache if exist, send request otherwise
            this._sensorsData = sensorsData;
            this.setStatus(DeviceCardStatus.ON);
            this._processInitialized();
        } else {
            this._requestForState();
        }
    }

    private void _requestForState() {
        this.setStatus(DeviceCardStatus.LOADING);

        this._deviceDetailViewModel.liveRequestSensorsData(this._device.getId(), 5).observe((MainActivity) getContext(), result -> {
            if (result.success) {
                assert result.result != null;

                this._sensorsData = result.result;
                this.setStatus(DeviceCardStatus.ON);
                this._processInitialized();
            } else {
                if (!(result.error instanceof TimeOutException))
                    AppLogger.error("Unknown error during requesting device status", result.error);
                this.setStatus(DeviceCardStatus.OFF);
            }
        });
    }

    private void _setCardInfo() {
        this._setInactiveBackground();

        int deviceId = this._device.getId();

        String title = this._devicesLocalDataManager.getOrSetDeviceName(deviceId, _device.getUsername());
        String description = this._devicesLocalDataManager.getOrSetDeviceDescription(deviceId, getContext().getString(R.string.default_device_description));
        int iconId = this._devicesLocalDataManager.getOrSetDeviceIconId(deviceId, R.drawable.icon_house);

        this.setTitle(title);
        this.setDescription(description);
        this.setIcon(iconId);
    }

    private void _processInitialized() {
        this.setProgress(this._sensorsData.getWaterLevel());
        this.setOnClickListener(this::_onCardClick);
    }

    private void _onCardClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("deviceId", this._device.getId());
        Navigation.findNavController(view).navigate(R.id.action_nav_devices_to_nav_device_detail, bundle);
    }

    private void _setInactiveBackground() {
        this._itemCard.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.L1_device_card_background_inactive));
        this._itemCard.setAlpha(0.6f);
    }

    private void _setActiveBackground() {
        this._itemCard.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.L1_device_card_background_active));
        this._itemCard.setAlpha(1);
    }

    public void setTitle(String title) {
        _titleTextView.setText(title);
    }

    public void setDescription(String description) {
        _descriptionTextView.setText(description);
    }

    public void setIcon(int resId) {
        _iconImageView.setImageResource(resId);
    }

    public void setProgress(int progress) {
        _progressBar.setProgress(progress);
    }

    private DeviceCardStatus getStatus() {
        return this._status;
    }

    public void setStatus(DeviceCardStatus status) {
        this._status = status;

        if (status == DeviceCardStatus.ON) {
            this._setActiveBackground();
            _statusTextView.setText(R.string.on);
            _statusTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.device_on));
            _statusIndicatorView.setBackgroundResource(R.drawable.icon_device_active);
        } else if (status == DeviceCardStatus.OFF) {
            this._setInactiveBackground();
            _statusTextView.setText(R.string.off);
            _statusTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.device_off));
            _statusIndicatorView.setBackgroundResource(R.drawable.icon_device_inactive);
        } else {
            this._setInactiveBackground();
            _statusTextView.setText(R.string.loading_with_dots);
            _statusTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.L1_text_secondary));
            _statusIndicatorView.setBackground(null);
        }
    }
}
