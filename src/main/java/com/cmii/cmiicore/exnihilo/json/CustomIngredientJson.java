package com.cmii.cmiicore.exnihilo.json;

import com.google.gson.*;
import com.cmii.cmiicore.exnihilo.registries.ingredient.IngredientUtil;
import com.cmii.cmiicore.exnihilo.registries.ingredient.OreIngredientStoring;
import com.cmii.cmiicore.exnihilo.util.LogUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.lang.reflect.Type;

public class CustomIngredientJson implements JsonDeserializer<Ingredient>, JsonSerializer<Ingredient> {
    public static final CustomIngredientJson INSTANCE = new CustomIngredientJson();

    private CustomIngredientJson() {}

    @Override
    public Ingredient deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
            String s = json.getAsString();
            return IngredientUtil.parseFromString(s);
        } else {
            LogUtil.error("Error parsing JSON: No Primitive String: " + json);
        }

        return Ingredient.EMPTY;
    }

    @Override
    public JsonElement serialize(Ingredient src, Type typeOfSrc, JsonSerializationContext context) {
        if (src instanceof OreIngredientStoring) {
            return new JsonPrimitive("ore:" + ((OreIngredientStoring) src).getOreName());
        } else {
            ItemStack[] stacks = src.getMatchingStacks();
            if (stacks.length > 0)
                return new JsonPrimitive(stacks[0].getItem().getRegistryName().toString() + ":" + stacks[0].getMetadata());
        }

        return new JsonPrimitive("minecraft:air:0");
    }
}