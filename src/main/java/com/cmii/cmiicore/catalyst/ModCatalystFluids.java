package com.cmii.cmiicore.catalyst;

import com.cmii.cmiicore.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class ModCatalystFluids {

    public static Fluid stellarEssence;

    public static void init() {
        stellarEssence = new Fluid("stellar_essence",
                new ResourceLocation(Reference.MOD_ID, "blocks/stellar_essence_still"),
                new ResourceLocation(Reference.MOD_ID, "blocks/stellar_essence_flow"));
        FluidRegistry.registerFluid(stellarEssence);
        FluidRegistry.addBucketForFluid(stellarEssence);

        ModCatalystBlocks.fluidStellarEssence = new com.cmii.cmiicore.catalyst.blocks.BlockFluidStellarEssence();
    }
}
