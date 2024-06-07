package com.smartplant.smartplantandroid.main.components.sensors_data.internal_utils.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.core.state.settings.ProjectSettings;
import com.smartplant.smartplantandroid.main.components.sensors_data.models.SensorsData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SensorsDataDBManagerST {
    // Singleton
    private static @Nullable SensorsDataDBManagerST _instance;

    private final SQLiteDatabase _db;
    private final SensorsDataDbHelper _dbHelper;

    public static synchronized void createInstance(Context context) {
        if (_instance != null)
            throw new RuntimeException("SensorsDataDBManagerST has already been initialized");
        _instance = new SensorsDataDBManagerST(context);
    }

    public static synchronized SensorsDataDBManagerST getInstance() {
        if (_instance == null)
            throw new RuntimeException("SensorsDataDBManagerST has not been initialized");
        return _instance;
    }

    private SensorsDataDBManagerST(Context context) {
        _dbHelper = new SensorsDataDbHelper(context);
        _db = _dbHelper.getWritableDatabase();
    }

    public long insertSensorsData(SensorsData sensorsData) {
        ContentValues values = new ContentValues();
        values.put(SensorsDataDbHelper.COLUMN_ILLUMINATION, sensorsData.getIllumination());
        values.put(SensorsDataDbHelper.COLUMN_SOIL_MOISTURE, sensorsData.getSoilMoisture());
        values.put(SensorsDataDbHelper.COLUMN_TEMPERATURE, sensorsData.getTemperature());
        values.put(SensorsDataDbHelper.COLUMN_HUMIDITY, sensorsData.getHumidity());
        values.put(SensorsDataDbHelper.COLUMN_WATER_LEVEL, sensorsData.getWaterLevel());
        values.put(SensorsDataDbHelper.COLUMN_CREATED_AT, new SimpleDateFormat(ProjectSettings.DATE_FORMAT, Locale.getDefault()).format(sensorsData.getCreatedAt()));
        return _db.insert(SensorsDataDbHelper.TABLE_NAME, null, values);
    }

    @SuppressLint("Range")
    public List<SensorsData> getAllSensorsData() {
        List<SensorsData> sensorsDataList = new ArrayList<>();
        Cursor cursor = _db.query(SensorsDataDbHelper.TABLE_NAME, null, null, null, null, null, null);

        if (!cursor.moveToFirst()) {
            cursor.close();
            return sensorsDataList;
        }

        do {
            int id = cursor.getInt(cursor.getColumnIndex(SensorsDataDbHelper.COLUMN_ID));
            int illumination = cursor.getInt(cursor.getColumnIndex(SensorsDataDbHelper.COLUMN_ILLUMINATION));
            int soilMoisture = cursor.getInt(cursor.getColumnIndex(SensorsDataDbHelper.COLUMN_SOIL_MOISTURE));
            double temperature = cursor.getDouble(cursor.getColumnIndex(SensorsDataDbHelper.COLUMN_TEMPERATURE));
            double humidity = cursor.getDouble(cursor.getColumnIndex(SensorsDataDbHelper.COLUMN_HUMIDITY));
            int waterLevel = cursor.getInt(cursor.getColumnIndex(SensorsDataDbHelper.COLUMN_WATER_LEVEL));
            String createdAtStr = cursor.getString(cursor.getColumnIndex(SensorsDataDbHelper.COLUMN_CREATED_AT));

            Date createdAt;
            try {
                createdAt = new SimpleDateFormat(ProjectSettings.DATE_FORMAT, Locale.getDefault()).parse(createdAtStr);
            } catch (Exception e) {
                AppLogger.error("Unknown error during parsing SensorsData createAt field", e);
                continue;
            }

            sensorsDataList.add(new SensorsData(id, waterLevel, illumination, soilMoisture, temperature, humidity, createdAt));
        } while (cursor.moveToNext());

        cursor.close();
        return sensorsDataList;
    }

    public int updateSensorsData(long id, SensorsData sensorsData) {
        ContentValues values = new ContentValues();
        values.put(SensorsDataDbHelper.COLUMN_ILLUMINATION, sensorsData.getIllumination());
        values.put(SensorsDataDbHelper.COLUMN_SOIL_MOISTURE, sensorsData.getSoilMoisture());
        values.put(SensorsDataDbHelper.COLUMN_TEMPERATURE, sensorsData.getTemperature());
        values.put(SensorsDataDbHelper.COLUMN_HUMIDITY, sensorsData.getHumidity());
        values.put(SensorsDataDbHelper.COLUMN_WATER_LEVEL, sensorsData.getWaterLevel());
        return _db.update(SensorsDataDbHelper.TABLE_NAME, values, SensorsDataDbHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int deleteSensorsData(long id) {
        return _db.delete(SensorsDataDbHelper.TABLE_NAME, SensorsDataDbHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void close() {
        _dbHelper.close();
    }
}
