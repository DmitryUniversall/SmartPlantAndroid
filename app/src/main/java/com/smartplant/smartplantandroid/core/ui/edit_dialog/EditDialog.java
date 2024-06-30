package com.smartplant.smartplantandroid.core.ui.edit_dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.ReturnThis;
import androidx.appcompat.app.AlertDialog;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.core.callbacks.ResultProcessor;

// TODO: Multi validators
// TODO: Auto process to required type
public class EditDialog {
    private final @NonNull Context _context;
    private final @NonNull String _title;
    private final @NonNull String _currentValue;
    private final int _inputType;

    private @Nullable DialogValueValidator _validator;
    private @Nullable ResultProcessor<String> _processor;

    public EditDialog(@NonNull Context context, @NonNull String title, @NonNull String currentValue, int inputType) {
        this._context = context;
        this._title = title;
        this._currentValue = currentValue;
        this._inputType = inputType;
    }

    @SuppressLint("InflateParams")
    protected View createDialogView() {
        return LayoutInflater.from(this._context).inflate(R.layout.dialog_edit, null);
    }

    protected void onPositiveButtonClick(@NonNull EditText input, @NonNull DialogInterface dialog, int which) {
        String newValue = input.getText().toString();

        if (this._validator != null) {
            String errorMessage = this._validator.validate(newValue);
            if (errorMessage != null) {
                Toast.makeText(this._context, errorMessage, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this._context, this._context.getString(R.string.saved_successfully), Toast.LENGTH_SHORT).show();
            }
        }

        if (this._processor != null) this._processor.process(newValue);
    }

    protected void onNegativeButtonClick(@NonNull DialogInterface dialog, int which) {
        dialog.cancel();
    }

    @ReturnThis
    @CanIgnoreReturnValue
    public EditDialog show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this._context);
        builder.setTitle(this._title);  // TODO: Title to xml

        View root = createDialogView();
        builder.setView(root);

        EditText input = root.findViewById(R.id.dialog_edit_text);
        input.setInputType(this._inputType);
        input.setText(this._currentValue);

        builder.setPositiveButton(this._context.getString(R.string.dialog_ok), ((dialog, which) -> this.onPositiveButtonClick(input, dialog, which)));
        builder.setNegativeButton(this._context.getString(R.string.dialog_cancel), this::onNegativeButtonClick);
        builder.show();
        return this;
    }

    @ReturnThis
    @CanIgnoreReturnValue
    public EditDialog setValidator(@Nullable DialogValueValidator _validator) {
        this._validator = _validator;
        return this;
    }

    @ReturnThis
    @CanIgnoreReturnValue
    public EditDialog setProcessor(@Nullable ResultProcessor<String> _processor) {
        this._processor = _processor;
        return this;
    }
}
