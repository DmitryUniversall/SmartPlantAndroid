package com.smartplant.smartplantandroid.main.components.notifiactions.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.main.components.devices.utils.DevicesLocalDataManagerST;
import com.smartplant.smartplantandroid.main.components.notifiactions.models.AbstractAppNotification;

import java.util.Locale;

public class AndroidNotificationUtils {
    private static final String CHANNEL_ID = "SmartPlantNotificationChannelMain";
    private static final String CHANNEL_NAME = "SmartPlant Notifications";
    private static final String CHANNEL_DESCRIPTION = "SmartPlant Notifications";

    public static void createNotificationChannel(@NonNull Context context) {
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
        channel.setDescription(CHANNEL_DESCRIPTION);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        if (notificationManager != null) notificationManager.createNotificationChannel(channel);
    }

    public static void sendGenericAndroidNotification(@NonNull AbstractAppNotification notification) {
        DevicesLocalDataManagerST devicesLocalDataManager = DevicesLocalDataManagerST.getInstance();
        Context context = notification.getContext();

        createNotificationChannel(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_seedling)  // TODO: Use logo here?
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getDescription())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)  // TODO: To func params
                .setAutoCancel(true);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        String device = context.getString(R.string.device);
        String deviceName = devicesLocalDataManager.getDeviceName(notification.getDeviceId());
        if (deviceName == null) throw new IllegalArgumentException(String.format(Locale.getDefault(), "Unable to send notification: Unknown device id=%d", notification.getDeviceId()));
        String description = notification.getDescription();
        String notificationDescription = String.format(Locale.getDefault(), "%s '%s': %s", device, deviceName, description);
        bigTextStyle.bigText(notificationDescription);
        builder.setStyle(bigTextStyle);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // TODO: Check permissions
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
