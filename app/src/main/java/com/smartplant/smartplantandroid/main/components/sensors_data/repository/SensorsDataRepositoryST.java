package com.smartplant.smartplantandroid.main.components.sensors_data.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.smartplant.smartplantandroid.core.async.background_task.BackgroundTask;
import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.main.components.sensors_data.internal_utils.db.SensorsDataDBService;
import com.smartplant.smartplantandroid.main.components.sensors_data.models.SensorsData;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class SensorsDataRepositoryST {
    // Singleton
    private static @Nullable SensorsDataRepositoryST _instance;

    // Services
    private final SensorsDataDBService _dbService;

    // Cache
    private final Map<Integer, SensorsData> _cachedSensorsData = new HashMap<>();
    private final Map<Integer, Long> _cacheTimeMap = new HashMap<>();
    private static final long CACHE_TTL = TimeUnit.HOURS.toMillis(1);  // TODO: Is it actually needed?

    public static synchronized void createInstance(Context context) {
        if (_instance != null)
            throw new RuntimeException("SensorsDataRepositoryST has already been initialized");
        _instance = new SensorsDataRepositoryST(context);
    }

    public static synchronized SensorsDataRepositoryST getInstance() {
        if (_instance == null)
            throw new RuntimeException("SensorsDataRepositoryST has not been initialized");
        return _instance;
    }

    private SensorsDataRepositoryST(Context context) {
        _dbService = new SensorsDataDBService(context);
    }

    private void _cacheSensorsData(int deviceId, @NonNull SensorsData sensorsData) {
        AppLogger.debug("Caching SensorsData for device %d", deviceId);
        _cachedSensorsData.put(deviceId, sensorsData);
        _cacheTimeMap.put(deviceId, System.currentTimeMillis());
    }

    private void _invalidateCachedSensorsData(int deviceId) {
        AppLogger.debug("Invalidating cache for device %d", deviceId);
        _cachedSensorsData.remove(deviceId);
        _cacheTimeMap.remove(deviceId);
    }

    public void clearCache() {
        AppLogger.debug("Clearing SensorsData cache");
        this._cachedSensorsData.clear();
        this._cacheTimeMap.clear();
    }

    @Nullable
    public BackgroundTask<Optional<SensorsData>> fetchLatestSensorsData(int deviceId) {
        this._invalidateCachedSensorsData(deviceId);
        return _dbService.getLatestSensorsData(deviceId).onSuccess(result -> {
            if (!result.isPresent()) return;
            this._cacheSensorsData(deviceId, result.get());
        });
    }

    @Nullable
    public SensorsData getCachedSensorsData(int deviceId) {
        long currentTime = System.currentTimeMillis();
        Long lastCacheTime = _cacheTimeMap.get(deviceId);

        if (lastCacheTime == null || (currentTime - lastCacheTime) > CACHE_TTL) {
            this._invalidateCachedSensorsData(deviceId);
            return null;
        }

        AppLogger.debug("Found cached SensorsData for device %d", deviceId);
        return _cachedSensorsData.get(deviceId);
    }

    public BackgroundTask<List<SensorsData>> getSensorsDataByDate(int deviceId, Date startDate, Date endDate) {
        return _dbService.getSensorsDataByDate(deviceId, startDate, endDate);
    }

    public BackgroundTask<Void> insertSensorsData(int deviceId, SensorsData sensorsData) {
        this._invalidateCachedSensorsData(deviceId);
        return _dbService.insertSensorsData(deviceId, sensorsData).onSuccess(result -> this._cacheSensorsData(deviceId, sensorsData));
    }

    public BackgroundTask<Void> updateSensorsData(long id, int deviceId, SensorsData sensorsData) {
        this._invalidateCachedSensorsData(deviceId);
        return _dbService.updateSensorsData(id, sensorsData).onSuccess(result -> this._cacheSensorsData(deviceId, sensorsData));
    }

    public BackgroundTask<Void> deleteSensorsData(long id, int deviceId) {
        this._invalidateCachedSensorsData(deviceId);
        return _dbService.deleteSensorsData(id);
    }

    public void close() {
        _dbService.close();
    }
}
