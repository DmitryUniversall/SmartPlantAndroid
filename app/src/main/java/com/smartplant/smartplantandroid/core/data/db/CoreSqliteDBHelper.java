package com.smartplant.smartplantandroid.core.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import com.smartplant.smartplantandroid.core.logs.AppLogger;

import java.util.ArrayList;
import java.util.List;

public class CoreSqliteDBHelper extends SQLiteOpenHelper {
    // Singleton
    protected static CoreSqliteDBHelper _instance;

    // DB
    private static final String DATABASE_NAME = "AppData.db";
    private static final int DATABASE_VERSION = 14;

    // Tables
    private static final List<DBTable> _tables = new ArrayList<>();

    public static synchronized void createInstance(Context context) {
        if (_instance != null)
            throw new RuntimeException("CoreSqliteDBHelper has already been initialized");
        _instance = new CoreSqliteDBHelper(context);
    }

    public static synchronized CoreSqliteDBHelper getInstance() {
        if (_instance == null)
            throw new RuntimeException("CoreSqliteDBHelper has not been initialized");
        return _instance;
    }

    private CoreSqliteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static void registerTable(@NonNull DBTable table) {
        _tables.add(table);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (DBTable table : _tables) {
            db.execSQL(table.getCreateScript());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        AppLogger.info("Upgrading db. Old version: %d; New version: %d", oldVersion, newVersion);

        for (DBTable table : _tables) {
            table.onUpgrade(db, oldVersion, newVersion);
        }
    }
}
