package com.smartplant.smartplantandroid.utils.data.json;

import static com.smartplant.smartplantandroid.utils.network.ApiHelper.getGson;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Map;

public class JsonUtils {
    private static final @NonNull Gson _gson = getGson();

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
