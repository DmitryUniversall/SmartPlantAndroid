package com.smartplant.smartplantandroid.main.components.notifiactions.utils;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.main.components.devices.utils.DevicesLocalDataManagerST;
import com.smartplant.smartplantandroid.main.components.notifiactions.models.AbstractAppNotification;

import java.util.Locale;

public class AndroidNotificationUtils {
    private static final String CHANNEL_ID = "SmartPlantNotificationChannelMain";
    private static final String CHANNEL_NAME = "SmartPlantNotifications";
    private static final String CHANNEL_DESCRIPTION = "SmartPlantNotifications";

    public static final int PERMISSION_REQUEST_CODE = 100;

    public static void createNotificationChannel(@NonNull Context context) {
        AppLogger.info("Creating notification channel");

        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
        channel.setDescription(CHANNEL_DESCRIPTION);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        if (notificationManager == null)
            throw new IllegalStateException("Unable to create notification channel: Unable to get NotificationManager");

        notificationManager.createNotificationChannel(channel);
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
        if (deviceName == null)
            throw new IllegalArgumentException(String.format(Locale.getDefault(), "Unable to send notification: Unknown device id=%d", notification.getDeviceId()));
        String description = notification.getDescription();
        String notificationDescription = String.format(Locale.getDefault(), "%s '%s': %s", device, deviceName, description);
        bigTextStyle.bigText(notificationDescription);
        builder.setStyle(bigTextStyle);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            AppLogger.warning("Unable to send notification: Permission not granted");
            return;
        }

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }

    public static boolean isNotificationPermissionGranted(@NonNull Context context) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU) return true;
        return ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestNotificationPermissionIfNeeded(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU) return;
        if (isNotificationPermissionGranted(activity)) return;

        AppLogger.info("Requesting notification permission");
        ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.POST_NOTIFICATIONS);
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
    }
}
