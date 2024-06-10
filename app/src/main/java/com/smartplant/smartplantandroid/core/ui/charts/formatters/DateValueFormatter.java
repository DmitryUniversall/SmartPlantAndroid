package com.smartplant.smartplantandroid.core.ui.charts.formatters;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateValueFormatter extends ValueFormatter {
    private final SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    @Override
    public String getFormattedValue(float value) {
        return mFormat.format(new Date((long) value));
    }
}