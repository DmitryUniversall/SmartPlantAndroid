package com.smartplant.smartplantandroid.main.components.notifiactions.internal_utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.smartplant.smartplantandroid.core.async.background_task.BackgroundTask;
import com.smartplant.smartplantandroid.core.data.DataUtils;
import com.smartplant.smartplantandroid.main.components.notifiactions.models.AppNotification;

import java.util.ArrayList;
import java.util.List;

public class NotificationsDBService {
    private final SQLiteDatabase _db;
    private final NotificationsDBHelper _dbHelper;

    public NotificationsDBService(Context context) {
        _dbHelper = new NotificationsDBHelper(context);
        _db = _dbHelper.getWritableDatabase();
    }

    private AppNotification _createNotificationFromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(NotificationsDBHelper.COLUMN_ID));
        int deviceId = cursor.getInt(cursor.getColumnIndexOrThrow(NotificationsDBHelper.COLUMN_DEVICE_ID));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(NotificationsDBHelper.COLUMN_TITLE));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(NotificationsDBHelper.COLUMN_DESCRIPTION));
        int imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(NotificationsDBHelper.COLUMN_IMAGE_RES_ID));
        boolean isChecked = cursor.getInt(cursor.getColumnIndexOrThrow(NotificationsDBHelper.COLUMN_IS_CHECKED)) == 1;
        String actions = cursor.getString(cursor.getColumnIndexOrThrow(NotificationsDBHelper.COLUMN_ACTIONS));

        return new AppNotification(id, deviceId, isChecked, title, description, imageResId, DataUtils.convertStringToIntegerList(actions, NotificationsDBHelper.ACTIONS_DELIMITER));
    }

    public BackgroundTask<Void> insertNotification(AppNotification notification) {
        return new BackgroundTask<>(() -> {
            ContentValues values = new ContentValues();
            values.put(NotificationsDBHelper.COLUMN_TITLE, notification.getTitle());
            values.put(NotificationsDBHelper.COLUMN_DESCRIPTION, notification.getDescription());
            values.put(NotificationsDBHelper.COLUMN_IMAGE_RES_ID, notification.getImageResId());
            values.put(NotificationsDBHelper.COLUMN_ACTIONS, DataUtils.convertListToString(notification.getActions(), NotificationsDBHelper.ACTIONS_DELIMITER));

            long newRowId = this._db.insert(NotificationsDBHelper.TABLE_NAME, null, values);
            if (newRowId != -1) notification.setId(newRowId);
            return null;
        });
    }

    public BackgroundTask<List<AppNotification>> getAllNotifications() {
        return new BackgroundTask<>(() -> {
            List<AppNotification> notifications = new ArrayList<>();

            Cursor cursor = this._db.query(
                    NotificationsDBHelper.TABLE_NAME,
                    null, // No columns
                    null, // No selection
                    null, // No selection arguments
                    null, // No group by
                    null, // No having
                    null  // Default order
            );


            if (cursor == null) return notifications;

            while (cursor.moveToNext()) {
                notifications.add(_createNotificationFromCursor(cursor));
            }

            cursor.close();
            return notifications;
        });
    }

    public BackgroundTask<List<AppNotification>> getUncheckedNotifications() {
        return new BackgroundTask<>(() -> {
            List<AppNotification> notifications = new ArrayList<>();
            String selection = NotificationsDBHelper.COLUMN_IS_CHECKED + " = ?";
            String[] selectionArgs = {"0"};

            Cursor cursor = this._db.query(
                    NotificationsDBHelper.TABLE_NAME,
                    null, // All columns
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor == null) return notifications;

            while (cursor.moveToNext()) {
                notifications.add(_createNotificationFromCursor(cursor));
            }

            cursor.close();
            return notifications;
        });
    }

    public BackgroundTask<List<AppNotification>> getAllNotificationsForDevice(int deviceId) {
        return new BackgroundTask<>(() -> {
            List<AppNotification> notifications = new ArrayList<>();
            String selection = NotificationsDBHelper.COLUMN_DEVICE_ID + " = ?";
            String[] selectionArgs = {String.valueOf(deviceId)};

            Cursor cursor = this._db.query(
                    NotificationsDBHelper.TABLE_NAME,
                    null, // All columns
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor == null) return notifications;

            while (cursor.moveToNext()) {
                notifications.add(_createNotificationFromCursor(cursor));
            }
            cursor.close();

            return notifications;
        });
    }

    public BackgroundTask<List<AppNotification>> getUncheckedNotificationsForDevice(int deviceId) {
        return new BackgroundTask<>(() -> {
            List<AppNotification> notifications = new ArrayList<>();
            String selection = NotificationsDBHelper.COLUMN_DEVICE_ID + " = ? AND " + NotificationsDBHelper.COLUMN_IS_CHECKED + " = ?";
            String[] selectionArgs = {String.valueOf(deviceId), "0"};

            Cursor cursor = this._db.query(
                    NotificationsDBHelper.TABLE_NAME,
                    null, // All columns
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor == null) return notifications;

            while (cursor.moveToNext()) {
                notifications.add(_createNotificationFromCursor(cursor));
            }
            cursor.close();

            return notifications;
        });
    }

    public void close() {
        _dbHelper.close();
    }
}
