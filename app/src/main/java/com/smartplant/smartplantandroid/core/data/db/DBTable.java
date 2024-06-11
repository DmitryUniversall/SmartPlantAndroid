package com.smartplant.smartplantandroid.core.data.db;

import android.database.sqlite.SQLiteDatabase;

public interface DBTable {
    String getCreateScript();

    String getTableName();

    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
}