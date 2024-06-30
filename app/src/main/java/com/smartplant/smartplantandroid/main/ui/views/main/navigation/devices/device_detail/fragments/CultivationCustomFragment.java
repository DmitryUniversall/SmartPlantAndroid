package com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.device_detail.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.smartplant.smartplantandroid.core.ui.edit_dialog.EditDialog;
import com.smartplant.smartplantandroid.main.components.auth.models.User;
import com.smartplant.smartplantandroid.main.components.cultivation_rules.models.CultivationRules;
import com.smartplant.smartplantandroid.main.components.cultivation_rules.repository.CultivationRulesRepositoryST;
import com.smartplant.smartplantandroid.main.ui.items.button.CustomButton;

public abstract class CultivationCustomFragment extends ChartCustomFragment {
    // Utils
    protected final CultivationRulesRepositoryST _cultivationRulesRepository;

    @IdRes
    protected abstract int getSetMinButtonId();

    @IdRes
    protected abstract int getSetMaxButtonId();

    protected abstract int getMinValue(@NonNull CultivationRules cultivationRules);

    protected abstract int getMaxValue(@NonNull CultivationRules cultivationRules);

    protected abstract @NonNull String getMinTitle();

    protected abstract @NonNull String getMaxTitle();

    protected abstract void setMin(@NonNull CultivationRules rules, int value);

    protected abstract void setMax(@NonNull CultivationRules rules, int value);

    protected @Nullable String validateMin(@NonNull String value) {
        return null;
    }

    protected @Nullable String validateMax(@NonNull String value) {
        return null;
    }

    public CultivationCustomFragment(User device) {
        super(device);
        this._cultivationRulesRepository = CultivationRulesRepositoryST.getInstance();
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);

        CustomButton setMinButton = root.findViewById(getSetMinButtonId());
        setMinButton.setOnClickListener(this::_onSetMinButtonClick);

        CustomButton setMaxButton = root.findViewById(getSetMaxButtonId());
        setMaxButton.setOnClickListener(this::_onSetMaxButtonClick);

        return root;
    }

    protected void _onSetMinButtonClick(View view) {
        Context context = getContext();
        assert context != null;

        Activity activity = getActivity();
        assert activity != null;

        this._cultivationRulesRepository.getOrCreateCultivationRules(this._device.getId()).onSuccess(
                rules -> activity.runOnUiThread(() -> {  // TODO: VALIDATE IT
                    EditDialog editDialog = new EditDialog(
                            context,
                            getMinTitle(),
                            String.valueOf(getMinValue(rules)),
                            InputType.TYPE_CLASS_NUMBER
                    );
                    editDialog.setValidator(this::validateMin);
                    editDialog.setProcessor(value -> this.setMin(rules, Integer.parseInt(value)));
                    editDialog.show();
                })
        ).execute();
    }

    private void _onSetMaxButtonClick(View view) {
        Context context = getContext();
        assert context != null;

        Activity activity = getActivity();
        assert activity != null;

        this._cultivationRulesRepository.getOrCreateCultivationRules(this._device.getId()).onSuccess(
                rules -> activity.runOnUiThread(() -> {
                    EditDialog editDialog = new EditDialog(
                            context,
                            getMaxTitle(),
                            String.valueOf(getMaxValue(rules)),
                            InputType.TYPE_CLASS_NUMBER
                    );
                    editDialog.setValidator(this::validateMax);
                    editDialog.setProcessor(value -> this.setMax(rules, Integer.parseInt(value)));
                    editDialog.show();
                })
        ).execute();
    }
}
