package com.smartplant.smartplantandroid.main.components.sensors_data.internal_utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SensorsDataDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AppData.db";
    private static final int DATABASE_VERSION = 2;

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

    public SensorsDataDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_DEVICE_ID + " INTEGER");
        }
    }
}
