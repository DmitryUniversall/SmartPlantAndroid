package com.smartplant.smartplantandroid.utils.data.json;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

public class CustomJsonObjectDeserializer implements JsonDeserializer<JsonObject> {
    @Override
    public JsonObject deserialize(JsonElement json, Type typeOfT, com.google.gson.JsonDeserializationContext context) {
        if (json.isJsonNull()) return null;
        return json.getAsJsonObject();
    }
}
