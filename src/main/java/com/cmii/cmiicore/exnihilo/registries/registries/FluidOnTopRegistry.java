package com.cmii.cmiicore.exnihilo.registries.registries;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.cmii.cmiicore.exnihilo.api.registries.IFluidOnTopRegistry;
import com.cmii.cmiicore.exnihilo.json.CustomBlockInfoJson;
import com.cmii.cmiicore.exnihilo.json.CustomItemInfoJson;
import com.cmii.cmiicore.exnihilo.registries.manager.ExNihiloRegistryManager;
import com.cmii.cmiicore.exnihilo.registries.registries.prefab.BaseRegistryList;
import com.cmii.cmiicore.exnihilo.registries.types.FluidFluidBlock;
import com.cmii.cmiicore.exnihilo.util.BlockInfo;
import com.cmii.cmiicore.exnihilo.util.ItemInfo;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class FluidOnTopRegistry extends BaseRegistryList<FluidFluidBlock> implements IFluidOnTopRegistry {
    public FluidOnTopRegistry() {
        super(
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(ItemInfo.class, CustomItemInfoJson.INSTANCE)
                        .registerTypeAdapter(BlockInfo.class, CustomBlockInfoJson.INSTANCE)
                        .create(),
                ExNihiloRegistryManager.FLUID_ON_TOP_DEFAULT_REGISTRY_PROVIDERS
        );
    }

    public void register(@NotNull Fluid fluidInBarrel, @NotNull Fluid fluidOnTop, @NotNull BlockInfo result) {
        registry.add(new FluidFluidBlock(fluidInBarrel.getName(), fluidOnTop.getName(), result));
    }

    public void register(@NotNull Fluid fluidInBarrel, @NotNull Fluid fluidOnTop, @NotNull ItemInfo result) {
        if (result.hasBlock())
            registry.add(new FluidFluidBlock(fluidInBarrel.getName(), fluidOnTop.getName(), new BlockInfo(result.getItemStack())));
    }

    public boolean isValidRecipe(Fluid fluidInBarrel, Fluid fluidOnTop) {
        if (fluidInBarrel == null || fluidOnTop == null)
            return false;
        for (FluidFluidBlock fBlock : registry) {
            if (fBlock.getFluidInBarrel().equals(fluidInBarrel.getName()) &&
                    fBlock.getFluidOnTop().equals(fluidOnTop.getName()))
                return true;
        }

        return false;
    }

    @Nonnull
    public BlockInfo getTransformedBlock(@NotNull Fluid fluidInBarrel, @NotNull Fluid fluidOnTop) {
        for (FluidFluidBlock fBlock : registry) {
            if (fBlock.getFluidInBarrel().equals(fluidInBarrel.getName()) &&
                    fBlock.getFluidOnTop().equals(fluidOnTop.getName()))
                return fBlock.getResult();
        }

        return BlockInfo.EMPTY;
    }

    @Override
    protected void registerEntriesFromJSON(FileReader fr) {
        List<FluidFluidBlock> gsonInput = gson.fromJson(fr, new TypeToken<List<FluidFluidBlock>>() {}.getType());
        registry.addAll(gsonInput);
    }

    @Override
    public List<?> getRecipeList() {
        return new ArrayList<>();
    }
}