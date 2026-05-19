package com.cmii.cmiicore.exnihilo.registries.registries;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.cmii.cmiicore.exnihilo.api.registries.IFluidTransformRegistry;
import com.cmii.cmiicore.exnihilo.json.CustomBlockInfoJson;
import com.cmii.cmiicore.exnihilo.json.CustomItemInfoJson;
import com.cmii.cmiicore.exnihilo.registries.manager.ExNihiloRegistryManager;
import com.cmii.cmiicore.exnihilo.registries.registries.prefab.BaseRegistryMap;
import com.cmii.cmiicore.exnihilo.registries.types.FluidTransformer;
import com.cmii.cmiicore.exnihilo.util.BlockInfo;
import com.cmii.cmiicore.exnihilo.util.ItemInfo;
import net.minecraftforge.fluids.FluidRegistry;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FluidTransformRegistry extends BaseRegistryMap<String, List<FluidTransformer>> implements IFluidTransformRegistry {
    public FluidTransformRegistry() {
        super(new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(ItemInfo.class, CustomItemInfoJson.INSTANCE)
                        .registerTypeAdapter(BlockInfo.class, CustomBlockInfoJson.INSTANCE)
                        .create(),
                new com.google.gson.reflect.TypeToken<Map<String, List<FluidTransformer>>>() {}.getType(),
                ExNihiloRegistryManager.FLUID_TRANSFORM_DEFAULT_REGISTRY_PROVIDERS);
    }

    public void register(@NotNull String inputFluid, @NotNull String outputFluid, int duration, @NotNull BlockInfo[] transformingBlocks, @NotNull BlockInfo[] blocksToSpawn) {
        register(new FluidTransformer(inputFluid, outputFluid, duration, transformingBlocks, blocksToSpawn));
    }

    public void register(@NotNull FluidTransformer transformer) {
        List<FluidTransformer> list = registry.get(transformer.getInputFluid());

        if (list == null) {
            list = new ArrayList<>();
        }

        list.add(transformer);
        registry.put(transformer.getInputFluid(), list);
    }

    public boolean containsKey(@NotNull String inputFluid) {
        return registry.containsKey(inputFluid);
    }

    public FluidTransformer getFluidTransformer(@NotNull String inputFluid, @NotNull String outputFluid) {
        if (registry.containsKey(inputFluid)) {
            for (FluidTransformer transformer : registry.get(inputFluid)) {
                if (transformer.getInputFluid().equals(inputFluid) && transformer.getOutputFluid().equals(outputFluid))
                    return transformer;
            }
        }
        return null;
    }

    @NotNull
    public List<FluidTransformer> getFluidTransformers(@NotNull String inputFluid) {
        return registry.get(inputFluid);
    }

    @Override
    protected void registerEntriesFromJSON(FileReader fr) {
        List<FluidTransformer> gsonInput = gson.fromJson(fr, new TypeToken<List<FluidTransformer>>() {}.getType());

        for (FluidTransformer transformer : gsonInput) {
            register(transformer);
        }
    }

    /**
     * Overridden as I don't want the registry to get saved directly,
     * rather a List that equals the contents of the registry
     */
    @Override
    public void saveJson(File file) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(file);

            gson.toJson(getFluidTransformers(), fw);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fw);
        }
    }

    public List<FluidTransformer> getFluidTransformers() {
        List<FluidTransformer> fluidTransformers = new ArrayList<>();
        for (List<FluidTransformer> transformers : registry.values()) {
            fluidTransformers.addAll(transformers);
        }
        return fluidTransformers;
    }

    @Override
    public List<?> getRecipeList() {
        return new ArrayList<>();
    }
}