package com.cmii.cmiicore.exnihilo.registries.registries;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.cmii.cmiicore.exnihilo.api.registries.IFluidBlockTransformerRegistry;
import com.cmii.cmiicore.exnihilo.json.*;
import com.cmii.cmiicore.exnihilo.registries.ingredient.OreIngredientStoring;
import com.cmii.cmiicore.exnihilo.registries.manager.ExNihiloRegistryManager;
import com.cmii.cmiicore.exnihilo.registries.registries.prefab.BaseRegistryList;
import com.cmii.cmiicore.exnihilo.registries.types.FluidBlockTransformer;
import com.cmii.cmiicore.exnihilo.util.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class FluidBlockTransformerRegistry extends BaseRegistryList<FluidBlockTransformer> implements IFluidBlockTransformerRegistry {

    public FluidBlockTransformerRegistry() {
        super(
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(ItemInfo.class, CustomItemInfoJson.INSTANCE)
                        .registerTypeAdapter(StackInfo.class, CustomItemInfoJson.INSTANCE)
                        .registerTypeAdapter(BlockInfo.class, CustomBlockInfoJson.INSTANCE)
                        .registerTypeAdapter(Ingredient.class, CustomIngredientJson.INSTANCE)
                        .registerTypeAdapter(OreIngredientStoring.class, CustomIngredientJson.INSTANCE)
                        .registerTypeAdapter(EntityInfo.class, CustomEntityInfoJson.INSTANCE)
                        .registerTypeAdapter(FluidBlockTransformer.class, CustomFluidBlockTransformerJson.INSTANCE)
                        .create(),
                ExNihiloRegistryManager.FLUID_BLOCK_DEFAULT_REGISTRY_PROVIDERS
        );
    }

    public void register(Fluid fluid, StackInfo inputBlock, StackInfo outputBlock) {
        register(fluid, inputBlock, outputBlock, null);
    }

    public void register(Fluid fluid, @Nonnull StackInfo inputBlock, @Nonnull StackInfo outputBlock, String entityName) {
        if (fluid == null) {
            LogUtil.error("Fluid is null, this may not happen!");
            for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
                LogUtil.warn(stackTraceElement);
            }
            return;
        }

        register(fluid.getName(), Ingredient.fromStacks(inputBlock.getItemStack()), outputBlock, entityName, entityName == null ? 0 : 4, entityName == null ? 0 : 4);
    }

    public void register(Fluid fluid, String oredict, StackInfo outputBlock) {
        register(fluid, oredict, outputBlock, null);
    }

    public void register(@Nonnull Fluid fluid, @Nonnull String oredict, @Nonnull StackInfo outputBlock, String entityName) {
        register(fluid.getName(), new OreIngredientStoring(oredict), outputBlock, entityName, entityName == null ? 0 : 4, entityName == null ? 0 : 4);
    }

    /**
     * Main register function
     */
    public void register(@Nonnull String fluid, @Nonnull Ingredient input, @Nonnull StackInfo outputBlock, @Nullable String entityName, int spawnCount, int spawnRange) {
        if (outputBlock.hasBlock()) {
            register(new FluidBlockTransformer(fluid, input, new BlockInfo(outputBlock.getBlockState()), entityName, spawnCount, spawnRange));
        } else {
            LogUtil.error("Item " + outputBlock.toString() + "  has no block version!");
        }
    }

    public boolean canBlockBeTransformedWithThisFluid(@Nonnull Fluid fluid, @Nonnull ItemStack stack) {
        for (FluidBlockTransformer transformer : registry) {
            if (fluid.getName().equals(transformer.getFluidName()) && transformer.getInput().apply(stack))
                return true;
        }

        return false;
    }

    @Nonnull
    public BlockInfo getBlockForTransformation(@Nonnull Fluid fluid, @Nonnull ItemStack stack) {
        for (FluidBlockTransformer transformer : registry) {
            if (fluid.getName().equals(transformer.getFluidName()) && transformer.getInput().apply(stack)) {
                return transformer.getOutput();
            }
        }

        return BlockInfo.EMPTY;
    }

    public int getSpawnCountForTransformation(@Nonnull Fluid fluid, @Nonnull ItemStack stack) {
        for (FluidBlockTransformer transformer : registry) {
            if (fluid.getName().equals(transformer.getFluidName()) && transformer.getInput().apply(stack)) {
                return transformer.getSpawnCount();
            }
        }

        return 0;
    }

    public int getSpawnRangeForTransformation(@Nonnull Fluid fluid, @Nonnull ItemStack stack) {
        for (FluidBlockTransformer transformer : registry) {
            if (fluid.getName().equals(transformer.getFluidName()) && transformer.getInput().apply(stack)) {
                return transformer.getSpawnRange();
            }
        }

        return 0;
    }

    public FluidBlockTransformer getTransformation(@Nonnull Fluid fluid, @Nonnull ItemStack stack) {
        for (FluidBlockTransformer transformer : registry) {
            if (fluid.getName().equals(transformer.getFluidName()) && transformer.getInput().apply(stack)) {
                return transformer;
            }
        }

        return null;
    }

    public EntityInfo getSpawnForTransformation(@Nonnull Fluid fluid, @Nonnull ItemStack stack) {
        for (FluidBlockTransformer transformer : registry) {
            if (fluid.getName().equals(transformer.getFluidName()) && transformer.getInput().apply(stack)) {
                return transformer.getToSpawn();
            }
        }

        return null;
    }

    @Override
    protected void registerEntriesFromJSON(FileReader fr) {
        List<FluidBlockTransformer> gsonInput = gson.fromJson(fr, new TypeToken<List<FluidBlockTransformer>>() {}.getType());
        registry.addAll(gsonInput);
    }

    @Override
    public List<?> getRecipeList() {
        return new ArrayList<>();
    }
}