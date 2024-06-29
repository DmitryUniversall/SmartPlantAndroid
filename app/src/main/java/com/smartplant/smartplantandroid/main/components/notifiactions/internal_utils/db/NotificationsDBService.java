package com.smartplant.smartplantandroid.main.components.notifiactions.internal_utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import com.smartplant.smartplantandroid.core.async.background_task.BackgroundTask;
import com.smartplant.smartplantandroid.core.data.db.CoreSqliteDBHelper;
import com.smartplant.smartplantandroid.main.components.notifiactions.models.AbstractAppNotification;
import com.smartplant.smartplantandroid.main.components.notifiactions.utils.generics.AppNotificationFactory;

import java.util.ArrayList;
import java.util.List;

public class NotificationsDBService {
    private final @NonNull SQLiteDatabase _db;
    private final @NonNull Context _context;

    public NotificationsDBService(@NonNull Context context) {
        this._db = CoreSqliteDBHelper.getInstance().getWritableDatabase();
        this._context = context;
    }

    private AbstractAppNotification _createNotificationFromCursor(@NonNull Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(NotificationsDBTable.COLUMN_ID));
        int deviceId = cursor.getInt(cursor.getColumnIndexOrThrow(NotificationsDBTable.COLUMN_DEVICE_ID));
        int notificationType = cursor.getInt(cursor.getColumnIndexOrThrow(NotificationsDBTable.COLUMN_NOTIFICATION_TYPE));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(NotificationsDBTable.COLUMN_TITLE));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(NotificationsDBTable.COLUMN_DESCRIPTION));
        boolean isChecked = cursor.getInt(cursor.getColumnIndexOrThrow(NotificationsDBTable.COLUMN_IS_CHECKED)) == 1;

        return AppNotificationFactory.createNotification(notificationType, this._context, deviceId, isChecked, id, title, description, null, null);  // TODO: CreatedAt
    }

    private ContentValues _createContentValuesFromData(AbstractAppNotification notificationData) {
        ContentValues values = new ContentValues();
        values.put(NotificationsDBTable.COLUMN_TITLE, notificationData.getTitle());
        values.put(NotificationsDBTable.COLUMN_DEVICE_ID, notificationData.getDeviceId());
        values.put(NotificationsDBTable.COLUMN_IS_CHECKED, notificationData.isChecked());
        values.put(NotificationsDBTable.COLUMN_DESCRIPTION, notificationData.getDescription());
        values.put(NotificationsDBTable.COLUMN_NOTIFICATION_TYPE, notificationData.getNotificationType());
        return values;
    }

    public BackgroundTask<Void> insertNotification(@NonNull AbstractAppNotification notificationData) {
        return new BackgroundTask<>(() -> {
            long newRowId = this._db.insertOrThrow(NotificationsDBTable.TABLE_NAME, null, _createContentValuesFromData(notificationData));
            if (newRowId != -1) notificationData.setId(newRowId);
            return null;
        });
    }

    public BackgroundTask<Void> updateNotification(@NonNull AbstractAppNotification notificationData) {
        return new BackgroundTask<>(() -> {
            if (notificationData.getId() == null)
                throw new IllegalStateException("Unable to update AppNotification: notification has no id");
            _db.update(NotificationsDBTable.TABLE_NAME, _createContentValuesFromData(notificationData), NotificationsDBTable.COLUMN_ID + " = ?", new String[]{String.valueOf(notificationData.getId())});
            return null;
        });
    }

    public BackgroundTask<List<AbstractAppNotification>> getAllNotifications() {
        return new BackgroundTask<>(() -> {
            List<AbstractAppNotification> notifications = new ArrayList<>();

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

    public BackgroundTask<List<AbstractAppNotification>> getUncheckedNotifications() {
        return new BackgroundTask<>(() -> {
            List<AbstractAppNotification> notifications = new ArrayList<>();
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

    public BackgroundTask<List<AbstractAppNotification>> getAllNotificationsForDevice(int deviceId) {
        return new BackgroundTask<>(() -> {
            List<AbstractAppNotification> notifications = new ArrayList<>();
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

    public BackgroundTask<List<AbstractAppNotification>> getUncheckedNotificationsForDevice(int deviceId) {
        return new BackgroundTask<>(() -> {
            List<AbstractAppNotification> notifications = new ArrayList<>();
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
