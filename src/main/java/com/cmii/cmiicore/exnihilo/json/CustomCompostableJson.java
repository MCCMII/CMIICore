package com.cmii.cmiicore.exnihilo.json;

import com.google.gson.*;
import com.cmii.cmiicore.exnihilo.registries.types.Compostable;
import com.cmii.cmiicore.exnihilo.texturing.Color;
import com.cmii.cmiicore.exnihilo.util.BlockInfo;

import java.lang.reflect.Type;

public class CustomCompostableJson implements JsonDeserializer<Compostable>, JsonSerializer<Compostable> {
    public static final CustomCompostableJson INSTANCE = new CustomCompostableJson();

    private CustomCompostableJson() {}

    @Override
    public JsonElement serialize(Compostable src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("value", src.getValue());
        obj.add("compostBlock", context.serialize(src.getCompostBlock(), BlockInfo.class));
        if (src.getColor() != Color.INVALID_COLOR) {
            obj.add("color", context.serialize(src.getColor(), Color.class));
        }

        return obj;
    }

    @Override
    public Compostable deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonHelper helper = new JsonHelper(json);
        JsonObject obj = json.getAsJsonObject();

        float value = (float) helper.getDouble("value");
        Color color = Color.INVALID_COLOR;
        if (obj.has("color")) {
            color = context.deserialize(json.getAsJsonObject().get("color"), Color.class);
        }

        BlockInfo result = context.deserialize(json.getAsJsonObject().get("compostBlock"), BlockInfo.class);

        return new Compostable(value, color, result);
    }
}