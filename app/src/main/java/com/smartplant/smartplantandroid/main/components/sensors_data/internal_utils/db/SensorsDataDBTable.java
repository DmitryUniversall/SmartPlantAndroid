package com.smartplant.smartplantandroid.main.components.sensors_data.internal_utils.db;

import android.database.sqlite.SQLiteDatabase;

import com.smartplant.smartplantandroid.core.data.db.DBTable;

public class SensorsDataDBTable implements DBTable {
    public static final String TABLE_NAME = "sensors_data";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DEVICE_ID = "device_id";
    public static final String COLUMN_ILLUMINATION = "illumination";
    public static final String COLUMN_SOIL_MOISTURE = "soil_moisture";
    public static final String COLUMN_TEMPERATURE = "temperature";
    public static final String COLUMN_HUMIDITY = "humidity";
    public static final String COLUMN_WATER_LEVEL = "water_level";
    public static final String COLUMN_CREATED_AT = "created_at";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DEVICE_ID + "INTEGER, " +
                    COLUMN_ILLUMINATION + " INTEGER, " +
                    COLUMN_SOIL_MOISTURE + " INTEGER, " +
                    COLUMN_TEMPERATURE + " REAL, " +
                    COLUMN_HUMIDITY + " REAL, " +
                    COLUMN_WATER_LEVEL + " INTEGER, " +
                    COLUMN_CREATED_AT + " DATETIME);";

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

    }
}
