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

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.main.components.auth.models.User;
import com.smartplant.smartplantandroid.main.components.devices.utils.DevicesLocalDataManagerST;
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

    private User _device;
    private int _progress = 0;
    private boolean _status = false;

    private DevicesLocalDataManagerST _devicesLocalDataManager;
    private DeviceDetailViewModel _deviceDetailViewModel;

    public DeviceCardItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DeviceCardItem(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.main_item_device_card, this, true);
        root.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        _titleTextView = findViewById(R.id.title);
        _descriptionTextView = findViewById(R.id.description);
        _iconImageView = findViewById(R.id.icon);
        _progressBar = findViewById(R.id.progress_bar);
        _statusTextView = findViewById(R.id.status);
        _statusIndicatorView = findViewById(R.id.status_indicator);

        _devicesLocalDataManager = DevicesLocalDataManagerST.getInstance();
        _deviceDetailViewModel = new ViewModelProvider((MainActivity) context).get(DeviceDetailViewModel.class);
    }

    public void bind(User device) {
        this._device = device;
        _initializeCard(getContext());
        this._requestForState(getContext());  // TODO: Cache, do not request each time it is re-rendered
    }

    private void _requestForState(Context context) {  // TODO: Make gray filter for card when request is processing
        this._deviceDetailViewModel.requestSensorsData(this._device.getId(), 5).observe((MainActivity) context, result -> {
            if (result.success) {
                assert result.result != null;

                this._status = true;
                this._progress = result.result.getWaterLevel();
            } else {
                this._status = false;
            }

            this._setInitializedData(context);
        });
    }

    private void _initializeCard(Context context) {
        String title = this._devicesLocalDataManager.getDeviceName(this._device.getId());
        String description = this._devicesLocalDataManager.getDeviceDescription(this._device.getId());
        Integer iconId = this._devicesLocalDataManager.getDeviceIconId(this._device.getId());

        this.setTitle(title != null ? title : this._device.getUsername());
        this.setDescription(description != null ? description : context.getString(R.string.default_device_description));
        this.setIcon(iconId != null ? iconId : R.drawable.icon_house);
    }

    private void _setInitializedData(Context context) {
        this.setProgress(this._progress);
        this.setStatus(this._status);

        this.setOnClickListener(this::_onCardClick);
    }

    private void _onCardClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("deviceId", this._device.getId());
        Navigation.findNavController(view).navigate(R.id.action_nav_devices_to_nav_device_detail, bundle);
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

    public void setStatus(boolean status) {
        if (status) {
            _statusTextView.setText(R.string.on);
            _statusTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.device_on));
            _statusIndicatorView.setBackgroundResource(R.drawable.icon_device_active);
        } else {
            _statusTextView.setText(R.string.off);
            _statusTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.device_off));
            _statusIndicatorView.setBackgroundResource(R.drawable.icon_device_inactive);
        }
    }
}