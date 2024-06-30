package com.smartplant.smartplantandroid.main.components.cultivation_rules.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.smartplant.smartplantandroid.core.async.background_task.BackgroundTask;
import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.main.components.cultivation_rules.internal_utils.db.CultivationRulesDBService;
import com.smartplant.smartplantandroid.main.components.cultivation_rules.models.CultivationRules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CultivationRulesRepositoryST {
    // Singleton
    private static @Nullable CultivationRulesRepositoryST _instance;

    // Services
    private final CultivationRulesDBService _dbService;

    // Cache
    private final @NonNull Map<Integer, CultivationRules> _cultivationRules = new HashMap<>();
    private boolean _isLoaded;

    public static synchronized void createInstance() {
        if (_instance != null)
            throw new RuntimeException("SensorsDataRepositoryST has already been initialized");
        _instance = new CultivationRulesRepositoryST();
    }

    public static synchronized CultivationRulesRepositoryST getInstance() {
        if (_instance == null)
            throw new RuntimeException("SensorsDataRepositoryST has not been initialized");
        return _instance;
    }

    private CultivationRulesRepositoryST() {
        _dbService = new CultivationRulesDBService();
        this.fetchAllCultivationRules().execute();  // TODO: Should it be here?
    }

    public BackgroundTask<Void> createCultivationRules(@NonNull CultivationRules cultivationRules) {
        return this._dbService.insertCultivationRules(cultivationRules)
                .onSuccess(result -> this._cultivationRules.put(cultivationRules.getDeviceId(), cultivationRules));
    }

    public BackgroundTask<Void> updateCultivationRules(@NonNull CultivationRules cultivationRules) {
        return this._dbService.updateCultivationRules(cultivationRules)
                .onSuccess(result -> this._cultivationRules.put(cultivationRules.getDeviceId(), cultivationRules));  // Just to be sure that data is actual
    }

    public BackgroundTask<Optional<CultivationRules>> fetchCultivationRules(int deviceId) {
        return this._dbService.getCultivationRules(deviceId)
                .onSuccess(result -> result.ifPresent(cultivationRules -> this._cultivationRules.put(deviceId, cultivationRules)));
    }

    public BackgroundTask<List<CultivationRules>> fetchAllCultivationRules() {
        return this._dbService.getAllCultivationRules()
                .onSuccess(result -> result.forEach(element -> this._cultivationRules.put(element.getDeviceId(), element)))
                .onSuccess(result -> _isLoaded = true);
    }

    public CultivationRules getCultivationRules(int deviceId) {
        if (!this._isLoaded)
            throw new IllegalStateException("Unable to get CultivationRules: Not loaded");

        return this._cultivationRules.get(deviceId);
    }

    public BackgroundTask<CultivationRules> getOrCreateCultivationRules(int deviceId) {
        if (!this._isLoaded)
            throw new IllegalStateException("Unable to get CultivationRules: Not loaded");

        return new BackgroundTask<>(() -> {
            if (this._cultivationRules.containsKey(deviceId)) {
                return this._cultivationRules.get(deviceId);
            }

            AppLogger.debug("Creating CultivationRules for device id=%d", deviceId);
            CultivationRules rules = new CultivationRules(deviceId);
            createCultivationRules(rules).execute();
            return rules;
        });
    }
}
