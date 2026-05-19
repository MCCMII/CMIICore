package com.cmii.cmiicore.exnihilo.json;

import com.google.gson.*;
import com.cmii.cmiicore.exnihilo.util.EntityInfo;

import java.lang.reflect.Type;

public class CustomEntityInfoJson implements JsonDeserializer<EntityInfo>, JsonSerializer<EntityInfo> {
    public static final CustomEntityInfoJson INSTANCE = new CustomEntityInfoJson();

    private CustomEntityInfoJson() {}

    @Override
    public JsonElement serialize(EntityInfo src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getName());
    }

    @Override
    public EntityInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new EntityInfo(json.getAsString());
    }
}