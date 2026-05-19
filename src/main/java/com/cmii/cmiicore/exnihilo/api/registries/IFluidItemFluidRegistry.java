package com.cmii.cmiicore.exnihilo.api.registries;

import com.cmii.cmiicore.exnihilo.registries.types.FluidItemFluid;
import com.cmii.cmiicore.exnihilo.util.StackInfo;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nullable;

public interface IFluidItemFluidRegistry extends IRegistryList<FluidItemFluid> {
    void register(String inputFluid, StackInfo reactant, String outputFluid);

    void register(Fluid inputFluid, StackInfo reactant, Fluid outputFluid);

    @Nullable
    String getFluidForTransformation(Fluid fluid, ItemStack stack);
}