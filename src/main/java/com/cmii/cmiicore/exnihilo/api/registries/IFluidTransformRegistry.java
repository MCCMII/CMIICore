package com.cmii.cmiicore.exnihilo.api.registries;

import com.cmii.cmiicore.exnihilo.registries.types.FluidTransformer;
import com.cmii.cmiicore.exnihilo.util.BlockInfo;

import javax.annotation.Nullable;
import java.util.List;

public interface IFluidTransformRegistry extends IRegistryMappedList<String, FluidTransformer> {
    void register(String inputFluid, String outputFluid, int duration,
                  BlockInfo[] transformingBlocks, BlockInfo[] blocksToSpawn);

    void register(FluidTransformer transformer);

    boolean containsKey(String inputFluid);

    @Nullable
    FluidTransformer getFluidTransformer(String inputFluid, String outputFluid);

    List<FluidTransformer> getFluidTransformers(String inputFluid);
}