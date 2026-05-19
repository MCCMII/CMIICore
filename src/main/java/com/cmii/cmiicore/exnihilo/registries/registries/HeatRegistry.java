package com.cmii.cmiicore.exnihilo.registries.registries;

import com.google.gson.GsonBuilder;
import com.cmii.cmiicore.exnihilo.api.registries.IHeatRegistry;
import com.cmii.cmiicore.exnihilo.json.CustomBlockInfoJson;
import com.cmii.cmiicore.exnihilo.registries.manager.ExNihiloRegistryManager;
import com.cmii.cmiicore.exnihilo.registries.registries.prefab.BaseRegistryMap;
import com.cmii.cmiicore.exnihilo.util.BlockInfo;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeatRegistry extends BaseRegistryMap<BlockInfo, Integer> implements IHeatRegistry {

    public HeatRegistry() {
        super(
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(BlockInfo.class, CustomBlockInfoJson.INSTANCE)
                        .create(),
                new com.google.gson.reflect.TypeToken<Map<BlockInfo, Integer>>() {}.getType(),
                ExNihiloRegistryManager.HEAT_DEFAULT_REGISTRY_PROVIDERS
        );
    }

    public void register(BlockInfo info, int heatAmount) {
        registry.put(info, heatAmount);
    }

    public void register(ItemStack stack, int heatAmount) {
        register(new BlockInfo(stack), heatAmount);
    }

    public int getHeatAmount(ItemStack stack) {
        return registry.get(new BlockInfo(stack));
    }

    public int getHeatAmount(BlockInfo info) {
        return registry.getOrDefault(info, 0);
    }

    @Override
    public void registerEntriesFromJSON(FileReader fr) {
        HashMap<String, Integer> gsonInput = gson.fromJson(fr, new com.google.gson.reflect.TypeToken<HashMap<String, Integer>>() {}.getType());

        for (Map.Entry<String, Integer> entry : gsonInput.entrySet()) {
            registry.put(new BlockInfo(entry.getKey()), entry.getValue());
        }
    }

    @Override
    public List<?> getRecipeList() {
        return null;
    }
}