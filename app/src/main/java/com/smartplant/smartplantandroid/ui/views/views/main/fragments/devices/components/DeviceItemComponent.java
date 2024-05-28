package com.smartplant.smartplantandroid.ui.views.views.main.fragments.devices.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.smartplant.smartplantandroid.R;

public class DeviceItemComponent extends LinearLayout {
    private TextView titleTextView;
    private TextView descriptionTextView;
    private ImageView iconImageView;
    private ProgressBar progressBar;
    private TextView statusTextView;
    private View statusIndicatorView;

    public DeviceItemComponent(Context context) {
        this(context, null);
    }

    public DeviceItemComponent(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DeviceItemComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.main_component_device_item, this, true);

        titleTextView = findViewById(R.id.title);
        descriptionTextView = findViewById(R.id.description);
        iconImageView = findViewById(R.id.icon);
        progressBar = findViewById(R.id.progress_bar);
        statusTextView = findViewById(R.id.status);
        statusIndicatorView = findViewById(R.id.status_indicator);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DeviceItemComponent, 0, 0);
            String title = typedArray.getString(R.styleable.DeviceItemComponent_title);
            String description = typedArray.getString(R.styleable.DeviceItemComponent_description);
            int progress = typedArray.getInt(R.styleable.DeviceItemComponent_progress, 0);
            boolean status = typedArray.getBoolean(R.styleable.DeviceItemComponent_status, false);
            int iconResId = typedArray.getResourceId(R.styleable.DeviceItemComponent_icon, R.drawable.icon_house);

            setTitle(title);
            setDescription(description);
            setProgress(progress);
            setStatus(status);
            setIcon(iconResId);

            typedArray.recycle();
        }
    }

    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    public void setDescription(String description) {
        descriptionTextView.setText(description);
    }

    public void setIcon(int resId) {
        iconImageView.setImageResource(resId);
    }

    public void setProgress(int progress) {
        progressBar.setProgress(progress);
    }

    public void setStatus(boolean status) {
        if (status) {
            statusTextView.setText(R.string.DeviceON);
            statusTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.primary));
            statusIndicatorView.setBackgroundResource(R.drawable.icon_device_active);
        } else {
            statusTextView.setText(R.string.DeviceOFF);
            statusTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.device_off));
            statusIndicatorView.setBackgroundResource(R.drawable.icon_device_inactive);
        }
    }
}
