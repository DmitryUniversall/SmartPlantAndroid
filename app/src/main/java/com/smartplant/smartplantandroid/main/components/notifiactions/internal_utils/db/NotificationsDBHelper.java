package com.smartplant.smartplantandroid.main.components.notifiactions.internal_utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NotificationsDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AppData.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "notifications";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DEVICE_ID = "device_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IMAGE_RES_ID = "image_res_id";
    public static final String COLUMN_IS_CHECKED = "is_checked";
    public static final String COLUMN_ACTIONS = "actions";
    public static final String ACTIONS_DELIMITER = ",";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DEVICE_ID + "INTEGER, " +
                    COLUMN_TITLE + " TEXT NOT NULL, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_IMAGE_RES_ID + " INTEGER, " +
                    COLUMN_IS_CHECKED + " INTEGER DEFAULT 0," +
                    COLUMN_ACTIONS + " TEXT);";

    public NotificationsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
