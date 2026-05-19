package com.cmii.cmiicore.exnihilo.api.registries;

import com.cmii.cmiicore.exnihilo.registries.types.FluidBlockTransformer;
import com.cmii.cmiicore.exnihilo.util.BlockInfo;
import com.cmii.cmiicore.exnihilo.util.EntityInfo;
import com.cmii.cmiicore.exnihilo.util.StackInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nullable;

public interface IFluidBlockTransformerRegistry extends IRegistryList<FluidBlockTransformer> {
    void register(@Nullable Fluid fluid, StackInfo inputBlock, StackInfo outputBlock, @Nullable String entityName);

    void register(Fluid fluid, StackInfo inputBlock, StackInfo outputBlock);

    void register(Fluid fluid, String oredict, StackInfo outputBlock, @Nullable String entityName);

    void register(Fluid fluid, String oredict, StackInfo outputBlock);

    void register(String fluid, Ingredient input, StackInfo outputBlock, @Nullable String entityName,
                  int spawnCount, int spawnRange);

    boolean canBlockBeTransformedWithThisFluid(Fluid fluid, ItemStack stack);

    BlockInfo getBlockForTransformation(Fluid fluid, ItemStack stack);

    int getSpawnCountForTransformation(Fluid fluid, ItemStack stack);

    int getSpawnRangeForTransformation(Fluid fluid, ItemStack stack);

    @Nullable
    FluidBlockTransformer getTransformation(Fluid fluid, ItemStack stack);

    @Nullable
    EntityInfo getSpawnForTransformation(Fluid fluid, ItemStack stack);
}