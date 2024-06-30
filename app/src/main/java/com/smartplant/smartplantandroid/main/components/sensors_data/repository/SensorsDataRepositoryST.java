package com.smartplant.smartplantandroid.main.components.sensors_data.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.smartplant.smartplantandroid.core.async.background_task.BackgroundTask;
import com.smartplant.smartplantandroid.core.data.json.JsonUtils;
import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.main.components.sensors_data.internal_utils.db.SensorsDataDBService;
import com.smartplant.smartplantandroid.main.components.sensors_data.models.SensorsData;
import com.smartplant.smartplantandroid.main.components.storage.models.StorageAction;
import com.smartplant.smartplantandroid.main.components.storage.models.StorageDataMessage;
import com.smartplant.smartplantandroid.main.components.storage.repository.StorageRepositoryST;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class SensorsDataRepositoryST {
    // Singleton
    private static @Nullable SensorsDataRepositoryST _instance;

    // Services
    private final SensorsDataDBService _dbService;

    // Reusable handlers
    protected final @NonNull ConcurrentHashMap<String, NewSensorsDataHandler> _newDataHandlers = new ConcurrentHashMap<>();

    // Cache
    private final Map<Integer, SensorsData> _cachedSensorsData = new HashMap<>();
    private final Map<Integer, Long> _cacheTimeMap = new HashMap<>();
    private static final long CACHE_TTL = TimeUnit.MINUTES.toMillis(20);

    // Utils
    private final StorageRepositoryST _storageRepository;

    public static synchronized void createInstance() {
        if (_instance != null)
            throw new RuntimeException("SensorsDataRepositoryST has already been initialized");
        _instance = new SensorsDataRepositoryST();
    }

    public static synchronized SensorsDataRepositoryST getInstance() {
        if (_instance == null)
            throw new RuntimeException("SensorsDataRepositoryST has not been initialized");
        return _instance;
    }

    private SensorsDataRepositoryST() {
        _dbService = new SensorsDataDBService();
        _storageRepository = StorageRepositoryST.getInstance();
        _newDataActionRegister();
    }

    private void _newDataActionRegister() {
        _storageRepository.onProcessorCreate("SensorsDataRepositoryST", () ->
                _storageRepository.getProcessor().onAction(  // Will be called each time app connects to storage WS
                        StorageAction.DeviceActionType.SENSORS_DATA_UPDATE.getValue(),
                        "SensorsDataRepository",
                        this::_sensorsDataActionHandler
                ));
    }

    private void _callNewDataHandlers(@NonNull SensorsData sensorsData) {
        if (_newDataHandlers.isEmpty()) return;
        _newDataHandlers.values().forEach((handler) -> handler.onNewSensorsData(sensorsData));
    }

    private void _sensorsDataActionHandler(@NonNull StorageDataMessage dataMessage, @NonNull StorageAction storageAction) {
        JsonObject data = storageAction.getData();
        assert data != null;
        SensorsData sensorsData = JsonUtils.fromJsonWithNulls(data, SensorsData.class);
        sensorsData.setDeviceId(dataMessage.getSenderId());
        sensorsData.setCreatedAt(dataMessage.getCreatedAt());  // TODO: Refactor it

        this.insertSensorsData(dataMessage.getSenderId(), sensorsData).execute();
    }

    private void _cacheSensorsData(int deviceId, @NonNull SensorsData sensorsData) {
        AppLogger.debug("Caching SensorsData for device id=%d", deviceId);
        _cachedSensorsData.put(deviceId, sensorsData);
        _cacheTimeMap.put(deviceId, System.currentTimeMillis());
    }

    private void _invalidateCachedSensorsData(int deviceId) {
        AppLogger.debug("Invalidating cache for device id=%d", deviceId);
        _cachedSensorsData.remove(deviceId);
        _cacheTimeMap.remove(deviceId);
    }

    public void clearCache() {
        AppLogger.debug("Clearing SensorsData cache");
        this._cachedSensorsData.clear();
        this._cacheTimeMap.clear();
    }

    public void onNewSensorsData(@NonNull String name, @NonNull NewSensorsDataHandler handler) {
        this._newDataHandlers.put(name, handler);
    }

    public void removeNewSensorsDataHandler(@NonNull String name) {
        this._newDataHandlers.remove(name);
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
        return _dbService.insertSensorsData(deviceId, sensorsData)
                .onSuccess(result -> {
                    AppLogger.info("SensorsData for device %d successfully saved", deviceId);
                    this._cacheSensorsData(deviceId, sensorsData);
                    this._callNewDataHandlers(sensorsData);
                })
                .onFailure(error -> AppLogger.error(error, "Failed to save sensors data for %d", deviceId));
    }

    public BackgroundTask<Void> updateSensorsData(long id, int deviceId, SensorsData sensorsData) {
        this._invalidateCachedSensorsData(deviceId);
        return _dbService.updateSensorsData(id, sensorsData).onSuccess(result -> this._cacheSensorsData(deviceId, sensorsData));
    }

    public BackgroundTask<Void> deleteSensorsData(long id, int deviceId) {
        this._invalidateCachedSensorsData(deviceId);
        return _dbService.deleteSensorsData(id);
    }
}
