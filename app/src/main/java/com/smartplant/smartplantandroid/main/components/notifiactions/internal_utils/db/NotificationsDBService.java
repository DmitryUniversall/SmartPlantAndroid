package com.smartplant.smartplantandroid.main.components.notifiactions.internal_utils.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import com.smartplant.smartplantandroid.core.async.background_task.BackgroundTask;
import com.smartplant.smartplantandroid.core.data.DataUtils;
import com.smartplant.smartplantandroid.core.data.db.CoreSqliteDBHelper;
import com.smartplant.smartplantandroid.main.components.notifiactions.models.AppNotification;

import java.util.ArrayList;
import java.util.List;

public class NotificationsDBService {
    private final SQLiteDatabase _db;

    public NotificationsDBService() {
        _db = CoreSqliteDBHelper.getInstance().getWritableDatabase();
    }

    private AppNotification _createNotificationFromCursor(@NonNull Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(NotificationsDBTable.COLUMN_ID));
        int deviceId = cursor.getInt(cursor.getColumnIndexOrThrow(NotificationsDBTable.COLUMN_DEVICE_ID));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(NotificationsDBTable.COLUMN_TITLE));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(NotificationsDBTable.COLUMN_DESCRIPTION));
        int imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(NotificationsDBTable.COLUMN_IMAGE_RES_ID));
        boolean isChecked = cursor.getInt(cursor.getColumnIndexOrThrow(NotificationsDBTable.COLUMN_IS_CHECKED)) == 1;
        String actions = cursor.getString(cursor.getColumnIndexOrThrow(NotificationsDBTable.COLUMN_ACTIONS));

        return new AppNotification(id, deviceId, isChecked, title, description, imageResId, DataUtils.convertStringToIntegerList(actions, NotificationsDBTable.ACTIONS_DELIMITER));
    }

    public BackgroundTask<Void> insertNotification(@NonNull AppNotification notification) {
        return new BackgroundTask<>(() -> {
            ContentValues values = new ContentValues();
            values.put(NotificationsDBTable.COLUMN_TITLE, notification.getTitle());
            values.put(NotificationsDBTable.COLUMN_DEVICE_ID, notification.getDeviceId());
            values.put(NotificationsDBTable.COLUMN_IS_CHECKED, notification.isChecked());
            values.put(NotificationsDBTable.COLUMN_DESCRIPTION, notification.getDescription());
            values.put(NotificationsDBTable.COLUMN_IMAGE_RES_ID, notification.getIconResId());
            values.put(NotificationsDBTable.COLUMN_ACTIONS, DataUtils.convertListToString(notification.getActions(), NotificationsDBTable.ACTIONS_DELIMITER));

            long newRowId = this._db.insert(NotificationsDBTable.TABLE_NAME, null, values);
            if (newRowId != -1) notification.setId(newRowId);
            return null;
        });
    }

    public BackgroundTask<Void> updateNotification(@NonNull AppNotification notification) {
        return new BackgroundTask<>(() -> {
            if (notification.getId() == null) throw new IllegalStateException("Unable to update AppNotification: notification has no id");

            ContentValues values = new ContentValues();
            values.put(NotificationsDBTable.COLUMN_TITLE, notification.getTitle());
            values.put(NotificationsDBTable.COLUMN_DEVICE_ID, notification.getDeviceId());
            values.put(NotificationsDBTable.COLUMN_IS_CHECKED, notification.isChecked());
            values.put(NotificationsDBTable.COLUMN_DESCRIPTION, notification.getDescription());
            values.put(NotificationsDBTable.COLUMN_IMAGE_RES_ID, notification.getIconResId());
            values.put(NotificationsDBTable.COLUMN_ACTIONS, DataUtils.convertListToString(notification.getActions(), NotificationsDBTable.ACTIONS_DELIMITER));
            _db.update(NotificationsDBTable.TABLE_NAME, values, NotificationsDBTable.COLUMN_ID + " = ?", new String[]{String.valueOf(notification.getId())});
            return null;
        });
    }

    public BackgroundTask<List<AppNotification>> getAllNotifications() {
        return new BackgroundTask<>(() -> {
            List<AppNotification> notifications = new ArrayList<>();

            Cursor cursor = this._db.query(
                    NotificationsDBTable.TABLE_NAME,
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
            String selection = NotificationsDBTable.COLUMN_IS_CHECKED + " = ?";
            String[] selectionArgs = {"0"};

            Cursor cursor = this._db.query(
                    NotificationsDBTable.TABLE_NAME,
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
            String selection = NotificationsDBTable.COLUMN_DEVICE_ID + " = ?";
            String[] selectionArgs = {String.valueOf(deviceId)};

            Cursor cursor = this._db.query(
                    NotificationsDBTable.TABLE_NAME,
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
            String selection = NotificationsDBTable.COLUMN_DEVICE_ID + " = ? AND " + NotificationsDBTable.COLUMN_IS_CHECKED + " = ?";
            String[] selectionArgs = {String.valueOf(deviceId), "0"};

            Cursor cursor = this._db.query(
                    NotificationsDBTable.TABLE_NAME,
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
}
