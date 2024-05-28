package com.smartplant.smartplantandroid.utils.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.smartplant.smartplantandroid.R;


public class CustomButton extends AppCompatButton {
    private boolean _isOutline;

    public CustomButton(Context context) {
        super(context);
        init(context, null);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs == null) return;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomButton);
        _isOutline = a.getBoolean(R.styleable.CustomButton_outline, false);
        a.recycle();

        updateButtonBackground();
    }

    public void setOutline(boolean outline) {
        _isOutline = outline;
        updateButtonBackground();
    }

    private void updateButtonBackground() {
        if (_isOutline) {
            Drawable background = getBackgroundDrawable(getContext(), R.drawable.btn_outline_background);
            setBackground(background);
            setTextColor(ContextCompat.getColor(getContext(), R.color.primary));
        } else {
            Drawable background = getBackgroundDrawable(getContext(), R.drawable.btn_background);
            setBackground(background);
            setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
        }
    }

    private Drawable getBackgroundDrawable(Context context, int resId) {
        return ContextCompat.getDrawable(context, resId);
    }
}
