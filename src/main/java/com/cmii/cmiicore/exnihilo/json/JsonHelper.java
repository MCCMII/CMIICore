package com.cmii.cmiicore.exnihilo.json;

import com.google.gson.JsonElement;

public class JsonHelper {
    private final JsonElement json;

    public JsonHelper(JsonElement json) {
        this.json = json;
    }

    public boolean getBoolean(String object) {
        return json.getAsJsonObject().get(object).getAsBoolean();
    }

    public boolean getNullableBoolean(String object, boolean defaultValue) {
        if (json.getAsJsonObject().get(object) != null) {
            return json.getAsJsonObject().get(object).getAsBoolean();
        }
        return defaultValue;
    }

    public boolean getNullableBoolean(String object) {
        return getNullableBoolean(object, false);
    }

    public int getInteger(String object) {
        return json.getAsJsonObject().get(object).getAsInt();
    }

    public int getNullableInteger(String object, int defaultValue) {
        if (json.getAsJsonObject().get(object) != null) {
            return json.getAsJsonObject().get(object).getAsInt();
        }
        return defaultValue;
    }

    public double getDouble(String object) {
        return json.getAsJsonObject().get(object).getAsDouble();
    }

    public double getNullableDouble(String object, double defaultValue) {
        if (json.getAsJsonObject().get(object) != null) {
            return json.getAsJsonObject().get(object).getAsDouble();
        }
        return defaultValue;
    }

    public float getNullableFloat(String object, float defaultValue) {
        if (json.getAsJsonObject().get(object) != null) {
            return json.getAsJsonObject().get(object).getAsFloat();
        }
        return defaultValue;
    }

    public String getString(String object) {
        return json.getAsJsonObject().get(object).getAsString();
    }

    public String getNullableString(String object, String defaultValue) {
        if (json.getAsJsonObject().get(object) != null) {
            return json.getAsJsonObject().get(object).getAsString();
        }
        return defaultValue;
    }
}
