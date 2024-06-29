package com.smartplant.smartplantandroid.main.ui.views.main.navigation.notifications.items.notifications;

import android.content.Context;

import com.smartplant.smartplantandroid.main.ui.views.main.navigation.notifications.items.notifications.humidity.HighHumidityAppNotificationItem;
import com.smartplant.smartplantandroid.main.ui.views.main.navigation.notifications.items.notifications.humidity.LowHumidityAppNotificationItem;
import com.smartplant.smartplantandroid.main.ui.views.main.navigation.notifications.items.notifications.illumination.TooBrightAppNotificationItem;
import com.smartplant.smartplantandroid.main.ui.views.main.navigation.notifications.items.notifications.illumination.TooDarkAppNotificationItem;
import com.smartplant.smartplantandroid.main.ui.views.main.navigation.notifications.items.notifications.soil_moisture.WetSoilAppNotificationItem;
import com.smartplant.smartplantandroid.main.ui.views.main.navigation.notifications.items.notifications.soil_moisture.DrySoilAppNotificationItem;
import com.smartplant.smartplantandroid.main.ui.views.main.navigation.notifications.items.notifications.temperature.HighTemperatureAppNotificationItem;

import java.util.HashMap;
import java.util.Map;

public class AppNotificationFactory {
    private static final Map<Integer, Class<? extends BaseAppNotificationItem>> _notificationClasses = new HashMap<>();

    static {
        _notificationClasses.put(1, TooBrightAppNotificationItem.class);
        _notificationClasses.put(2, TooDarkAppNotificationItem.class);

        _notificationClasses.put(3, HighHumidityAppNotificationItem.class);
        _notificationClasses.put(4, LowHumidityAppNotificationItem.class);

        _notificationClasses.put(5, WetSoilAppNotificationItem.class);
        _notificationClasses.put(6, DrySoilAppNotificationItem.class);

        _notificationClasses.put(7, HighTemperatureAppNotificationItem.class);
        _notificationClasses.put(8, HighTemperatureAppNotificationItem.class);
    }

    public static BaseAppNotificationItem createNotification(int viewType, Context context) {
        Class<? extends BaseAppNotificationItem> notificationClass = _notificationClasses.get(viewType);
        if (notificationClass == null)
            throw new IllegalArgumentException("Invalid viewType: " + viewType);

        try {
            return notificationClass.getConstructor(Context.class).newInstance(context);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
