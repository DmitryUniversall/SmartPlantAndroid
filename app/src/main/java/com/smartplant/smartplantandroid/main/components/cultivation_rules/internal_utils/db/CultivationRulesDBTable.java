package com.smartplant.smartplantandroid.main.components.cultivation_rules.internal_utils.db;

import android.database.sqlite.SQLiteDatabase;

import com.smartplant.smartplantandroid.core.data.db.DBTable;

public class CultivationRulesDBTable implements DBTable {
    public static final String TABLE_NAME = "cultivation_rules";

    public static final String COLUMN_DEVICE_ID = "device_id";
    public static final String COLUMN_MIN_TEMPERATURE = "min_temperature";
    public static final String COLUMN_MAX_TEMPERATURE = "max_temperature";
    public static final String COLUMN_MIN_HUMIDITY_PERCENT = "min_humidity_percent";
    public static final String COLUMN_MAX_HUMIDITY_PERCENT = "max_humidity_percent";
    public static final String COLUMN_MIN_SOIL_MOISTURE_PERCENT = "min_soil_moisture_percent";
    public static final String COLUMN_MAX_SOIL_MOISTURE_PERCENT = "max_soil_moisture_percent";
    public static final String COLUMN_MIN_ILLUMINATION_PERCENT = "min_illumination_percent";
    public static final String COLUMN_MAX_ILLUMINATION_PERCENT = "max_illumination_percent";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_DEVICE_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_MIN_TEMPERATURE + " INTEGER, " +
                    COLUMN_MAX_TEMPERATURE + " INTEGER, " +
                    COLUMN_MIN_HUMIDITY_PERCENT + " INTEGER, " +
                    COLUMN_MAX_HUMIDITY_PERCENT + " INTEGER, " +
                    COLUMN_MIN_SOIL_MOISTURE_PERCENT + " INTEGER, " +
                    COLUMN_MAX_SOIL_MOISTURE_PERCENT + " INTEGER, " +
                    COLUMN_MIN_ILLUMINATION_PERCENT + " INTEGER, " +
                    COLUMN_MAX_ILLUMINATION_PERCENT + " INTEGER);";

    @Override
    public String getCreateScript() {
        return TABLE_CREATE;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 14) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            db.execSQL(TABLE_CREATE);
        }
    }
}
