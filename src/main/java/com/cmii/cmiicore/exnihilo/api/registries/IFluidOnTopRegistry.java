package com.cmii.cmiicore.exnihilo.api.registries;

import com.cmii.cmiicore.exnihilo.registries.types.FluidFluidBlock;
import com.cmii.cmiicore.exnihilo.util.BlockInfo;
import com.cmii.cmiicore.exnihilo.util.ItemInfo;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nullable;

public interface IFluidOnTopRegistry extends IRegistryList<FluidFluidBlock> {
    void register(Fluid fluidInBarrel, Fluid fluidOnTop, BlockInfo result);

    void register(Fluid fluidInBarrel, Fluid fluidOnTop, ItemInfo result);

    boolean isValidRecipe(@Nullable Fluid fluidInBarrel, @Nullable Fluid fluidOnTop);

    BlockInfo getTransformedBlock(Fluid fluidInBarrel, Fluid fluidOnTop);
}