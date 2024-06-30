package com.smartplant.smartplantandroid.main.components.cultivation_rules.internal_utils.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import com.smartplant.smartplantandroid.core.async.background_task.BackgroundTask;
import com.smartplant.smartplantandroid.core.data.db.CoreSqliteDBHelper;
import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.main.components.cultivation_rules.models.CultivationRules;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CultivationRulesDBService {
    private final @NonNull SQLiteDatabase _db;

    public CultivationRulesDBService() {
        this._db = CoreSqliteDBHelper.getInstance().getWritableDatabase();
    }

    @SuppressLint("Range")
    private CultivationRules cursorToCultivationRules(Cursor cursor) {
        int deviceId = cursor.getInt(cursor.getColumnIndex(CultivationRulesDBTable.COLUMN_DEVICE_ID));
        int minTemperature = cursor.getInt(cursor.getColumnIndex(CultivationRulesDBTable.COLUMN_MIN_TEMPERATURE));
        int maxTemperature = cursor.getInt(cursor.getColumnIndex(CultivationRulesDBTable.COLUMN_MAX_TEMPERATURE));
        int minHumidityPercent = cursor.getInt(cursor.getColumnIndex(CultivationRulesDBTable.COLUMN_MIN_HUMIDITY_PERCENT));
        int maxHumidityPercent = cursor.getInt(cursor.getColumnIndex(CultivationRulesDBTable.COLUMN_MAX_HUMIDITY_PERCENT));
        int minSoilMoisturePercent = cursor.getInt(cursor.getColumnIndex(CultivationRulesDBTable.COLUMN_MIN_SOIL_MOISTURE_PERCENT));
        int maxSoilMoisturePercent = cursor.getInt(cursor.getColumnIndex(CultivationRulesDBTable.COLUMN_MAX_SOIL_MOISTURE_PERCENT));
        int minIlluminationPercent = cursor.getInt(cursor.getColumnIndex(CultivationRulesDBTable.COLUMN_MIN_ILLUMINATION_PERCENT));
        int maxIlluminationPercent = cursor.getInt(cursor.getColumnIndex(CultivationRulesDBTable.COLUMN_MAX_ILLUMINATION_PERCENT));

        CultivationRules rules = new CultivationRules(deviceId);
        rules.setMinTemperature(minTemperature);
        rules.setMaxTemperature(maxTemperature);
        rules.setMinHumidityPercent(minHumidityPercent);
        rules.setMaxHumidityPercent(maxHumidityPercent);
        rules.setMinSoilMoisturePercent(minSoilMoisturePercent);
        rules.setMaxSoilMoisturePercent(maxSoilMoisturePercent);
        rules.setMinIlluminationPercent(minIlluminationPercent);
        rules.setMaxIlluminationPercent(maxIlluminationPercent);

        return rules;
    }

    private ContentValues _createContentValuesFromRules(@NonNull CultivationRules cultivationRules) {
        ContentValues values = new ContentValues();
        values.put(CultivationRulesDBTable.COLUMN_DEVICE_ID, cultivationRules.getDeviceId());
        values.put(CultivationRulesDBTable.COLUMN_MIN_TEMPERATURE, cultivationRules.getMinTemperature());
        values.put(CultivationRulesDBTable.COLUMN_MAX_TEMPERATURE, cultivationRules.getMaxTemperature());
        values.put(CultivationRulesDBTable.COLUMN_MIN_HUMIDITY_PERCENT, cultivationRules.getMinHumidityPercent());
        values.put(CultivationRulesDBTable.COLUMN_MAX_HUMIDITY_PERCENT, cultivationRules.getMaxHumidityPercent());
        values.put(CultivationRulesDBTable.COLUMN_MIN_SOIL_MOISTURE_PERCENT, cultivationRules.getMinSoilMoisturePercent());
        values.put(CultivationRulesDBTable.COLUMN_MAX_SOIL_MOISTURE_PERCENT, cultivationRules.getMaxSoilMoisturePercent());
        values.put(CultivationRulesDBTable.COLUMN_MIN_ILLUMINATION_PERCENT, cultivationRules.getMinIlluminationPercent());
        values.put(CultivationRulesDBTable.COLUMN_MAX_ILLUMINATION_PERCENT, cultivationRules.getMaxIlluminationPercent());
        return values;
    }

    public BackgroundTask<Void> insertCultivationRules(@NonNull CultivationRules newRules) {
        return new BackgroundTask<Void>(() -> {
            ContentValues values = _createContentValuesFromRules(newRules);
            _db.insertOrThrow(CultivationRulesDBTable.TABLE_NAME, null, values);
            return null;
        }).onSuccess(result -> AppLogger.debug("Successfully inserted cultivation rules to db"));
    }

    public BackgroundTask<Optional<CultivationRules>> getCultivationRules(int deviceId) {
        return new BackgroundTask<>(() -> {
            String query = "SELECT * FROM " + CultivationRulesDBTable.TABLE_NAME + " WHERE " + CultivationRulesDBTable.COLUMN_DEVICE_ID + " = ? LIMIT 1";

            Cursor cursor = _db.rawQuery(query, new String[]{String.valueOf(deviceId)});
            if (cursor == null || !cursor.moveToFirst()) return Optional.empty();

            CultivationRules rules = cursorToCultivationRules(cursor);
            cursor.close();
            return Optional.of(rules);
        });
    }

    public BackgroundTask<List<CultivationRules>> getAllCultivationRules() {
        return new BackgroundTask<>(() -> {
            List<CultivationRules> rulesList = new ArrayList<>();
            String query = "SELECT * FROM " + CultivationRulesDBTable.TABLE_NAME;

            Cursor cursor = _db.rawQuery(query, null);
            if (cursor == null) return rulesList;

            while (cursor.moveToNext()) rulesList.add(cursorToCultivationRules(cursor));
            cursor.close();

            return rulesList;
        });
    }

    public BackgroundTask<Void> updateCultivationRules(@NonNull CultivationRules newRules) {
        return new BackgroundTask<Void>(() -> {
            ContentValues values = _createContentValuesFromRules(newRules);
            values.remove(CultivationRulesDBTable.COLUMN_DEVICE_ID);
            _db.update(CultivationRulesDBTable.TABLE_NAME, values, CultivationRulesDBTable.COLUMN_DEVICE_ID + " = ?", new String[]{String.valueOf(newRules.getDeviceId())});
            return null;
        }).onSuccess(result -> AppLogger.debug("Successfully updated cultivation rules in db"));
    }
}
