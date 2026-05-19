package com.cmii.cmiicore.exnihilo.json;

import com.google.gson.*;
import com.cmii.cmiicore.exnihilo.registries.types.WitchWaterWorld;
import com.cmii.cmiicore.exnihilo.util.BlockInfo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

public class CustomWitchWaterWorld implements JsonDeserializer<WitchWaterWorld>, JsonSerializer<WitchWaterWorld> {
    public static final CustomWitchWaterWorld INSTANCE = new CustomWitchWaterWorld();

    private CustomWitchWaterWorld() {}

    @Override
    public JsonObject serialize(WitchWaterWorld src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        for (Map.Entry<BlockInfo, Integer> entry : src.toMap().entrySet())
            json.add(entry.getKey().toString(), new JsonPrimitive(entry.getValue()));
        return json;
    }

    @Override
    public WitchWaterWorld deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        WitchWaterWorld www = new WitchWaterWorld(new ArrayList<>(), new ArrayList<>());
        for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet())
            www.add(new BlockInfo(entry.getKey()), entry.getValue().getAsInt());
        return www;
    }
}