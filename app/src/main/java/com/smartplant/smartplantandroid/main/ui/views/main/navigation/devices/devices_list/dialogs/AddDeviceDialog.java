package com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.devices_list.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.main.ui.items.button.CustomButton;
import com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.devices_list.DevicesListViewModel;

public class AddDeviceDialog extends AlertDialog {
    private EditText _deviceUsernameInput;
    private CustomButton _submitButton;
    private DevicesListViewModel _viewModel;
    private final AppCompatActivity _activity;

    public AddDeviceDialog(@NonNull Context context, AppCompatActivity viewModelStoreOwner) {
        super(context);
        this._activity = viewModelStoreOwner;
        init();
    }

    public AddDeviceDialog(@NonNull Context context, int themeResId, AppCompatActivity viewModelStoreOwner) {
        super(context, themeResId);
        this._activity = viewModelStoreOwner;
        init();
    }

    protected AddDeviceDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, AppCompatActivity viewModelStoreOwner) {
        super(context, cancelable, cancelListener);
        this._activity = viewModelStoreOwner;
        init();
    }

    private void init() {
        _viewModel = new ViewModelProvider(this._activity).get(DevicesListViewModel.class);

        View root = this._inflate(getLayoutInflater());
        setView(root);

        _deviceUsernameInput = root.findViewById(R.id.device_username_input);
        _submitButton = root.findViewById(R.id.request_device_add_button);

        _submitButton.setOnClickListener(this::_onSubmitButtonClick);
    }

    @SuppressLint("InflateParams")
    private View _inflate(LayoutInflater inflater) {
        return inflater.inflate(R.layout.dialog_add_device, null);
    }

    private void _onSubmitButtonClick(View v) {
        Context context = getContext();

        _submitButton.setText(R.string.request_with_dots);
        String deviceUsername = _deviceUsernameInput.getText().toString();

        this._viewModel.pairDevice(deviceUsername)
                .onSuccess((result, response, applicationResponse) -> {
                    if (applicationResponse.getApplicationStatusCode() != 4401) {
                        this._activity.runOnUiThread(() -> Toast.makeText(context, context.getString(R.string.failed_to_add_device), Toast.LENGTH_LONG).show());
                        return;
                    }

                    this.cancel();
                    this._activity.runOnUiThread(() -> Toast.makeText(context, context.getString(R.string.device_added_successfully), Toast.LENGTH_LONG).show());
                })
                .onFailure(error -> AppLogger.error("Got unknown error during device pair", error))
                .after(() -> this._activity.runOnUiThread(() -> _submitButton.setText(R.string.send_device_add_request)))
                .send();
    }
}
