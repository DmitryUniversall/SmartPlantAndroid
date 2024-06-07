package com.smartplant.smartplantandroid.core.data.json;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.smartplant.smartplantandroid.core.state.settings.ProjectSettings;

import java.util.Map;

public class JsonUtils {
    private static final @NonNull Gson _gson = _createGson();

    @NonNull
    private static Gson _createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat(ProjectSettings.DATE_FORMAT);
        return gsonBuilder.create();
    }

    @NonNull
    public static Gson getGson() {
        return _gson;
    }

    @NonNull
    public static <T> T fromJsonWithNulls(@NonNull JsonObject json, @NonNull Class<T> model, @NonNull Gson gson) {
        JsonObject sanitizedObject = new JsonObject();
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isJsonNull()) {
                sanitizedObject.add(entry.getKey(), entry.getValue());
            }
        }

        return gson.fromJson(sanitizedObject, model);
    }

    @NonNull
    public static <T> T fromJsonWithNulls(@NonNull JsonObject json, @NonNull Class<T> model) {
        return fromJsonWithNulls(json, model, _gson);
    }

    @NonNull
    public static <T> T fromJsonWithNulls(@NonNull String string, @NonNull Class<T> model, @NonNull Gson gson) {
        JsonObject json = JsonParser.parseString(string).getAsJsonObject();
        return fromJsonWithNulls(json, model, gson);
    }

    @NonNull
    public static <T> T fromJsonWithNulls(@NonNull String string, @NonNull Class<T> model) {
        return fromJsonWithNulls(string, model, _gson);
    }
}
