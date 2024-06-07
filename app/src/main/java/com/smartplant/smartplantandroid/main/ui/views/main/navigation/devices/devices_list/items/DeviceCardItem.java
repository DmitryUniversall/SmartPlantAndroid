package com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.devices_list.items;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.smartplant.smartplantandroid.R;

public class DeviceCardItem extends LinearLayout {
    private TextView titleTextView;
    private TextView descriptionTextView;
    private ImageView iconImageView;
    private ProgressBar progressBar;
    private TextView statusTextView;
    private View statusIndicatorView;

    public DeviceCardItem(Context context) {
        this(context, null);
    }

    public DeviceCardItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DeviceCardItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.main_item_device_card, this, true);

        titleTextView = findViewById(R.id.title);
        descriptionTextView = findViewById(R.id.description);
        iconImageView = findViewById(R.id.icon);
        progressBar = findViewById(R.id.progress_bar);
        statusTextView = findViewById(R.id.status);
        statusIndicatorView = findViewById(R.id.status_indicator);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DeviceCardItem, 0, 0);
            String title = typedArray.getString(R.styleable.DeviceCardItem_title);
            String description = typedArray.getString(R.styleable.DeviceCardItem_description);
            int progress = typedArray.getInt(R.styleable.DeviceCardItem_progress, 0);
            boolean status = typedArray.getBoolean(R.styleable.DeviceCardItem_status, false);
            int iconResId = typedArray.getResourceId(R.styleable.DeviceCardItem_icon, R.drawable.icon_house);

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
            statusTextView.setText(R.string.on);
            statusTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.device_on));
            statusIndicatorView.setBackgroundResource(R.drawable.icon_device_active);
        } else {
            statusTextView.setText(R.string.off);
            statusTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.device_off));
            statusIndicatorView.setBackgroundResource(R.drawable.icon_device_inactive);
        }
    }
}