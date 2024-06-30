package com.smartplant.smartplantandroid.core.ui.edit_dialog;

import androidx.annotation.Nullable;

public interface DialogValueValidator {
    @Nullable
    String validate(String newValue);
}
