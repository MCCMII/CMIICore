package com.cmii.cmiicore.exnihilo.api.registries;

import com.cmii.cmiicore.exnihilo.registries.types.WitchWaterWorld;
import com.cmii.cmiicore.exnihilo.util.BlockInfo;
import net.minecraftforge.fluids.Fluid;

public interface IWitchWaterWorldRegistry extends IRegistryMap<Fluid, WitchWaterWorld> {
    boolean contains(Fluid fluid);

    BlockInfo getResult(Fluid fluid, float chance);
}