package com.smartplant.smartplantandroid.main.components.notifiactions.internal_utils.android;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.annotation.NonNull;

import com.smartplant.smartplantandroid.main.components.notifiactions.models.AppNotificationData;

public class AndroidNotificationUtils {
    private static final String CHANNEL_ID = "SmartPlantNotificationChannelMain";
    private static final String CHANNEL_NAME = "SmartPlant Notifications";
    private static final String CHANNEL_DESCRIPTION = "SmartPlant Notifications";

    public static void createNotificationChannel(Context context) {
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
        channel.setDescription(CHANNEL_DESCRIPTION);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        if (notificationManager != null) notificationManager.createNotificationChannel(channel);
    }

    public static void sendAndroidNotification(@NonNull AppNotificationData notificationData) {  // TODO: Add icon field for AppNotificationData
        // TODO
    }
}
