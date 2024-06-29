package com.smartplant.smartplantandroid.main.components.notifiactions.utils.generics;

import android.content.Context;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.smartplant.smartplantandroid.main.components.notifiactions.models.AbstractAppNotification;
import com.smartplant.smartplantandroid.main.components.notifiactions.utils.generics.humidity.HighHumidityAppNotification;
import com.smartplant.smartplantandroid.main.components.notifiactions.utils.generics.humidity.LowHumidityAppNotification;
import com.smartplant.smartplantandroid.main.components.notifiactions.utils.generics.illumination.TooBrightAppNotification;
import com.smartplant.smartplantandroid.main.components.notifiactions.utils.generics.illumination.TooDarkAppNotification;
import com.smartplant.smartplantandroid.main.components.notifiactions.utils.generics.soil.DrySoilAppNotification;
import com.smartplant.smartplantandroid.main.components.notifiactions.utils.generics.soil.WetSoilAppNotification;
import com.smartplant.smartplantandroid.main.components.notifiactions.utils.generics.temperature.HighTemperatureAppNotification;
import com.smartplant.smartplantandroid.main.components.notifiactions.utils.generics.temperature.LowTemperatureAppNotification;

import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AppNotificationFactory {
    private static final Map<Integer, Class<? extends AbstractAppNotification>> _notificationClasses = new HashMap<>();

    static {
        _notificationClasses.put(1, HighHumidityAppNotification.class);
        _notificationClasses.put(2, LowHumidityAppNotification.class);

        _notificationClasses.put(3, HighTemperatureAppNotification.class);
        _notificationClasses.put(4, LowTemperatureAppNotification.class);

        _notificationClasses.put(5, TooBrightAppNotification.class);
        _notificationClasses.put(6, TooDarkAppNotification.class);

        _notificationClasses.put(7, WetSoilAppNotification.class);
        _notificationClasses.put(8, DrySoilAppNotification.class);
    }

    public static AbstractAppNotification createNotification(
            int notificationType,
            @NonNull Context context,
            int deviceId,
            boolean isChecked,
            @Nullable Long id,
            @Nullable String title,
            @Nullable String description,
            @Nullable @DrawableRes Integer iconId,
            @Nullable Date _createdAt
    ) {
        Class<? extends AbstractAppNotification> notificationClass = _notificationClasses.get(notificationType);
        if (notificationClass == null)
            throw new IllegalArgumentException("Invalid notificationType: " + notificationType);

        try {
            Constructor<? extends AbstractAppNotification> constructor = notificationClass.getDeclaredConstructor(
                    Context.class, int.class, boolean.class, Long.class, String.class, String.class, Integer.class, Date.class
            );
            return constructor.newInstance(context, deviceId, isChecked, id, title, description, iconId, _createdAt);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create AppNotification instance", e);
        }
    }
}
