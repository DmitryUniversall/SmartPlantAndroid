package com.smartplant.smartplantandroid.utils.data.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtils {
    private static final Gson _gson = createGson();

    private static Gson createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        return gsonBuilder.create();
    }

    public static Gson getGson() {  // TODO: Return same or create new?
        return _gson;
    }
}
