package com.cmii.cmiicore.exnihilo.json;

import com.google.gson.*;
import com.cmii.cmiicore.exnihilo.registries.types.FluidBlockTransformer;
import com.cmii.cmiicore.exnihilo.util.BlockInfo;
import com.cmii.cmiicore.exnihilo.util.EntityInfo;
import net.minecraft.item.crafting.Ingredient;

import java.lang.reflect.Type;

public class CustomFluidBlockTransformerJson implements JsonDeserializer<FluidBlockTransformer>, JsonSerializer<FluidBlockTransformer> {
    public static final CustomFluidBlockTransformerJson INSTANCE = new CustomFluidBlockTransformerJson();

    private CustomFluidBlockTransformerJson() {}

    @Override
    public JsonElement serialize(FluidBlockTransformer src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("fluidName", src.getFluidName());
        obj.add("input", context.serialize(src.getInput()));
        obj.add("output", context.serialize(src.getOutput()));

        if (src.getToSpawn() != EntityInfo.EMPTY)
            obj.add("toSpawn", context.serialize(src.getToSpawn()));

        if (src.getSpawnCount() != 0) {
            obj.addProperty("spawnCount", src.getSpawnCount());
        }

        if (src.getSpawnRange() != 0) {
            obj.addProperty("spawnRange", src.getSpawnRange());
        }

        return obj;
    }

    @Override
    public FluidBlockTransformer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();

        return new FluidBlockTransformer(
                obj.getAsJsonPrimitive("fluidName").getAsString(),
                context.deserialize(obj.get("input"), Ingredient.class),
                context.deserialize(obj.get("output"), BlockInfo.class),
                obj.has("toSpawn") ? (EntityInfo) context.deserialize(obj.get("toSpawn"), EntityInfo.class) : EntityInfo.EMPTY,
                obj.has("spawnCount") ? obj.getAsJsonPrimitive("spawnCount").getAsInt() : 0,
                obj.has("spawnRange") ? obj.getAsJsonPrimitive("spawnRange").getAsInt() : 0
        );
    }
}