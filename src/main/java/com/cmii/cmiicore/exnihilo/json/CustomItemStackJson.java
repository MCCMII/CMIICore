package com.cmii.cmiicore.exnihilo.json;

import com.google.gson.*;
import com.cmii.cmiicore.exnihilo.util.LogUtil;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

import java.lang.reflect.Type;

public class CustomItemStackJson implements JsonDeserializer<ItemStack>, JsonSerializer<ItemStack> {
    public static final CustomItemStackJson INSTANCE = new CustomItemStackJson();

    private CustomItemStackJson() {}

    @Override
    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonHelper helper = new JsonHelper(json);

        String name = helper.getString("name");
        int amount = helper.getNullableInteger("amount", 1);
        int meta = helper.getNullableInteger("meta", 0);

        Item item = Item.getByNameOrId(name);
        if (item == null) {
            LogUtil.error("Error parsing JSON: Invalid Item: " + json);
            LogUtil.error("This may result in crashing or other undefined behavior");

            return new ItemStack(Items.AIR);
        }

        ItemStack stack = new ItemStack(item, amount, meta);

        if (json.getAsJsonObject().has("nbt")) {
            try {
                stack.setTagCompound(JsonToNBT.getTagFromJson(json.getAsJsonObject().get("nbt").getAsString()));
            } catch (NBTException e) {
                LogUtil.error("Could not convert JSON to NBT: " + json.getAsJsonObject().get("nbt").getAsString(), e);
                e.printStackTrace();
            }
        }

        return stack;
    }

    @Override
    public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("name", src.getItem().getRegistryName() == null ? "" : src.getItem().getRegistryName().toString());
        jsonObject.addProperty("amount", src.getCount());
        jsonObject.addProperty("meta", src.getItemDamage());

        NBTTagCompound nbt = src.getTagCompound();
        if (nbt != null) {
            jsonObject.addProperty("nbt", nbt.toString());
        }

        return jsonObject;
    }
}
