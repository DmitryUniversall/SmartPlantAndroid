package com.smartplant.smartplantandroid.main.components.notifiactions.internal_utils.db;

import android.database.sqlite.SQLiteDatabase;

import com.smartplant.smartplantandroid.core.data.db.DBTable;
import com.smartplant.smartplantandroid.core.logs.AppLogger;

public class NotificationsDBTable implements DBTable {
    public static final String TABLE_NAME = "notifications";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DEVICE_ID = "device_id";
    public static final String COLUMN_NOTIFICATION_TYPE = "notification_type";
    public static final String COLUMN_IS_CHECKED = "is_checked";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";

    // Deprecated
    public static final String DEPRECATED_COLUMN_IMAGE_RES_ID = "image_res_id";
    public static final String DEPRECATED_COLUMN_ACTIONS = "actions";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DEVICE_ID + " INTEGER, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_IS_CHECKED + " INTEGER DEFAULT 0, " +
                    COLUMN_NOTIFICATION_TYPE + " INTEGER);";

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
        if (oldVersion < 9) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NOTIFICATION_TYPE + " INTEGER DEFAULT 1;");
            db.execSQL("ALTER TABLE " + TABLE_NAME + " DROP COLUMN " + DEPRECATED_COLUMN_IMAGE_RES_ID + ";");
            db.execSQL("ALTER TABLE " + TABLE_NAME + " DROP COLUMN " + DEPRECATED_COLUMN_ACTIONS + ";");
        }

        if (oldVersion < 10) {  // Re-create to delete NOT NULL from COLUMN_TITLE
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
            db.execSQL(getCreateScript());
        }
    }
}
