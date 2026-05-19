package com.cmii.cmiicore.exnihilo.json;

import com.google.gson.*;
import com.cmii.cmiicore.exnihilo.util.ItemInfo;
import com.cmii.cmiicore.exnihilo.util.LogUtil;
import net.minecraft.item.Item;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

import java.lang.reflect.Type;

public class CustomItemInfoJson implements JsonDeserializer<ItemInfo>, JsonSerializer<ItemInfo> {
    public static final CustomItemInfoJson INSTANCE = new CustomItemInfoJson();

    private CustomItemInfoJson() {}

    @Override
    public JsonElement serialize(ItemInfo src, Type typeOfSrc, JsonSerializationContext context) {

        NBTTagCompound nbt = src.getNbt();
        if (nbt == null || nbt.isEmpty())
            return new JsonPrimitive(src.getItem().getRegistryName().toString() + ":" + src.getMeta());

        JsonObject obj = new JsonObject();
        obj.add("name", context.serialize(src.getItem().getRegistryName().toString()));
        obj.add("meta", context.serialize(src.getMeta()));
        obj.add("nbt", context.serialize(src.getNbt().toString()));
        return obj;
    }

    @Override
    public ItemInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
            String name = json.getAsString();
            return new ItemInfo(name);
        } else {
            JsonHelper helper = new JsonHelper(json);

            String name = helper.getString("name");
            int meta = helper.getNullableInteger("meta", 0);

            Item item = Item.getByNameOrId(name);

            NBTTagCompound nbt = null;
            if (json.getAsJsonObject().has("nbt")) {
                try {
                    nbt = JsonToNBT.getTagFromJson(json.getAsJsonObject().get("nbt").getAsString());
                } catch (NBTException e) {
                    LogUtil.error("Could not convert JSON to NBT: " + json.getAsJsonObject().get("nbt").getAsString(), e);
                    e.printStackTrace();
                }
            }

            if (item == null) {
                LogUtil.error("Error parsing JSON: Invalid Item: " + json);
                LogUtil.error("This may result in crashing or other undefined behavior");
                return ItemInfo.EMPTY;
            }

            return new ItemInfo(item, meta, nbt);
        }
    }
}