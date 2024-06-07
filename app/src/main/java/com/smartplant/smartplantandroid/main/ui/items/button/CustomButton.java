package com.smartplant.smartplantandroid.main.ui.items.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.smartplant.smartplantandroid.R;


public class CustomButton extends AppCompatButton {
    private boolean _isOutline = false;

    public CustomButton(Context context) {
        super(context);
        _init(context, null);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        _init(context, attrs);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        _init(context, attrs);
    }

    private void _init(Context context, AttributeSet attrs) {
        if (attrs == null) return;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomButton);
        _isOutline = a.getBoolean(R.styleable.CustomButton_outline, false);
        a.recycle();

        _updateButtonBackground();
    }

    private Drawable _getBackgroundDrawable(Context context, @DrawableRes int resId) {
        return ContextCompat.getDrawable(context, resId);
    }

    private void _updateButtonBackground() {
        if (_isOutline) {
            setBackground(_getBackgroundDrawable(getContext(), R.drawable.btn_outline_background));
            setTextColor(ContextCompat.getColor(getContext(), R.color.L1_primary));
        } else {
            setBackground(_getBackgroundDrawable(getContext(), R.drawable.btn_background));
            setTextColor(ContextCompat.getColor(getContext(), R.color.L1_accent));
        }
    }

    public void setOutline(boolean outline) {
        _isOutline = outline;
        _updateButtonBackground();
    }
}
