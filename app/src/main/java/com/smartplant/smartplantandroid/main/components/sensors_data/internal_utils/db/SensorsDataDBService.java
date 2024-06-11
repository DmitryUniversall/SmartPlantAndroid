package com.smartplant.smartplantandroid.main.components.sensors_data.internal_utils.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import com.smartplant.smartplantandroid.core.async.background_task.BackgroundTask;
import com.smartplant.smartplantandroid.core.data.db.CoreSqliteDBHelper;
import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.core.state.settings.ProjectSettings;
import com.smartplant.smartplantandroid.main.components.sensors_data.models.SensorsData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;


public class SensorsDataDBService {  // TODO: Throw exceptions on interaction errors
    private final @NonNull SQLiteDatabase _db;

    public SensorsDataDBService() {
        this._db = CoreSqliteDBHelper.getInstance().getWritableDatabase();
    }

    @SuppressLint("Range")
    private SensorsData cursorToSensorsData(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(SensorsDataDBTable.COLUMN_ID));
        int illumination = cursor.getInt(cursor.getColumnIndex(SensorsDataDBTable.COLUMN_ILLUMINATION));
        int soilMoisture = cursor.getInt(cursor.getColumnIndex(SensorsDataDBTable.COLUMN_SOIL_MOISTURE));
        int waterLevel = cursor.getInt(cursor.getColumnIndex(SensorsDataDBTable.COLUMN_WATER_LEVEL));
        float temperature = cursor.getFloat(cursor.getColumnIndex(SensorsDataDBTable.COLUMN_TEMPERATURE));
        float humidity = cursor.getFloat(cursor.getColumnIndex(SensorsDataDBTable.COLUMN_HUMIDITY));
        String createdAtString = cursor.getString(cursor.getColumnIndex(SensorsDataDBTable.COLUMN_CREATED_AT));
        Date createdAt = null;

        try {
            createdAt = new SimpleDateFormat(ProjectSettings.DATE_FORMAT, Locale.getDefault()).parse(createdAtString);
        } catch (Exception e) {
            AppLogger.error("Unknown error during fetching sensors data", e);
        }

        return new SensorsData(id, waterLevel, illumination, soilMoisture, temperature, humidity, createdAt);
    }

    public BackgroundTask<Void> insertSensorsData(int deviceId, SensorsData sensorsData) {
        return new BackgroundTask<>(() -> {
            ContentValues values = new ContentValues();
            values.put(SensorsDataDBTable.COLUMN_DEVICE_ID, deviceId);
            values.put(SensorsDataDBTable.COLUMN_ILLUMINATION, sensorsData.getIllumination());
            values.put(SensorsDataDBTable.COLUMN_SOIL_MOISTURE, sensorsData.getSoilMoisture());
            values.put(SensorsDataDBTable.COLUMN_TEMPERATURE, sensorsData.getTemperature());
            values.put(SensorsDataDBTable.COLUMN_HUMIDITY, sensorsData.getHumidity());
            values.put(SensorsDataDBTable.COLUMN_WATER_LEVEL, sensorsData.getWaterLevel());
            values.put(SensorsDataDBTable.COLUMN_CREATED_AT, new SimpleDateFormat(ProjectSettings.DATE_FORMAT, Locale.getDefault()).format(sensorsData.getCreatedAt()));
            long newRowId = _db.insert(SensorsDataDBTable.TABLE_NAME, null, values);
            if (newRowId != -1) sensorsData.setId(newRowId);
            return null;
        });
    }

    public BackgroundTask<Void> updateSensorsData(long id, SensorsData sensorsData) {
        return new BackgroundTask<>(() -> {
            ContentValues values = new ContentValues();
            values.put(SensorsDataDBTable.COLUMN_ILLUMINATION, sensorsData.getIllumination());
            values.put(SensorsDataDBTable.COLUMN_SOIL_MOISTURE, sensorsData.getSoilMoisture());
            values.put(SensorsDataDBTable.COLUMN_TEMPERATURE, sensorsData.getTemperature());
            values.put(SensorsDataDBTable.COLUMN_HUMIDITY, sensorsData.getHumidity());
            values.put(SensorsDataDBTable.COLUMN_WATER_LEVEL, sensorsData.getWaterLevel());
            _db.update(SensorsDataDBTable.TABLE_NAME, values, SensorsDataDBTable.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
            return null;
        });
    }

    public BackgroundTask<Void> deleteSensorsData(long id) {
        return new BackgroundTask<>(() -> {
            _db.delete(SensorsDataDBTable.TABLE_NAME, SensorsDataDBTable.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
            return null;
        });
    }

    public BackgroundTask<Optional<SensorsData>> getLatestSensorsData(int deviceId) {
        return new BackgroundTask<>(() -> {
            String query = "SELECT * FROM " + SensorsDataDBTable.TABLE_NAME +
                    " WHERE " + SensorsDataDBTable.COLUMN_DEVICE_ID + " = ?" +
                    " ORDER BY " + SensorsDataDBTable.COLUMN_CREATED_AT + " DESC LIMIT 1";

            Cursor cursor = _db.rawQuery(query, new String[]{String.valueOf(deviceId)});
            if (cursor == null || !cursor.moveToFirst()) return Optional.empty();

            SensorsData sensorsData = cursorToSensorsData(cursor);
            cursor.close();
            return Optional.of(sensorsData);
        });
    }

    public BackgroundTask<List<SensorsData>> getSensorsDataByDate(int deviceId, Date startDate, Date endDate) {
        return new BackgroundTask<>(() -> {
            String query = "SELECT * FROM " + SensorsDataDBTable.TABLE_NAME +
                    " WHERE " + SensorsDataDBTable.COLUMN_DEVICE_ID + " = ?" +
                    " AND " + SensorsDataDBTable.COLUMN_CREATED_AT + " BETWEEN ? AND ?";

            SimpleDateFormat sdf = new SimpleDateFormat(ProjectSettings.DATE_FORMAT, Locale.getDefault());
            String startDateString = sdf.format(startDate);
            String endDateString = sdf.format(endDate);

            List<SensorsData> sensorsDataList = new ArrayList<>();
            Cursor cursor = _db.rawQuery(query, new String[]{String.valueOf(deviceId), startDateString, endDateString});
            if (cursor == null) return sensorsDataList;

            while (cursor.moveToNext()) {
                sensorsDataList.add(cursorToSensorsData(cursor));
            }
            cursor.close();

            return sensorsDataList;
        });
    }
}
