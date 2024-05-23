package com.smartplant.smartplantandroid.utils.network.http;

import androidx.annotation.NonNull;

import com.smartplant.smartplantandroid.utils.settings.ProjectSettingsST;

public class HTTPApiHelper {
    @NonNull
    public static String getBaseURL(String protocol) {
        ProjectSettingsST projectSettings = ProjectSettingsST.getInstance();

        return protocol +
                "://" +
                projectSettings.getTSHost() +
                ":" +
                projectSettings.getTSPort() +
                projectSettings.getApiBaseUrl();
    }
}
